package com.pro06.controller;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error/*")
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @GetMapping("/")
    public String handleError(HttpServletRequest request, Model model) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("code", "404");
                model.addAttribute("msg", "해당 주소를 찾을 수 없습니다.");
                return "/error";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("code", "403");
                model.addAttribute("msg", "허용되지 않는 메소드입니다.");
                return "/error";
            } else if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                model.addAttribute("code", "405");
                model.addAttribute("msg", "해당 원인을 찾을 수 없습니다.");
                return "/error";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("code", "500");
                model.addAttribute("msg", "해당 원인을 찾을 수 없습니다.");
                return "/error";
            } else {
                model.addAttribute("code", "이유를 알 수 없습니다.");
                return "/error";
            }
        }
        model.addAttribute("code", "이유를 알 수 없습니다.");
        return "/error";
    }

}

