package com.pro06.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

//    @GetMapping("/error")
//    public String handleError(HttpServletRequest request, Model model) {
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//        model.addAttribute("code", status.toString());
//        model.addAttribute("msg", HttpStatus.valueOf(Integer.valueOf(status.toString())));
//        return "/error";
//    }
}
