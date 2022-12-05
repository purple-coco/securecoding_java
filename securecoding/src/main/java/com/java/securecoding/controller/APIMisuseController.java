package com.java.securecoding.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.Socket;

@Slf4j
@Controller
public class APIMisuseController {

    @GetMapping("/7/1")
    public String DNSlookupForm() {
        return "/7/7.1";
    }

    @GetMapping("/7/2")
    public String VulnerableAPIForm() {
        return "/7/7.2";
    }

}


