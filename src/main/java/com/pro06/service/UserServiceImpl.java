package com.pro06.service;

import com.pro06.dto.UserDTO;
import com.pro06.entity.BaseEntity;
import com.pro06.entity.Role;
import com.pro06.entity.Status;
import com.pro06.entity.User;
import com.pro06.repository.UserRepository;
import com.sun.security.auth.UnixNumericUserPrincipal;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<UserDTO> userList() {
        List<User> userList = userRepository.findAll();
        List<UserDTO> userDTOList = userList.stream().map(
                user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
        return userDTOList;
    }

    @Override
    public void userDelete(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public void userInsert(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        userRepository.save(user);
    }

    @Override
    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }

    @Override
    public UserDTO getId(String id) {
        User user = userRepository.getId(id);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }

    @Override
    public User LoginId(String id) {
        User user = userRepository.getId(id);
        return user;
    }

    @Override
    public void userUpdate(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        userRepository.save(user);
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
                pass = 2;
            } else if (user.getStatus().equals(Status.OUT)) {
                pass = 3;
            }
        }
        return pass;
    }
}
