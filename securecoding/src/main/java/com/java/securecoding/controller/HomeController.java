package com.java.securecoding.controller;

import com.java.securecoding.domain.form.MemberForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String main() {
        return "index";
    }

    @GetMapping(value = {"/member/login", "/2/16"})
    public String LoginForm(Model model) {

        model.addAttribute("memberForm", new MemberForm());

        return "/2/2.16";
    }

    @PostMapping(value = {"/2/16/vuln"})
    public String LoginForm_vuln() {
        return "/2/2.16";
    }

    @PostMapping(value = {"/member/login", "/2/16/secure"})
    public String LoginForm_secure() {
        return "/2/2.16";
    }
}
