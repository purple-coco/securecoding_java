package com.java.securecoding.controller;

import exception.NotJoinException;
import exception.PermissionException;
import exception.UpdateInfoException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(NotJoinException.class)
    public String redirectJoin(Model model) {
        model.addAttribute("message", "중복된 아이디입니다.");
        model.addAttribute("searchUrl", "/member/signup");

        return "message";
    }

    @ExceptionHandler(PermissionException.class)
    public String NotPermission(Model model) {
        model.addAttribute("message", "권한이 없습니다.");
        model.addAttribute("searchUrl", "/");

        return "message";
    }

    @ExceptionHandler(UpdateInfoException.class)
    public String UpdateMemberInfo(Model model) {
        model.addAttribute("message", "비밀번호는 최소 8자 이상, 영어 대·소문자, 숫자, 특수문자가 혼용되어야 합니다.");
        model.addAttribute("searchUrl", "/");

        return "message";
    }
}
