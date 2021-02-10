package ua.svitl.enterbankonline.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.svitl.enterbankonline.model.Payment;
import ua.svitl.enterbankonline.model.Person;

import java.util.Optional;


public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findPaymentByPaymentId(int paymentId);
    Page<Payment> findAllByBankAccount_PersonByPersonId(Person person, Pageable pageable);
}
