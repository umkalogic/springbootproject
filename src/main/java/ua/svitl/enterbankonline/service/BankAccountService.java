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
import ua.svitl.enterbankonline.model.Person;
import ua.svitl.enterbankonline.repository.BankAccountRepository;
import ua.svitl.enterbankonline.repository.CreditCardRepository;
import ua.svitl.enterbankonline.repository.PaymentRepository;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<BankAccount> bankAccountsByAccountTypeOrderByAccountTypeAsc(String accountType) {
        return bankAccountRepository.findBankAccountsByAccountTypeOrderByAccountTypeAsc(accountType);
    }

    public List<BankAccount> bankAccountsByAccountTypeOrderByAccountTypeDesc(String accountType) {
        return bankAccountRepository.findBankAccountsByAccountTypeOrderByAccountTypeDesc(accountType);
    }

    public List<BankAccount> bankAccountsByAccountAmountOrderByAccountAmountAsc(Double amount) {
        return bankAccountRepository.findBankAccountsByAccountAmountOrderByAccountAmountAsc(amount);
    }

    public List<BankAccount> bankAccountsByAccountAmountOrderByAccountAmountDesc(Double amount) {
        return bankAccountRepository.findBankAccountsByAccountAmountOrderByAccountAmountDesc(amount);
    }
    public List<BankAccount> bankAccountsByBankAccountNumberOrderByBankAccountNumberAsc(BigInteger accountNumber) {
        return bankAccountRepository.findBankAccountsByBankAccountNumberOrderByBankAccountNumberAsc(accountNumber);
    }

    public List<BankAccount> bankAccountsByBankAccountNumberOrderByBankAccountNumberDesc(BigInteger accountNumber) {
        return bankAccountRepository.findBankAccountsByBankAccountNumberOrderByBankAccountNumberDesc(accountNumber);
    }

    public List<Payment> paymentsByIsSent(Boolean isSent) {
        return paymentRepository.findPaymentsByIsSent(isSent);
    }

    public List<Payment> paymentsByBankAccountByBankAccountId(BankAccount bankAccount) {
        return paymentRepository.findPaymentsByBankAccountByBankAccountId(bankAccount);
    }

    public CreditCard getCardByCardNumber(BigInteger cardNumber){
        return creditCardRepository.findCreditCardByCardNumber(cardNumber);
    }

    public List<CreditCard> getCardsByBankAccount(BankAccount account) {
        return creditCardRepository.findCreditCardsByBankAccountByBankAccountId(account);
    }

    public List<CreditCard> getCardsByIsActive(Boolean isActive){
        return creditCardRepository.findCreditCardsByIsActive(isActive);
    }

    public List<BankAccount> getBankAccountsByPerson(Person person) {
        return bankAccountRepository.findBankAccountsByPersonByPersonId(person);
    }

    public List<BankAccount> getBankAccountsByPersonAndCurrency(Person person, String currency) {
        return bankAccountRepository.findBankAccountsByPersonByPersonIdAndCurrency(person, currency);
    }

    public BankAccount getBankAccountByBankAccountNumberEndsWith(BigInteger accountNumberEnd) {
       return bankAccountRepository.findBankAccountByBankAccountNumberEndsWith(accountNumberEnd);
    }

    public BankAccount getBankAccountByBankAccountId(int accountId) {
        return bankAccountRepository.findBankAccountByBankAccountId(accountId);
    }

    public List<BankAccount> getBankAccountsByIsActive(Boolean isActive) {
        return bankAccountRepository.findBankAccountsByIsActive(isActive);
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

    public Map<Integer, List<CreditCard>> getCardsForEachBankAccount(List<BankAccount> listBankAccounts) {
        Map<Integer, List<CreditCard>> accountCards = new LinkedHashMap<>();
        List<Integer> listAccountIds = listBankAccounts.stream()
                .map(BankAccount::getBankAccountId)
                .collect(Collectors.toList());
        List<CreditCard> listCards;
        for (int i = 0; i < listBankAccounts.size(); i++) {
            BankAccount account = listBankAccounts.get(i);
            listCards = creditCardRepository.findCreditCardsByBankAccountByBankAccountId(account);
            accountCards.put(listAccountIds.get(i), listCards);
        }
        return accountCards;
    }
}
