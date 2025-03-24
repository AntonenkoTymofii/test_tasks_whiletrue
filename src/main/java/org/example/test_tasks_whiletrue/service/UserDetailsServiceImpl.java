package org.example.test_tasks_whiletrue.service;

import org.example.test_tasks_whiletrue.converter.UserConverter;
import org.example.test_tasks_whiletrue.exception.userExceptions.UserNotFoundException;
import org.example.test_tasks_whiletrue.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel userModel = null;
        try {
            userModel = userService.getUserModelByEmail(email);
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException("Користувача не знайдено");
        }

        return new User(
                userModel.getEmail(),
                userModel.getHash(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

}
