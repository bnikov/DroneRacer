package com.example.droneracer.web;

import com.example.droneracer.Service.OrganizerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class OrganizerController {
    private final OrganizerService organizerService;

    public OrganizerController(OrganizerService organizerService) {
        this.organizerService = organizerService;
    }

    @GetMapping("/organizers")
    public String getOrganizers(Model model){
        model.addAttribute("bodyContent", "organizers");
        model.addAttribute("organizers", organizerService.findAll());
        return "master-template";
    }

    @GetMapping("/editOrganizer/{id}")
    public String editOrganizer(@PathVariable Integer id, Model model){

        if (organizerService.findById(id).isEmpty()){
            return "redirect:/organizers?error=OrganizerNotFound";
        }

        model.addAttribute("organizer", organizerService.findById(id).get());
        model.addAttribute("bodyContent", "editOrganizer");
        return "master-template";
    }

    @PostMapping("/editOrganizer/{id}")
    public String editOrganizer(@PathVariable Integer id,
                                @RequestParam String organizer_name,
                                @RequestParam String url){
        organizerService.update(id, organizer_name, url);
        return "redirect:/organizers";
    }

    @PostMapping("/updateOrganizerPhoto/{id}")
    public String updatePhoto(@PathVariable Integer id,
                              @RequestParam MultipartFile photo) throws IOException {
        organizerService.updatePhoto(id, photo);
        return "redirect:/organizers";
    }
}
