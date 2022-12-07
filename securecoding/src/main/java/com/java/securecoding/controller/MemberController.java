package com.java.securecoding.controller;


import com.java.securecoding.domain.form.MemberForm;
import com.java.securecoding.domain.member.Member;
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

    @GetMapping(value = {"/members/new", "/2/5"})
    public String CreateMemberForm(Model model) {

        model.addAttribute("memberForm", new MemberForm());

        return "/2/2.5";
    }

    @PostMapping(value = {"/2/5/vuln"})
    public String CreateMemberForm(@Valid MemberForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("message", "유효하지 않은 정보입니다.");
            model.addAttribute("searchUrl", "/");

            return "message";
        }

        Member member = new Member();

        member.setName(form.getName());
        member.setUsername(form.getUsername());
        member.setPassword(form.getPassword());


        return "redirect:/";
    }
}
