package ua.svitl.enterbankonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.svitl.enterbankonline.model.Payment;



public interface PaymentRepository extends JpaRepository<Payment, Integer> {


}
