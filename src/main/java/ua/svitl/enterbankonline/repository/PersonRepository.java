package ua.svitl.enterbankonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.svitl.enterbankonline.model.Person;
import ua.svitl.enterbankonline.model.User;

import java.math.BigInteger;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findPersonByIdNumberTaxCode(BigInteger taxCode);
    Optional<Person> findPersonByPersonId(int personId);
    Optional<Person> findPersonByPersonUsersContains(User user);
}
