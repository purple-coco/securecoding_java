package com.java.securecoding.controller;

import com.java.securecoding.domain.form.MemberForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String main() {
        return "index";
    }

    @GetMapping(value = {"/member/login"})
    public String LoginForm(Model model) {

        model.addAttribute("memberForm", new MemberForm());

        return "/2/2.5";
    }
}
