package com.example.droneracer.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class DefaultController {

    public DefaultController() {
    }

    @GetMapping()
    public String defaultMapping(){
        return "redirect:/home";
    }
}
