package com.pro06.service;

import com.pro06.dto.UserDTO;
import com.pro06.entity.Role;
import com.pro06.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public interface UserService {
    public List<UserDTO> userList();
    public void userInsert(UserDTO userDTO);
    public PasswordEncoder passwordEncoder();
    public UserDTO getId(String id);
    public User LoginId(String id);
    public void userUpdate(UserDTO userDTO);
    public void userDelete(Integer id);
    public int loginPro(String id);

}
