package com.java.securecoding.controller;

import exception.NotJoinException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NotJoinException.class)
    public String redirectJoin(Model model) {
        model.addAttribute("message", "중복된 아이디입니다.");
        model.addAttribute("searchUrl", "/member/signup");

        return "message";
    }
}
