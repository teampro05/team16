package com.pro06.service;

import com.pro06.entity.Role;
import com.pro06.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public interface UserService {
    public List<User> userList();
    public User userInsert(User user);
    public PasswordEncoder passwordEncoder();
    public User getId(String id);
    public User userUpdate(User user);
    public void userDelete(Integer id);
    public int loginPro(String id);

}
