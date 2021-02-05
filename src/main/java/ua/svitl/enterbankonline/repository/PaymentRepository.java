package ua.svitl.enterbankonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.svitl.enterbankonline.model.BankAccount;
import ua.svitl.enterbankonline.model.Payment;

import java.util.List;


public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findPaymentsByIsSent(Boolean isSent);
    List<Payment> findPaymentsByBankAccountByBankAccountId(BankAccount bankAccount);
}
