package com.naicker.we.attend.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
    @RequestMapping(value = "/home")
    public String home() {
       return "home";
    }

    @GetMapping(value = "/alter")
    public String renderUpdateStudentForm(){
        return "alterStudent";
    }
    
}
