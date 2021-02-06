package ua.svitl.enterbankonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.svitl.enterbankonline.model.PassportData;
import ua.svitl.enterbankonline.model.Person;

import java.util.Optional;


public interface PassportDataRepository extends JpaRepository<PassportData, Integer> {
    Optional<PassportData> findFirstByPersonByPersonIdAndIsDomesticTrue(Person person);
    Optional<PassportData> findFirstByPersonByPersonId(Person person);
}
