package com.example.droneracer.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    @GetMapping("/about")
    public String getAboutPage(Model model) {
        model.addAttribute("bodyContent", "about");   //TODO: fix this abomination
        return "master-template";
    }
}
