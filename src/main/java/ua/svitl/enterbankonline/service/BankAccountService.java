package ua.svitl.enterbankonline.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.svitl.enterbankonline.model.BankAccount;
import ua.svitl.enterbankonline.model.CreditCard;
import ua.svitl.enterbankonline.model.Payment;
import ua.svitl.enterbankonline.model.Person;
import ua.svitl.enterbankonline.model.dto.ExistingPaymentDto;
import ua.svitl.enterbankonline.model.dto.PaymentDto;
import ua.svitl.enterbankonline.repository.BankAccountRepository;
import ua.svitl.enterbankonline.repository.CreditCardRepository;
import ua.svitl.enterbankonline.repository.PaymentRepository;
import ua.svitl.enterbankonline.utilities.BankTransactionException;

import javax.transaction.Transactional;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final CreditCardRepository creditCardRepository;
    private final PaymentRepository paymentRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository,
                              CreditCardRepository creditCardRepository,
                              PaymentRepository paymentRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.creditCardRepository = creditCardRepository;
        this.paymentRepository = paymentRepository;
    }

    private static String sortFieldAdjustment(String fieldName) {
        switch (fieldName) {
            case "bankaccountnumber":
                fieldName = "bankAccountNumber";
                break;
            case "accountype":
                fieldName = "accountType";
                break;
            case "accountamount":
                fieldName = "accountAmount";
                break;
            case "paymentid":
                fieldName = "paymentId";
                break;
            case "paymentdate":
                fieldName = "paymentDate";
                break;
            case "paymentamount":
                fieldName = "paymentAmount";
                break;
            case "bankaccountid":
                fieldName = "bankAccountId";
                break;
            default:
                return fieldName;
        }
        return fieldName;
    }

    public Page<BankAccount> findPaginated(Person person, int pageNo, int pageSize, String sortField, String sortDirection) {
        sortField = sortFieldAdjustment(sortField);
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return bankAccountRepository.findAllByPersonByPersonId(person, pageable);
    }

    public Page<Payment> findPaginatedPayments(Person person, int pageNo, int pageSize, String sortField, String sortDirection) {
        sortField = sortFieldAdjustment(sortField);
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return paymentRepository.findAllByBankAccount_PersonByPersonId(person, pageable);
    }

    public Map<BankAccount, List<CreditCard>> getCardsForEachBankAccount(List<BankAccount> listBankAccounts) {
        Map<BankAccount, List<CreditCard>> accountCards = new LinkedHashMap<>();
        List<CreditCard> listCards;
        for (BankAccount account: listBankAccounts) {
            listCards = creditCardRepository.findCreditCardsByBankAccount(account);
            accountCards.put(account, listCards);
        }
        return accountCards;
    }

    private BankAccount getBankAccountByBankAccountId(int accountId) {
        return bankAccountRepository.findBankAccountByBankAccountId(accountId)
                .orElseThrow(() -> new BankTransactionException("No such bank account: wrong id"));
    }

    private Payment getPaymentByPaymentId(int paymentId) {
        return paymentRepository.findPaymentByPaymentId(paymentId)
                .orElseThrow(() -> new BankTransactionException("No such payment: wrong id"));

    }

    public Payment getPaymentById(int id) {
        try {
            return getPaymentByPaymentId(id);
        } catch (BankTransactionException ex) {
            return new Payment();
        }
    }

    @Transactional
    public Payment savePreparedPayment(PaymentDto paymentDto, int bankAccountId) {
        try {
            Payment thePayment = new Payment();
            BankAccount bankAccount = getBankAccountByBankAccountId(bankAccountId);
            if (paymentDto.getToBankAccount().equals(bankAccount.getBankAccountNumber())) {
                throw new BankTransactionException("You cannot make payment to the same account!");
            }
            if (bankAccount.getAccountAmount() < paymentDto.getPaymentAmount()) {
                throw new BankTransactionException("You don't have enough money to perform this transaction");
            }
            thePayment.setToBankAccount(paymentDto.getToBankAccount());
            thePayment.setPaymentAmount(paymentDto.getPaymentAmount());

            thePayment.setBankAccount(bankAccount);
            thePayment.setIsSent(false);
            return paymentRepository.save(thePayment);
        } catch (BankTransactionException ex) {
            return new Payment();
        }
    }

    private BankAccount getBankAccountByBankAccountNumber(String accountNumber) {
        return bankAccountRepository.findBankAccountByBankAccountNumber(accountNumber)
                .orElseThrow(() -> new BankTransactionException(
                        "No such bank account to which to pay to: wrong account number")
                );
    }


    @Transactional
    public Payment updatePayment(Payment postPayment) {
        try {
            Payment payment = getPaymentByPaymentId(postPayment.getPaymentId());

            BankAccount fromBankAccount = payment.getBankAccount();

            Double theAmountAvailable = fromBankAccount.getAccountAmount();
            Double theAmountToPay = payment.getPaymentAmount();

            if (theAmountToPay > theAmountAvailable) {
                throw new BankTransactionException("You don't have enough money to perform transaction.");
            }

            BankAccount toBankAccount = getBankAccountByBankAccountNumber(payment.getToBankAccount());
            if (Objects.equals(toBankAccount, fromBankAccount)) {
                throw new BankTransactionException("You cannot send money to the same account!");
            }

            // payment to
            toBankAccount.setAccountAmount(toBankAccount.getAccountAmount() + theAmountToPay);
            bankAccountRepository.save(toBankAccount);

            // payment from
            fromBankAccount.setAccountAmount(fromBankAccount.getAccountAmount() - theAmountToPay);
            bankAccountRepository.save(fromBankAccount);

            // send payment
            payment.setIsSent(true);
            paymentRepository.save(payment);

            //TODO: make return String?
            return payment; //
        } catch (BankTransactionException ex) {
            // TODO log exception
            return new Payment();
        }
    }

    // @Transactional
    public String updateAccountActive(int id, boolean activate) {
        try {
            BankAccount bankAccount = getBankAccountByBankAccountId(id);
            if (activate) {
                bankAccount.setEnableRequest(true);
            } else {
                bankAccount.setIsActive(false);
            }
            bankAccountRepository.save(bankAccount);
             return "Update successful";
        } catch (BankTransactionException ex) {
            // TODO log exception
            return ex.getMessage();
        }
    }

    // @Transactional
    public String updateAccount(int id, boolean activate) {
        try {
            BankAccount bankAccount = getBankAccountByBankAccountId(id);
            bankAccount.setIsActive(activate);
            bankAccount.setEnableRequest(false);
            bankAccountRepository.save(bankAccount);
            return "Update successful";
        } catch (BankTransactionException ex) {
            // TODO log exception
            return ex.getMessage();
        }
    }

    public String getBankAccountNumberById(int accountId) {
        try {
            return getBankAccountByBankAccountId(accountId).getBankAccountNumber();
        } catch (BankTransactionException ex) {
            return ex.getMessage();
        }
    }

    @Transactional
    public Payment getPaymentByExistingPayment(ExistingPaymentDto paymentDto) {
        try {
            Payment thePayment = getPaymentById(paymentDto.getPaymentId());
            thePayment.setPaymentAmount(paymentDto.getPaymentAmount());
            thePayment.setToBankAccount(paymentDto.getToBankAccount());
            updatePayment(thePayment);
            return thePayment;
        } catch (BankTransactionException ex) {
            return new Payment();
        }
    }

    @Transactional
    public String deletePaymentById(int paymentId) {
        try {
            paymentRepository.delete(getPaymentByPaymentId(paymentId));
            return "Delete successful";
        } catch (BankTransactionException ex) {
            return ex.getMessage();
        }
    }
}
