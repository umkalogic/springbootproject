package ua.svitl.enterbankonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.svitl.enterbankonline.model.BankAccount;

import java.math.BigInteger;
import java.util.List;


public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    List<BankAccount> findBankAccountsByAccountTypeOrderByAccountTypeAsc(String accountType);
    List<BankAccount> findBankAccountsByAccountTypeOrderByAccountTypeDesc(String accountType);
    List<BankAccount> findBankAccountsByAccountAmountOrderByAccountAmountAsc(Double amount);
    List<BankAccount> findBankAccountsByAccountAmountOrderByAccountAmountDesc(Double amount);
    List<BankAccount> findBankAccountsByBankAccountNumberOrderByBankAccountNumberAsc(BigInteger accountNumber);
    List<BankAccount> findBankAccountsByBankAccountNumberOrderByBankAccountNumberDesc(BigInteger accountNumber);
}
