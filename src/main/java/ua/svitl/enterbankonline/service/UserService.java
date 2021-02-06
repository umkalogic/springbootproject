package ua.svitl.enterbankonline.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.svitl.enterbankonline.model.Person;
import ua.svitl.enterbankonline.model.Role;
import ua.svitl.enterbankonline.model.User;
import ua.svitl.enterbankonline.model.dto.UserPersonDataDto;
import ua.svitl.enterbankonline.repository.PersonRepository;
import ua.svitl.enterbankonline.repository.RoleRepository;
import ua.svitl.enterbankonline.repository.UserRepository;

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

    public User findUserByUserName(String userName) {
        return userRepository.findUserByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: no such username"));
    }

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
                .orElseThrow(() -> new UsernameNotFoundException("User not found: no such user id"));;
        existingUser.setUserName(userFromPost.getUserName());
        existingUser.setEmail(userFromPost.getEmail());
        existingUser.setIsActive(userFromPost.getIsActive());
        return userRepository.save(existingUser);
    }

    @Transactional
    public User updateUserActive(final User user, Boolean activeStatus) {
        User existingUser = userRepository.findUserByUserId(user.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: no user to activate"));;
        existingUser.setIsActive(activeStatus);
        return userRepository.save(existingUser);
    }

    public User getUserById(int id) {
        return userRepository.findUserByUserId(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: no such user id"));
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
