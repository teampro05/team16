package com.pro06.controller;

import com.pro06.dto.BoardDTO;
import com.pro06.entity.*;
import com.pro06.service.FaqService;
import com.pro06.service.NoticeService;
import com.pro06.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Log4j2
@Controller
public class HomeController {


    @Autowired
    private UserService userService;

    @Autowired
    private FaqService faqService;

    @Autowired
    private NoticeService noticeService;

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


    @GetMapping("/oto")
    public String oto(Model model){
        return "/board/oto";
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
            model.addAttribute("url", "/logout");
            return "/alert";
        } else if (pass==3){
            model.addAttribute("msg", "해당 계정은 탈퇴한 계정입니다.");
            model.addAttribute("url", "/logout");
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

    @PostMapping("/role")
    public String out1(Principal principal, Model model, Role role){
        User user = userService.getId(principal.getName());
        user.setRole(role);
        userService.userUpdate(user);
        return "redirect:/";
    }

    // Faq

    @GetMapping("/faq")
    public String Faq(Model model) {
        List<Faq> faqList = faqService.faqList();
        model.addAttribute("faqList", faqList);
        return "/board/faq";
    }

    @GetMapping("/faqadd")
    public String FaqForm(Model model) {
        return "/board/faqadd";
    }

    @PostMapping("/faqadd")
    public String FaqInsert(Faq faq){
        faqService.faqInsert(faq);
        return "redirect:/faq";
    }


    // Notice

    @GetMapping("/notice")
    public String notice(Model model, Principal principal) {
        List<Notice> noticeList = noticeService.NoticeList();
        model.addAttribute("noticeList", noticeList);
        return "/board/notice";
    }

    @GetMapping("/noticeGet")
    public String noticeGet(Model model, Long no) {
        Notice notice = noticeService.NoticeGet(no);
        model.addAttribute("notice", notice);
        return "/board/noticeGet";
    }


    @GetMapping("/noticeadd")
    public String noticeForm(Model model, Principal principal) {
        model.addAttribute("boardDTO", new BoardDTO());
        model.addAttribute("principal", principal.getName());
        return "/board/noticeadd";
    }

    @PostMapping("/noticeadd")
    public String noticeInsert(BoardDTO boardDTO){
        Notice notice = Notice.create(boardDTO);
        noticeService.NoticeInsert(notice);
        return "redirect:/notice";
    }

    @GetMapping("/noticeEdit")
    public String noticeEditForm(Model model, Notice notice) {
        model.addAttribute("notice", notice);
        return "/board/noticeEdit";
    }

    @PostMapping("noticeEdit")
    public String noticeEdit(BoardDTO boardDTO){
        Notice notice = Notice.create(boardDTO);
        noticeService.NoticeInsert(notice);
        return "redirect:/notice";
    }

    @GetMapping("/noticeDelete")
    public String noticeDelete(Model model, Long no) {
        noticeService.NoticeDelete(no);
        return "redirect:/notice";
    }
}
