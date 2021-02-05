package ua.svitl.enterbankonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.svitl.enterbankonline.model.User;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUserName(String userName);
    User findUserByIdNumberTaxCode(BigInteger taxNumber);
    User findUserByEmail(String email);
    User findUserByUserId(int id);
}