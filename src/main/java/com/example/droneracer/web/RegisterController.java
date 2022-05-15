package com.example.droneracer.web;

import com.example.droneracer.Model.Enumerations.AuthenticationType;
import com.example.droneracer.Model.Enumerations.Role;
import com.example.droneracer.Service.PilotService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class RegisterController {

    private final PilotService pilotService;

    public RegisterController(PilotService pilotService) {
        this.pilotService = pilotService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("bodyContent", "register");
        return "master-template";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String repeatedPassword,
                           @RequestParam String first_name,
                           @RequestParam String last_name,
                           @RequestParam String email) {
        if (pilotService.findByEmail(email).isPresent()){
            return "redirect:/register?error=AccountWithEmailAlreadyExists";
        }
        if (pilotService.findByUsername(username).isPresent()){
            return "redirect:/register?error=AccountWithUsernameAlreadyExists";
        }
        if (pilotService.listAll().size() == 0){
            pilotService.register(username, first_name, last_name, email, password, repeatedPassword, LocalDate.now(), Role.ROLE_ADMIN);
        }else{
            pilotService.register(username, first_name, last_name, email, password, repeatedPassword, LocalDate.now(), Role.ROLE_USER);
        }
        return "redirect:/login";
    }
}
