package com.example.droneracer.web;

import com.example.droneracer.Service.RaceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final RaceService raceService;

    public HomeController(RaceService raceService) {
        this.raceService = raceService;
    }

    @GetMapping()
    public String showHomePage(Model model) {
        model.addAttribute("races", raceService.listAll());
        model.addAttribute("bodyContent", "race_menu");
        return "master-template";
    }
}
