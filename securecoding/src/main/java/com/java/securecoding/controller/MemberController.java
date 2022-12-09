package com.java.securecoding.controller;


import com.java.securecoding.domain.form.MemberForm;
import com.java.securecoding.domain.form.MemberForm2;
import com.java.securecoding.domain.member.Member;
import com.java.securecoding.service.CommandService;
import com.java.securecoding.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController{

    private final MemberService memberService;

    @GetMapping(value = {"/member/signup", "/2/5", "/2/5/vuln", "/2/5/secure"})
    public String CreateMemberForm(Model model) {

        model.addAttribute("memberForm", new MemberForm());

        return "/2/2.5";
    }

    @PostMapping(value = {"/2/5/vuln"})
    public String CreateMemberForm_vuln(@Valid MemberForm form, BindingResult result, Model model) {

        log.info("bindingresult = {}", result);

        if (result.hasErrors()) {
            model.addAttribute("message", "유효하지 않은 정보입니다.");
            model.addAttribute("searchUrl", "/2/5");

            return "message";
        }

        Member member = new Member();

        member.setName(form.getName());
        member.setUsername(form.getUsername());

        log.info("{}", form.getName());
        log.info("{}", form.getUsername());
        log.info("{}", form.getPassword());

        if(!memberService.passwordValidate(form.getPassword())) {

            model.addAttribute("message", "최소 8자 이상, 영어 대·소문자, 숫자, 특수문자가 혼용되어야 합니다.");
            model.addAttribute("searchUrl", "/member/signup");

            return "message";
        } else {
            member.setPassword(form.getPassword());

            memberService.join(member);

            model.addAttribute("message", "회원가입이 완료되었습니다.");
            model.addAttribute("searchUrl", "/member/login");

            return "message";
        }
    }

    @PostMapping(value = {"/2/5/secure"})
    public String CreateMemberForm_secure(@Valid MemberForm2 form, BindingResult result, Model model) {

        if (result.hasErrors()) {
            log.info("bindingresult = {}", result);
            model.addAttribute("message", "유효하지 않은 정보입니다.");
            model.addAttribute("searchUrl", "/2/5");

            return "message";
        }

        Member member = new Member();

        member.setName(form.getName());
        member.setUsername(form.getUsername());

        log.info("{}", form.getName());
        log.info("{}", form.getUsername());
        log.info("{}", form.getPassword());

        System.out.println("member = " + member);

        if(memberService.passwordValidate(form.getPassword())) {

            member.setPassword(memberService.HashPassword(form.getPassword()));
            memberService.join(member);

            model.addAttribute("message", "회원가입이 완료되었습니다.");
            model.addAttribute("searchUrl", "/member/login");

            return "message";
        } else {
            model.addAttribute("message", "최소 8자 이상, 영어 대·소문자, 숫자, 특수문자가 혼용되어야 합니다.");
            model.addAttribute("searchUrl", "/member/signup");

            return "message";
        }
    }

}
