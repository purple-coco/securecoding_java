package com.java.securecoding.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class EncapsulationController {

    @GetMapping("/6/1")
    public String DataExposureForm() {
        return "/6/6.1";
    }

    @GetMapping("/6/2")
    public String DebugForm() {
        return "/6/6.2";
    }

    @GetMapping("/6/3")
    public String PublicForm() {
        return "/6/6.3";
    }

    @GetMapping("/6/4")
    public String PrivateForm() {
        return "/6/6.4";
    }
}
