package com.example.droneracer.web;

import com.example.droneracer.Model.City;
import com.example.droneracer.Model.Enumerations.Role;
import com.example.droneracer.Model.Organizer;
import com.example.droneracer.Model.Pilot;
import com.example.droneracer.Model.exceptions.CityNotFoundException;
import com.example.droneracer.Model.exceptions.CountryNotFoundException;
import com.example.droneracer.Model.exceptions.OrganizerNotFoundException;
import com.example.droneracer.Service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {   //TODO: add photo and video moderation from admin
    RaceService raceService;     //TODO: implement give admin permissions tab DONE
    PilotService pilotService;
    OrganizerService organizerService;
    CityService cityService;
    CountryService countryService;
    ParticipantService participantService;

    public AdminController(RaceService raceService,
                           PilotService pilotService,
                           OrganizerService organizerService,
                           CityService cityService,
                           CountryService countryService,
                           ParticipantService participantService) {
        this.raceService = raceService;
        this.pilotService = pilotService;
        this.organizerService = organizerService;
        this.cityService = cityService;
        this.countryService = countryService;
        this.participantService = participantService;
    }

    @GetMapping
    public String getAdminPage(Model model) {
        model.addAttribute("bodyContent", "admin");     //TODO: Fix this abomination SEMI-DONE
        return "master-template";
    }

    @GetMapping("/addRace")
    public String addRacePage(Model model) {
        model.addAttribute("bodyContent", "addRace");
        model.addAttribute("organizers", organizerService.findAll());
        model.addAttribute("cities", cityService.findAll());
        return "master-template";
    }

    @PostMapping("/addRace")
    public String addRace(@RequestParam String race_description,
                          @RequestParam String start_date,
                          @RequestParam String end_date,
                          @RequestParam Integer city_id,
                          @RequestParam Integer organizer_id,
                          @RequestPart MultipartFile track_photo) throws IOException {
        City city = cityService.
                findById(city_id).
                orElseThrow(CityNotFoundException::new);

        Organizer organizer = organizerService.
                findById(organizer_id).
                orElseThrow(OrganizerNotFoundException::new);

        byte[] track_photo_bytes = track_photo == null ? null : track_photo.getBytes();
        raceService.save(
                race_description,
                LocalDate.parse(start_date),
                LocalDate.parse(end_date),
                city,
                organizer,
                track_photo_bytes
        );
        return "redirect:/admin";
    }

    @GetMapping("/deleteRace")
    public String deleteRace(Model model) {
        model.addAttribute("races", raceService.listAll());
        model.addAttribute("bodyContent", "deleteRace");
        return "master-template";
    }

    @PostMapping("/deleteRace/{id}")
    public String deleteRace(@PathVariable Integer id) {
        if (raceService.findById(id).isPresent()) {
            participantService.deleteAllByRaceId(id);
            raceService.deleteById(id);
        }
        return "redirect:/home";
    }

    @GetMapping("/addCity")
    public String addCity(Model model) {
        model.addAttribute("countries", countryService.findAll());
        model.addAttribute("bodyContent", "addCity");
        return "master-template";
    }

    @PostMapping("/addCity")
    public String addCity(@RequestParam String city_name,
                          @RequestParam Integer country_id) {

        cityService.save(city_name, countryService.findById(country_id).orElseThrow(CountryNotFoundException::new));
        return "redirect:/admin";
    }

    @GetMapping("/editRace")
    public String editRace(Model model) {
        model.addAttribute("races", raceService.listAll());
        model.addAttribute("bodyContent", "editRaceMenu");
        return "master-template";
    }

    @GetMapping("/editRace/{id}")
    public String editRace(Model model, @PathVariable Integer id) {
        if (raceService.findById(id).isEmpty()){
            return "redirect:/home?error=RaceNotFound";
        }

        model.addAttribute("race", raceService.findById(id).get());
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("organizers", organizerService.findAll());
        model.addAttribute("bodyContent", "editRace");
        return "master-template";
    }

    @PostMapping("/editRace/{id}")
    public String editRace(@PathVariable Integer id,
                           @RequestParam String race_description,
                           @RequestParam String start_date,
                           @RequestParam String end_date,
                           @RequestParam Integer city_id,
                           @RequestParam Integer organizer_id) {

        LocalDate startDate = LocalDate.parse(start_date);
        LocalDate endDate = LocalDate.parse(end_date);
        City city = cityService.findById(city_id).orElseThrow(CityNotFoundException::new);
        Organizer organizer = organizerService.findById(organizer_id).orElseThrow(OrganizerNotFoundException::new);
        raceService.update(id, race_description, startDate, endDate, city, organizer);
        return "redirect:/admin/editRace";
    }

    @PostMapping("/changeRacePhoto/{id}")
    public String changePhoto(@PathVariable Integer id,
                              @RequestPart MultipartFile track_photo) throws IOException {

        if (track_photo == null) {
            return "redirect:/editRace?error=InvalidPhoto";
        }

        byte[] track_photo_bytes = track_photo.getBytes();
        raceService.updatePhoto(id, track_photo_bytes);
        return "redirect:/admin/editRace";
    }

    @GetMapping("/addCountry")
    public String addCountry(Model model) {
        model.addAttribute("bodyContent", "addCountry");
         return "master-template";
    }

    @PostMapping("/addCountry")
    public String addCountry(@RequestParam String country_name) {
        countryService.save(country_name);
        return "redirect:/admin";
    }

    @GetMapping("/deleteCountry")
    public String deleteCountry(Model model){
        model.addAttribute("countries", countryService.findAll());
        model.addAttribute("bodyContent", "deleteCountry");
        return "master-template";
    }

    @PostMapping("/deleteCountry/{id}")
    public String deleteCountry(@PathVariable Integer id){
        if (countryService.findById(id).isEmpty()){
            return "redirect:/home";
        }
        cityService.deleteAllByCountryId(id);                //TODO: handle races when country is deleted
        countryService.deleteById(id);
        return "redirect:/admin/deleteCountry";
    }

    @GetMapping("/deleteCity")
    public String deleteCity(Model model){
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("bodyContent", "deleteCity");
        return "master-template";
    }

    @PostMapping("/deleteCity/{id}")
    public String deleteCity(@PathVariable Integer id){
        if (cityService.findById(id).isEmpty()){
            return "redirect:/home";
        }
        cityService.deleteById(id);
        return "redirect:/admin/deleteCity";
    }


    @GetMapping("/addOrganizer")
    public String addOrganizer(Model model) {
        model.addAttribute("bodyContent", "addOrganizer");
        return "master-template";
    }

    @PostMapping("/addOrganizer")
    public String addOrganizer(@RequestParam String organizer_name,
                               @RequestParam String url,
                               @RequestParam MultipartFile photo) throws IOException {
        if (photo.getBytes().length == 0) {
            return "redirect:/addOrganizer?error=InvalidPhoto";
        }
        organizerService.save(organizer_name, url, photo);
        return "redirect:/organizers";
    }

    @GetMapping("/deleteOrganizer")
    public String deleteOrganizer(Model model) {
        model.addAttribute("bodyContent", "deleteOrganizer");
        model.addAttribute("organizers", organizerService.findAll());
        return "master-template";
    }


    @PostMapping("/deleteOrganizer/{id}")
    public String deleteOrganizer(@PathVariable Integer id) {
        if (organizerService.findById(id).isEmpty()){
            return "redirect:/admin/deleteOrganizer?error=OrganizerNotFound";
        }
        organizerService.deleteById(id);                    //TODO: handle races when deleting organizer
        return "redirect:/admin/deleteOrganizer";
    }

    @GetMapping("/giveAdmin")
    public String getAdmin(Model model, HttpServletRequest http){
        List<Pilot> pilots = pilotService.listAll()
                .stream()
                .filter(p -> !p.getUsername().equals(http.getRemoteUser()))
                .collect(Collectors.toList());

        model.addAttribute("pilots", pilots);
        model.addAttribute("bodyContent", "giveAdmin");
        return "master-template";
    }

    @PostMapping("/giveAdmin/{id}")
    public String giveAdmin(@PathVariable Integer id){
        if (pilotService.findById(id).isEmpty()){
            return "redirect:/admin/giveAdmin?error=UserNotFound";
        }
        pilotService.setRole(id, Role.ROLE_ADMIN);
        return "redirect:/admin/giveAdmin";
    }

    @PostMapping("revokeAdmin/{id}")
    public String revokeAdmin(@PathVariable Integer id){
        if (pilotService.findById(id).isEmpty()){
            return "redirect:/admin/giveAdmin?error=UserNotFound";
        }
        pilotService.setRole(id, Role.ROLE_USER);
        return "redirect:/admin/giveAdmin";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(Model model, HttpServletRequest http){
        List<Pilot> pilots = pilotService.listAll()
                .stream()
                .filter(p -> !p.getUsername().equals(http.getRemoteUser()))
                .collect(Collectors.toList());
        model.addAttribute("pilots", pilots);
        model.addAttribute("bodyContent", "deleteUser");
        return "master-template";
    }

    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Integer id){
        if (pilotService.findById(id).isPresent()){
            pilotService.deleteById(id);                  //TODO: Handle user deletion impact on other tables DONE
            return "redirect:/admin/deleteUser";          //TODO: handle self deletion DONE
        }
        return "redirect:/home";
    }

}
