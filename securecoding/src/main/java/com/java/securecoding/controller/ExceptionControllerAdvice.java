package com.java.securecoding.controller;

import exception.NotAllowExtException;
import exception.NotJoinException;
import exception.PermissionException;
import exception.UpdateInfoException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;

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
        model.addAttribute("message", "잘못된 접근입니다.");
        model.addAttribute("searchUrl", "/");

        return "message";
    }

    @ExceptionHandler(UpdateInfoException.class)
    public String UpdateMemberInfo(@PathVariable Long memberId, Model model) {
        model.addAttribute("message", "비밀번호는 최소 8자 이상, 영어 대·소문자, 숫자, 특수문자가 혼용되어야 합니다.");
        model.addAttribute("searchUrl", "/mypage/info/" + memberId);

        return "message";
    }

    @ExceptionHandler(NotAllowExtException.class)
    public String NotAllowExt(Model model) {
        model.addAttribute("message", "허용되지 않는 파일 확장자입니다.");
        model.addAttribute("searchUrl", "/board/new");

        return "message";
    }
}
