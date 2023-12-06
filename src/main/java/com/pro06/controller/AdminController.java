package com.pro06.controller;

import com.pro06.entity.Role;
import com.pro06.entity.User;
import com.pro06.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Log4j2
@Controller
@RequestMapping("/admin")
public class AdminController {


    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/home")
    public String Home(Model model) {
        return "/admin/home";
    }
}