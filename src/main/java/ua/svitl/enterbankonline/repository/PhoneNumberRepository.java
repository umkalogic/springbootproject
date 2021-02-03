package ua.svitl.enterbankonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.svitl.enterbankonline.model.PhoneNumber;

import java.util.List;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Integer> {
    List<PhoneNumber> findPhoneNumbersByPhone(String phoneWithoutCountryCode);
}
