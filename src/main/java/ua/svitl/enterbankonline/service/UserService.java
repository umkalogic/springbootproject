package ua.svitl.enterbankonline.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.svitl.enterbankonline.model.Role;
import ua.svitl.enterbankonline.model.User;
import ua.svitl.enterbankonline.model.dto.UserPersonDataDto;
import ua.svitl.enterbankonline.repository.RoleRepository;
import ua.svitl.enterbankonline.repository.UserRepository;
import ua.svitl.enterbankonline.utilities.UserManagementExceptions;

import javax.transaction.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
   }

    User findUserByUserName(String userName) {
        return userRepository.findUserByUserName(userName)
                .orElseThrow(() -> new UserManagementExceptions("User not found: no such username"));
    }

    private User findUserById(int id) {
        return userRepository.findUserByUserId(id)
                .orElseThrow(() -> new UserManagementExceptions("User not found: no such user id"));
    }

    public User getUserByUserId(int id) {
        try {
            return findUserById(id);
        } catch (UserManagementExceptions ex) {
            // TODO log exception
            return new User();
        }
    }

    public User getUserByName(String userName) {
        try {
            return findUserByUserName(userName);
        } catch (UserManagementExceptions ex) {
            // TODO log exception
            return new User();
        }
    }

    @Transactional
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setIsActive(true);
        Role userRole = roleRepository.findRoleByRoleName("USER");
        user.setRoleByRoleId(userRole);
        return userRepository.save(user);
    }

    public Role getRoleByName(String name) {
        return roleRepository.findRoleByRoleName(name);
    }

    @Transactional
    public User updateUser(final User userFromPost) {
        User existingUser = userRepository.findUserByUserId(userFromPost.getUserId())
                .orElseThrow(() -> new UserManagementExceptions("User not found: no such user id"));;
        existingUser.setUserName(userFromPost.getUserName());
        existingUser.setEmail(userFromPost.getEmail());
        existingUser.setIsActive(userFromPost.getIsActive());
        return userRepository.save(existingUser);
    }

    @Transactional
    public User updateUserActive(int id, Boolean activeStatus) {
        try {
            User existingUser = userRepository.findUserByUserId(id)
                    .orElseThrow(() -> new UserManagementExceptions("User not found: no user to activate"));
            existingUser.setIsActive(activeStatus);
            return userRepository.save(existingUser);
        } catch (UserManagementExceptions ex) {
            // TODO log exception
            return new User();
        }
    }


    public Page<UserPersonDataDto> findPaginated(int pageNo, int pageSize,
                                                 String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return userRepository.getUserPersonData(pageable);
    }

}
