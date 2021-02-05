package ua.svitl.enterbankonline.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.svitl.enterbankonline.model.Role;
import ua.svitl.enterbankonline.model.User;
import ua.svitl.enterbankonline.repository.RoleRepository;
import ua.svitl.enterbankonline.repository.UserRepository;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByUserName(String userName) {
        return userRepository.findUserByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    User findUserByIdNumberTaxCode(BigInteger taxNumber) {
        return userRepository.findUserByIdNumberTaxCode(taxNumber);
    }

    User findUserByEmail(String email){
        return userRepository.findUserByEmail(email);
    }

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setIsActive(true);
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(final User userFromPost) {
        User existingUser = userRepository.findUserByUserId(userFromPost.getUserId());
        existingUser.setUserName(userFromPost.getUserName());
        existingUser.setLastName(userFromPost.getLastName());
        existingUser.setFirstName(userFromPost.getFirstName());
        existingUser.setEmail(userFromPost.getEmail());
        existingUser.setIsActive(userFromPost.getIsActive());
        return userRepository.save(existingUser);
    }

    public User getUserId(int id) {
        return userRepository.findUserByUserId(id);
    }


    public Page<User> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return userRepository.findAll(pageable);
    }
}
