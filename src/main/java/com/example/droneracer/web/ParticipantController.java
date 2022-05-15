package com.example.droneracer.web;

import com.example.droneracer.Model.Pilot;
import com.example.droneracer.Model.Race;
import com.example.droneracer.Service.ParticipantService;
import com.example.droneracer.Service.PilotService;
import com.example.droneracer.Service.RaceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ParticipantController {
    ParticipantService participantService;
    PilotService pilotService;
    RaceService raceService;

    public ParticipantController(ParticipantService participantService, PilotService pilotService, RaceService raceService) {
        this.participantService = participantService;
        this.pilotService = pilotService;
        this.raceService = raceService;
    }

    @PostMapping("/raceRegister")
    public String participate(@RequestParam Integer raceId, @RequestParam String pilotUsername){
        if (raceService.findById(raceId).isPresent() && pilotService.findByUsername(pilotUsername).isPresent()){
            Race race = raceService.findById(raceId).get();
            Pilot pilot = pilotService.findByUsername(pilotUsername).get();
            participantService.register(race, pilot);
            return "redirect:/race/" + raceId;
        }
        return "redirect:/races";
    }

    @PostMapping("/raceWithdraw")
    public String withdraw(@RequestParam Integer raceId, @RequestParam String pilotUsername){
        if(raceService.findById(raceId).isPresent() && pilotService.findByUsername(pilotUsername).isPresent()){
            Race race = raceService.findById(raceId).get();
            Pilot pilot = pilotService.findByUsername(pilotUsername).get();
            participantService.withdraw(race, pilot);
            return "redirect:/race/" + raceId;
        }
        return "redirect:/races";
    }

}
