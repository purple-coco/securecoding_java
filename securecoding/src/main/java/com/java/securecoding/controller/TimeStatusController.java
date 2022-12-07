package com.java.securecoding.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class TimeStatusController {

    @GetMapping("/3/1")
    public String RaceConditonForm() {
        return "/3/3.1";
    }

    @GetMapping("/3/1/code")
    public String RaceConditonForm_code() {
        return "/3/3.1.code";
    }

    @GetMapping("/3/2")
    public String RecursiveFuntionForm() {
        return "/3/3.2";
    }
}
