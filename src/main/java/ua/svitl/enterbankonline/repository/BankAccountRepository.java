package ua.svitl.enterbankonline.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.svitl.enterbankonline.model.BankAccount;
import ua.svitl.enterbankonline.model.Person;

import java.math.BigInteger;
import java.util.List;


public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    List<BankAccount> findBankAccountsByAccountTypeOrderByAccountTypeAsc(String accountType);
    List<BankAccount> findBankAccountsByAccountTypeOrderByAccountTypeDesc(String accountType);
    List<BankAccount> findBankAccountsByAccountAmountOrderByAccountAmountAsc(Double amount);
    List<BankAccount> findBankAccountsByAccountAmountOrderByAccountAmountDesc(Double amount);
    List<BankAccount> findBankAccountsByBankAccountNumberOrderByBankAccountNumberAsc(BigInteger accountNumber);
    List<BankAccount> findBankAccountsByBankAccountNumberOrderByBankAccountNumberDesc(BigInteger accountNumber);
    List<BankAccount> findBankAccountsByPersonByPersonId(Person person);
    List<BankAccount> findBankAccountsByPersonByPersonIdAndCurrency(Person person, String currency);
    BankAccount findBankAccountByBankAccountNumberEndsWith(BigInteger accountNumberEnd);
    BankAccount findBankAccountByBankAccountId(int accountId);
    List<BankAccount> findBankAccountsByIsActive(Boolean isActive);
    Page<BankAccount> findAllByPersonByPersonId(Person person, Pageable pageable);
}
