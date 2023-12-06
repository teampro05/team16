package com.pro06.controller;

import com.pro06.entity.Role;
import com.pro06.entity.User;
import com.pro06.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Log4j2
@Controller
public class HomeController {


    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String Home(Model model){
        return "index";
    }

    @GetMapping("/term")
    public String Term(Model model){
        return "/user/joinTerm";
    }

    @GetMapping("/login")
    public String Login(Model model){
        return "/user/login";
    }

    @GetMapping("/active")
    public String active(Model model){
        return "/user/active";
    }

    @GetMapping("/status")
    public String status(Model model, Principal principal){
        String id = principal.getName();
        int pass = userService.loginPro(id);
        if (pass == 1) {
            model.addAttribute("msg", "로그인 되었습니다.");
            model.addAttribute("url", "/");
            return "/alert";
        } else if (pass == 2) {
            model.addAttribute("msg", "해당 계정은 휴면계정입니다. 휴면을 풀어주세요.");
            model.addAttribute("url", "/active");
            return "/alert";
        } else if (pass==3){
            model.addAttribute("msg", "해당 계정은 탈퇴한 계정입니다.");
            model.addAttribute("url", "/");
            return "/alert";
        } else {
            model.addAttribute("msg", "로그인 정보가 맞지 않습니다.");
            model.addAttribute("url", "/login");
            return "/alert";
        }
    }



    @GetMapping("/join1")
    public String JoinForm(Model model){
        return "/user/join1";
    }

    @PostMapping("/joinPro1")
    public String Join(Model model, User user){
        userService.userInsert(user);
        return "redirect:/";
    }

    @GetMapping("/error")
    public String error(Model model){
        return "/index";
    }

    @GetMapping("/myPage")
    public String Exindex(Model model, Principal principal){
        User user = userService.getId(principal.getName());
        model.addAttribute("principal", principal);
        model.addAttribute("user", user);
        return "/user/myPage";
    }

    @PostMapping("/out")
    public String out1(Principal principal, Model model, Role role){
        User user = userService.getId(principal.getName());
        user.setRole(role);
        user.setAddr1("1234번지");
        userService.userUpdate(user);
        return "redirect:/";
    }

}
