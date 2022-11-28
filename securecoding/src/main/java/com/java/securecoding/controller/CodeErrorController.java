package com.java.securecoding.controller;

import com.java.securecoding.service.CommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class CodeErrorController {

    @GetMapping("/5/1")
    public String NullForm(@RequestParam (required = false)String cmd) {
        return "/5/5.1";
    }

    @PostMapping("/5/vuln1")
    public String NullForm_vuln(HttpServletRequest request, Model model) throws IOException {
        String cmd = request.getParameter("cmd");
        String result;

        try {
            result = CommandService.commandService(cmd);
            model.addAttribute("result", result);
        } catch (Exception e) {
            model.addAttribute("result", e.getMessage());
        }

        return "/5/5.1";
    }

    @PostMapping("/5/secure1")
    public String NullForm_secure(HttpServletRequest request, Model model) throws IOException {

        String cmd2 = request.getParameter("cmd2");
        String result2;

        List<String> allowedCommands = new ArrayList<String>();
        allowedCommands.add("cal");

        System.out.println("cmd2.matches(\"[|;&:>-]*\") = " + cmd2.matches("[|;&:>-]*"));


        if (cmd2.equals("")) {
            model.addAttribute("result2", "명령어를 입력하세요");

        } else if (cmd2.matches("[|;&:>-]*")) {
            model.addAttribute("message", "허용하지 않는 명령어입니다.");
            model.addAttribute("searchUrl", "/5/1");

            return "message";
        } else if (!(allowedCommands.contains(cmd2))) {
            model.addAttribute("message", "허용하지 않는 명령어입니다.");
            model.addAttribute("searchUrl", "/5/1");

            return "message";
        } else {
            result2 = CommandService.commandService(cmd2);
            model.addAttribute("result2", result2);
        }

        return "/5/5.1";
    }
}
