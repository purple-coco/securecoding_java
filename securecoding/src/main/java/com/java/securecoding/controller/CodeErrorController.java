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

    private static CommandService commandService;

    //1. Null Pointer 역참조
    @GetMapping("/5/1")
    public String NullForm(@RequestParam (required = false)String cmd) {
        return "/5/5.1";
    }

    @GetMapping("/5/1/code")
    public String NullForm_code() {
        return "/5/5.1.code";
    }


    @PostMapping("/5/1/vuln")
    public String NullForm_vuln(HttpServletRequest request, Model model) throws IOException {
        String cmd = request.getParameter("cmd");
        String result;

        try {
            result = commandService.OSCommandService(cmd);
            model.addAttribute("result", result);
        } catch (Exception e) {
            model.addAttribute("result", e.getMessage());
        }

        return "/5/5.1";
    }

    @PostMapping("/5/1/secure")
    public String NullForm_secure(HttpServletRequest request, Model model) throws IOException {

        String cmd2 = request.getParameter("cmd2");
        String result2;

        if (cmd2.equals("")) {
            model.addAttribute("result2", "명령어를 입력하세요");
        }


        List<String> allowedCommands = new ArrayList<String>();
        allowedCommands.add("cal");

        System.out.println("cmd2.matches(\"[|;&:>-]*\") = " + cmd2.matches("[|;&:>-]*"));

        if (!allowedCommands.contains(cmd2)) {
            model.addAttribute("message", "허용하지 않는 명령어입니다.");
            model.addAttribute("searchUrl", "/5/1");

            return "message";
        } else if (cmd2.matches("[|;&:>-]*")) {
            model.addAttribute("message", "허용하지 않는 명령어입니다.");
            model.addAttribute("searchUrl", "/5/1");

            return "message";
        } else {
            result2 = commandService.OSCommandService(cmd2);
            model.addAttribute("result2", result2);
        }

        return "/5/5.1";
    }

    //2. 부적절한 자원 해제
    @GetMapping("/5/2")
    public String InappropriateResourceForm() {
        return "/5/5.2";
    }

    //3. 해제된 자원 사용
    @GetMapping("/5/3")
    public String FreedResourceForm() {
        return "/5/5.3";
    }

    //4. 초기화되지 않은 변수 사용
    @GetMapping("/5/4")
    public String UninitializedVariableForm() {
        return "/5/5.4";
    }

    //5. 신뢰할 수 없는 데이터의 역직렬화
    @GetMapping("/5/5")
    public String DeserializationForm_code() {
        return "/5/5.5";
    }
}
