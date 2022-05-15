package com.example.droneracer.web;

import com.example.droneracer.Model.Pilot;
import com.example.droneracer.Model.exceptions.InvalidUserCredentialsException;
import com.example.droneracer.Service.PilotService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final PilotService pilotService;

    public LoginController(PilotService pilotService) {
        this.pilotService = pilotService;
    }

    @GetMapping
    public String getLoginPage(Model model) {
        model.addAttribute("bodyContent", "login");
        return "master-template";
    }

    @PostMapping
    public String login(HttpServletRequest request, Model model) {
        Pilot pilot;

        try {
            pilot = pilotService.login(request.getParameter("username"), request.getParameter("password")).orElseThrow(InvalidUserCredentialsException::new);
            request.getSession().setAttribute("user", pilot);
            return "redirect:/home";
        } catch (InvalidUserCredentialsException exception) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", exception.getMessage());
            model.addAttribute("bodyContent", "login");
            return "master-template";
        }
    }
}



