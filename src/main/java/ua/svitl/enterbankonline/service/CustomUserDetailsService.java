package ua.svitl.enterbankonline.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ua.svitl.enterbankonline.model.Role;
import ua.svitl.enterbankonline.model.User;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) {
        User user = userService.findUserByUserName(userName);
        List<GrantedAuthority> authority = getUserAuthority(user.getRoleByRoleId());
        return buildUserForAuthentication(user, authority);
    }

    private List<GrantedAuthority> getUserAuthority(Role userRole) {
        List<GrantedAuthority> role = new ArrayList<>();
        role.add(new SimpleGrantedAuthority(userRole.getRoleName()));
        return role;
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authority) {
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
                user.getIsActive(), true, true, true, authority);
    }
}