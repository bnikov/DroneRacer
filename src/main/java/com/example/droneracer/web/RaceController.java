package com.example.droneracer.web;

import com.example.droneracer.Model.Pilot;
import com.example.droneracer.Model.Race;
import com.example.droneracer.Service.ParticipantService;
import com.example.droneracer.Service.PilotService;
import com.example.droneracer.Service.RaceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RaceController {
    private final RaceService raceService;
    private final PilotService pilotService;
    private final ParticipantService participantService;

    public RaceController(RaceService raceService, PilotService pilotService, ParticipantService participantService) {
        this.raceService = raceService;
        this.pilotService = pilotService;
        this.participantService = participantService;
    }

    @GetMapping("/race/{id}")
    public String showRace(Model model, @PathVariable Integer id, HttpServletRequest req) {
        if (raceService.findById(id).isEmpty() || pilotService.findByUsername(req.getRemoteUser()).isEmpty()){
            return "redirect:/races?error=RaceNotFound";
        }

        Race race = raceService.findById(id).get();
        Pilot pilot = pilotService.findByUsername(req.getRemoteUser()).get();
        model.addAttribute("race", race);
        model.addAttribute("bodyContent", "race");
        model.addAttribute("participants", participantService.findAllByRace(raceService.findById(id).get()));
        model.addAttribute("isRegistered", participantService.findAllByRaceAndPilot(race, pilot).size() > 0);

        return "master-template";
    }
}
