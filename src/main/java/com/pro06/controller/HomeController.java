package com.pro06.controller;

import com.pro06.dto.BoardDTO;
import com.pro06.dto.UserDTO;
import com.pro06.entity.*;
import com.pro06.service.BoardService;
import com.pro06.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Controller
//@RequestMapping("/")
public class HomeController {


    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BoardService boardService;


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
    public String active(Model model, HttpServletRequest request, HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
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
            model.addAttribute("msg", "환영합니다! 로그인되었습니다 \uD83E\uDD13");
            model.addAttribute("url", "/");
            return "/alert";
        } else if (pass == 2) {
            model.addAttribute("msg", "해당 계정은 휴면계정입니다. 휴면을 풀어주세요.");
            model.addAttribute("url", "/active");
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


    @GetMapping("/join")
    public String JoinForm(Model model){
        return "/user/join";
    }

    @PostMapping("/joinPro")
    public String Join(Model model, UserDTO userDTO){
        userService.userInsert(userDTO);
        return "redirect:/";
    }

    @PostMapping("idCheckPro")
    public ResponseEntity idCheck(@RequestBody UserDTO userDTO) throws Exception {
        String id = userDTO.getId();
        boolean result = userService.idCheck(id);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping("/myPage")
    public String Exindex(Model model, Principal principal){
        UserDTO userDTO = userService.getId(principal.getName());
        model.addAttribute("user", userDTO);
        return "/user/myPage";
    }

    @GetMapping("/remove")
    public String remove(String id, Model model){
        UserDTO userDTO = userService.getId(id);
        userDTO.setStatus(Status.OUT);
        userService.userUpdate(userDTO);
        model.addAttribute("msg", "지금까지 감사합니다.");
        model.addAttribute("url", "/logout");
        return "/alert";
    }



    // Faq

    @GetMapping("/faq")
    public String Faq(Model model) {
        List<BoardDTO> faqList = boardService.faqList();
        model.addAttribute("faqList", faqList);
        return "/board/faq";
    }

    @GetMapping("/faqadd")
    public String FaqForm(Model model) {
        model.addAttribute("boarddto", new BoardDTO());
        return "/board/faqadd";
    }

    @PostMapping("/faqadd")
    public String FaqInsert(BoardDTO boardDTO){
        boardService.faqInsert(boardDTO);
        return "redirect:/faq";
    }




    // Notice

    @GetMapping("/notice")
    public String notice(Model model) {
        List<BoardDTO> noticeList = boardService.NoticeList();
        model.addAttribute("noticeList", noticeList);
        return "/board/notice";
    }

    @GetMapping("/noticeGet")
    public String noticeGet(Model model, Integer no) {
        BoardDTO notice = boardService.NoticeGet(no);
        model.addAttribute("notice", notice);
        return "/board/noticeGet";
    }


    @GetMapping("/noticeadd")
    public String noticeForm(Model model, Principal principal) {
        model.addAttribute("boardDTO", new BoardDTO());
        model.addAttribute("prin", principal.getName());
        return "/board/noticeadd";
    }

    @PostMapping("/noticeadd")
    public String noticeInsert(BoardDTO boardDTO){
        boardService.NoticeInsert(boardDTO);
        return "redirect:/notice";
    }

    @GetMapping("/noticeEdit")
    public String noticeEditForm(Model model, Integer no) {
        BoardDTO boardDTO = boardService.NoticeGet(no);
        model.addAttribute("notice", boardDTO);
        return "/board/noticeEdit";
    }

    @PostMapping("noticeEdit")
    public String noticeEdit(BoardDTO boardDTO){
        Integer no = boardDTO.getNo();
        boardService.NoticeUpdate(boardDTO);
        return "redirect:/noticeGet?no="+no;
    }

    @GetMapping("/noticeDelete")
    public String noticeDelete(Model model, Integer no) {
        boardService.NoticeDelete(no);
        return "redirect:/notice";
    }

}
