package com.pro06.service;

import com.pro06.entity.BaseEntity;
import com.pro06.entity.Role;
import com.pro06.entity.Status;
import com.pro06.entity.User;
import com.pro06.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User userInsert(User user) {
        user.setPw(passwordEncoder.encode(user.getPw()));
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        return userRepository.save(user);
    }

    @Override
    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }

    @Override
    public User getId(String id) {
        return userRepository.getId(id);
    }

    @Override
    public User userUpdate(User user) {
        return userRepository.save(user);
    }

    @Override
    public int loginPro(String id) {
        int pass = 0;
        User user = userRepository.getId(id);
        LocalDateTime local = LocalDateTime.now().minusDays(30);

        if (local.isAfter(user.getLoginAt())) {
            user.setStatus(Status.REST);
            userRepository.save(user);
            pass = 2;
        } else {
            if (user.getStatus().equals(Status.ACTIVE)) {
                user.setLoginAt(LocalDateTime.now());
                userRepository.save(user);
                pass = 1;
            } else if (user.getStatus().equals(Status.REST)) {
                new AntPathRequestMatcher("/logout");
                pass = 2;
            } else if (user.getStatus().equals(Status.OUT)) {
                new AntPathRequestMatcher("/logout");
                pass = 3;
            }
            return pass;
        }
        return pass;
    }
}
