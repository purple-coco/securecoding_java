package com.java.securecoding.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @GetMapping("/4/1")
    public String ErrorMessageForm() {
        return "/4/4.1";
    }
}
