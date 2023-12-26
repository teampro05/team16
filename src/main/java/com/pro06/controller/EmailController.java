package com.pro06.controller;

import com.pro06.dto.email.EmailMessage;
import com.pro06.dto.email.EmailPostDto;
import com.pro06.dto.email.EmailResponseDto;
import com.pro06.service.EmailService;
import groovy.util.logging.Slf4j;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@lombok.extern.slf4j.Slf4j
@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;


    // 이메일 테스트
    @PostMapping("/sendTest")
    public ResponseEntity sendTest(@RequestBody Map<String, String > map) {
        EmailMessage emailMessage = EmailMessage.builder()
                .to(map.get("email"))
                .subject("Word Master 회원 가입을 위한 이메일 인증코드 발송")
                .build();
        String code = emailService.sendMail(emailMessage, "email");
        EmailResponseDto emailResponseDto = new EmailResponseDto();
        emailResponseDto.setCode(code);
        return ResponseEntity.ok(emailResponseDto);
    }

    @PostMapping("/otoSend")
    public void otoSend(@RequestBody Map<String, String > map, Model model) {
        EmailMessage emailMessage = EmailMessage.builder()
                .to(map.get("email"))
                .ototitle(map.get("ototitle"))
                .otocontent(map.get("otocontent"))
                .subject("Word Master 문의사항을 보내드립니다.")
                .subtitle(map.get("subtitle"))
                .message(map.get("message"))
                .build();
        emailService.otosendMail(emailMessage, "email2");
    }

//    @PostMapping("/insubTest")
//    public ResponseEntity insubTest(@RequestBody String insub) {
//        EmailMessage emailMessage = EmailMessage.builder()
//                .to(email)
//                .subject("테스트 메일 입니다.")
//                .message("1234 입니다요")
//                .build();
//        emailService.sendMailTest(emailMessage);
//        return ResponseEntity.ok().build();
//    }

    // 임시 비밀번호 발급
    @PostMapping("/password")
    public ResponseEntity sendPasswordMail(@RequestBody EmailPostDto emailPostDto) {
        EmailMessage emailMessage = EmailMessage.builder()
                .to("sendjin1@naver.com")
//                .to(emailPostDto.getEmail())
                .subject("임시 비밀번호 발급")
                .message("1234 입니다요")
                .build();
        emailService.sendMail(emailMessage, "password");
        return ResponseEntity.ok().build();
    }

    // 회원가입 이메일 인증 - 요청 시 body로 인증번호 반환하도록 작성하였음
    @PostMapping("/email")
    public ResponseEntity sendJoinMail(@RequestBody EmailPostDto emailPostDto) {
        EmailMessage emailMessage = EmailMessage.builder()
                .to("sendjin4@gmail.com")
//                .to(emailPostDto.getEmail())
                .subject("이메일 인증을 위한 인증 코드 발송")
                .build();
        String code = emailService.sendMail(emailMessage, "email");
        EmailResponseDto emailResponseDto = new EmailResponseDto();
        emailResponseDto.setCode(code);
        return ResponseEntity.ok(emailResponseDto);
    }


    @PostMapping("/mail/confirm")
    public String mailConfirm(Model model) {
        String code = emailService.createKey();
        model.addAttribute("code", code);
        return "/mail";
    }

    @PostMapping("/mail/confirm2")
    @ResponseBody
    public String mailConfirm2(String email, Model model) throws MessagingException, UnsupportedEncodingException {
        MimeMessage msg = emailService.createMessageHtml(email);
        model.addAttribute("msg", msg);
        return "/mail2";
    }

    @PostMapping("/mail/verifyCode")
    @ResponseBody
    public int verifyCode(@RequestParam("code") String code) {
        int result = 0;
        System.out.println("code : "+code);
        System.out.println("code match : "+ emailService.ePw.equals(code));
        if(emailService.ePw.equals(code)) {
            result =1;
        }
        return result;
    }
}
