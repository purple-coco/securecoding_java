package com.java.securecoding.controller;

import com.java.securecoding.domain.member.Member;
import com.java.securecoding.domain.session.MemberInfo;
import com.java.securecoding.exception.*;
import com.java.securecoding.service.MemberService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

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
    public String UpdateMemberInfo(Model model, HttpSession session) {

        MemberInfo memberInfo = (MemberInfo) session.getAttribute("memberInfo");
        Long memberId = memberInfo.getId();

        model.addAttribute("message", "비밀번호는 최소 8자 이상, 영어 대·소문자, 숫자, 특수문자가 혼용되어야 합니다.");
        model.addAttribute("searchUrl", "/mypage/info/"+ memberId);

        return "message";
    }

    @ExceptionHandler(NotAllowExtException1.class)
    public String NotAllowExt1(Model model) {
        model.addAttribute("message", "파일 이름에 허용하지 않는 문자열이 포함되었습니다.");
        model.addAttribute("searchUrl", "/board/new");

        return "message";
    }

    @ExceptionHandler(NotAllowExtException2.class)
    public String NotAllowExt2(Model model) {
        model.addAttribute("message", "업로드 허용되는 파일 확장자가 아닙니다.");
        model.addAttribute("searchUrl", "/board/new");

        return "message";
    }

    @ExceptionHandler(BoardException.class)
    public String UpdateBoard(Model model) {
        model.addAttribute("message", "자신이 작성한 게시물만 수정 및 삭제가 가능합니다.");
        model.addAttribute("searchUrl", "/board");

        return "message";
    }
}
