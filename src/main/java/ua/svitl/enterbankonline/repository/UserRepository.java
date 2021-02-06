package ua.svitl.enterbankonline.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.svitl.enterbankonline.model.User;
import ua.svitl.enterbankonline.model.dto.UserPersonDataDto;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUserName(String userName);
    Optional<User> findUserByUserId(int id);
    @Query("select new ua.svitl.enterbankonline.model.dto.UserPersonDataDto(u.userId, p.personId, " +
            "u.userName, u.email, " +
            "p.lastName, p.firstName, p.middleName, p.birthDate, " +
            "f.phoneCountryCode, f.phone, u.isActive) " +
            "from User u " +
            "join u.personByPersonId p " +
            "join p.personPhoneNumbers f " +
            "where u.roleByRoleId.roleName = 'USER' " +
            "and f.isPrimary = true")
    Page<UserPersonDataDto> getUserPersonData(Pageable pageable);
}