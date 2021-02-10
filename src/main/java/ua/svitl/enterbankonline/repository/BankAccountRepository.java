package ua.svitl.enterbankonline.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.svitl.enterbankonline.model.BankAccount;
import ua.svitl.enterbankonline.model.Person;

import java.util.Optional;


public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    Optional<BankAccount> findBankAccountByBankAccountId(int accountId);
    Page<BankAccount> findAllByPersonByPersonId(Person person, Pageable pageable);
    Optional<BankAccount> findBankAccountByBankAccountNumber(String accountNumber);
}
