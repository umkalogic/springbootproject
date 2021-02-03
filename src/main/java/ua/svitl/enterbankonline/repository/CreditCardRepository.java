package ua.svitl.enterbankonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.svitl.enterbankonline.model.BankAccount;
import ua.svitl.enterbankonline.model.CreditCard;

import java.math.BigInteger;
import java.util.List;

public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {
    CreditCard findCreditCardByCardNumber(BigInteger cardNumber);
    List<CreditCard> findCreditCardsByBankAccountByBankAccountId(BankAccount account);
    List<CreditCard> findCreditCardsByIsActive(Boolean isActive);
}
