package ua.svitl.enterbankonline.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.svitl.enterbankonline.model.BankAccount;
import ua.svitl.enterbankonline.model.CreditCard;
import ua.svitl.enterbankonline.model.Payment;
import ua.svitl.enterbankonline.model.User;
import ua.svitl.enterbankonline.repository.BankAccountRepository;
import ua.svitl.enterbankonline.repository.CreditCardRepository;
import ua.svitl.enterbankonline.repository.PaymentRepository;

import java.math.BigInteger;
import java.util.List;

@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final CreditCardRepository creditCardRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository, CreditCardRepository creditCardRepository, PaymentRepository paymentRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.creditCardRepository = creditCardRepository;
        this.paymentRepository = paymentRepository;
    }

    List<BankAccount> bankAccountsByAccountTypeOrderByAccountTypeAsc(String accountType) {
        return bankAccountRepository.findBankAccountsByAccountTypeOrderByAccountTypeAsc(accountType);
    }

    List<BankAccount> bankAccountsByAccountTypeOrderByAccountTypeDesc(String accountType) {
        return bankAccountRepository.findBankAccountsByAccountTypeOrderByAccountTypeDesc(accountType);
    }

    List<BankAccount> bankAccountsByAccountAmountOrderByAccountAmountAsc(Double amount) {
        return bankAccountRepository.findBankAccountsByAccountAmountOrderByAccountAmountAsc(amount);
    }

    List<BankAccount> bankAccountsByAccountAmountOrderByAccountAmountDesc(Double amount) {
        return bankAccountRepository.findBankAccountsByAccountAmountOrderByAccountAmountDesc(amount);
    }
    List<BankAccount> bankAccountsByBankAccountNumberOrderByBankAccountNumberAsc(BigInteger accountNumber) {
        return bankAccountRepository.findBankAccountsByBankAccountNumberOrderByBankAccountNumberAsc(accountNumber);
    }

    List<BankAccount> bankAccountsByBankAccountNumberOrderByBankAccountNumberDesc(BigInteger accountNumber) {
        return bankAccountRepository.findBankAccountsByBankAccountNumberOrderByBankAccountNumberDesc(accountNumber);
    }

    List<Payment> paymentsByIsSent(Boolean isSent) {
        return paymentRepository.findPaymentsByIsSent(isSent);
    }

    List<Payment> paymentsByBankAccountByBankAccountId(BankAccount bankAccount) {
        return paymentRepository.findPaymentsByBankAccountByBankAccountId(bankAccount);
    }

    CreditCard creditCardByCardNumber(BigInteger cardNumber){
        return creditCardRepository.findCreditCardByCardNumber(cardNumber);
    }

    List<CreditCard> creditCardsByBankAccountByBankAccountId(BankAccount account) {
        return creditCardRepository.findCreditCardsByBankAccountByBankAccountId(account);
    }

    List<CreditCard> creditCardsByIsActive(Boolean isActive){
        return creditCardRepository.findCreditCardsByIsActive(isActive);
    }

    List<BankAccount> bankAccountsByUserByUserId(User user) {
        return bankAccountRepository.findBankAccountsByUserByUserId(user);
    }

    List<BankAccount> bankAccountsByUserByUserIdAndCurrency(User user, String currency) {
        return bankAccountRepository.findBankAccountsByUserByUserIdAndCurrency(user, currency);
    }

    BankAccount bankAccountByBankAccountNumberEndsWith(BigInteger accountNumberEnd) {
       return bankAccountRepository.findBankAccountByBankAccountNumberEndsWith(accountNumberEnd);
    }

    BankAccount bankAccountByBankAccountId(int accountId) {
        return bankAccountRepository.findBankAccountByBankAccountId(accountId);
    }

    BankAccount bankAccountByIsActive(Boolean isActive) {
        return bankAccountRepository.findBankAccountByIsActive(isActive);
    }

    public Page<BankAccount> findPaginated(User user, int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return bankAccountRepository.findAllByUserByUserId(user, pageable);
    }

}
