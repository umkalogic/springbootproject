package ua.svitl.enterbankonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.svitl.enterbankonline.model.Role;
import ua.svitl.enterbankonline.model.User;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findRoleByRoleName(String roleName);
}