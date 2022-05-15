package com.example.droneracer.web;

import com.example.droneracer.Model.*;
import com.example.droneracer.Model.Enumerations.Role;
import com.example.droneracer.Model.exceptions.NoProfilePictureException;
import com.example.droneracer.Service.*;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class PilotController {

    private final PilotService pilotService;
    private final PilotImageService pilotImageService;
    private final PilotVideoService pilotVideoService;
    private final ParticipantService participantService;
    private final DroneService droneService;

    public PilotController(PilotService pilotService,
                           PilotImageService pilotImageService,
                           PilotVideoService pilotVideoService,
                           ParticipantService participantService,
                           DroneService droneService) {
        this.pilotService = pilotService;
        this.pilotImageService = pilotImageService;
        this.pilotVideoService = pilotVideoService;
        this.participantService = participantService;
        this.droneService = droneService;
    }

    @GetMapping("/profile/{username}")
    public String getProfile(@PathVariable String username, HttpServletRequest httpServletRequest, Model model) {

        if (pilotService.findByUsername(username).isEmpty()) {
            return "redirect:/home";
        }                                                               //TODO: remove "show all" buttons DONE

        Pilot pilot = pilotService.findByUsername(username).get();

        PilotImage profilePicture;
        try {
            profilePicture = pilotImageService.findProfilePicture(pilot).orElseThrow(NoProfilePictureException::new);
        } catch (NoProfilePictureException e) {
            profilePicture = null;
        }

        List<PilotImage> images = pilotImageService.findAllNonProfilePictures(pilot);
        List<PilotVideo> videos = pilotVideoService.findAllByPilot(pilot);
        Optional<Drone> drone = droneService.findByPilot(pilot);

        model.addAttribute("pilot", pilot);
        model.addAttribute("profilePicture", profilePicture);
        model.addAttribute("images", images);
        model.addAttribute("videos", videos);
        model.addAttribute("image_count", images.size());
        model.addAttribute("video_count", videos.size());
        model.addAttribute("race_count", participantService.findAllByPilot(pilot).size());
        model.addAttribute("showButtons", httpServletRequest.getRemoteUser().equals(username));
        model.addAttribute("drone", drone.isPresent() ? drone.get() : null);

        model.addAttribute("bodyContent", "profile");
        return "master-template";
    }

    @GetMapping("/edit/{id}")
    public String editProfile(@PathVariable Integer id, Model model) {
        if (pilotService.findById(id).isEmpty()) {
            return "redirect:/home?error=PilotNotFound";
        }
        Pilot pilot = pilotService.findById(id).get();
        model.addAttribute("pilot", pilot);
        model.addAttribute("hasDrone", droneService.findByPilot(pilotService.findById(id).get()).isPresent());
        model.addAttribute("isFacebookUser", pilot.getAuthType().toString() == "FACEBOOK");

        model.addAttribute("bodyContent", "editProfile");   //TODO: restructure frontend DONE
        return "master-template";                                                  //TODO: add link to "My Profile" NOT WORTH THE TIME
    }                                                                              //TODO: change for facebook user

    @PostMapping("/edit/{id}")
    public String updateProfile(@PathVariable Integer id,
                                @RequestParam String username,
                                @RequestParam String email,
                                @RequestParam String first_name,
                                @RequestParam String last_name,
                                @RequestParam String bio,
                                HttpServletRequest req) {
        req.setAttribute("user", pilotService.update(id, username, email, first_name, last_name, bio));
        return "redirect:/profile/" + req.getRemoteUser();
    }

    @GetMapping("/viewRaces/{id}")
    public String viewRaces(@PathVariable Integer id, HttpServletRequest httpServletRequest, Model model) {
        if (pilotService.findById(id).isEmpty()) {
            return "redirect:/home";
        }

        Pilot pilot = pilotService.findById(id).get();
        PilotImage profilePicture;
        try {
            profilePicture = pilotImageService.findProfilePicture(pilot).orElseThrow(NoProfilePictureException::new);
        } catch (NoProfilePictureException e) {
            profilePicture = null;
        }
        List<PilotImage> images = pilotImageService.findAllNonProfilePictures(pilot);
        List<PilotVideo> videos = pilotVideoService.findAllByPilot(pilot);

        model.addAttribute("pilot", pilot);
        model.addAttribute("profilePicture", profilePicture);
        model.addAttribute("image_count", images.size());
        model.addAttribute("video_count", videos.size());
        model.addAttribute("race_count", participantService.findAllByPilot(pilot).size());
        model.addAttribute("showButtons", httpServletRequest.getRemoteUser().equals(pilot.getUsername()));

        model.addAttribute("races",
                participantService.findAllByPilot(pilot).stream()
                        .map(Participant::getRace)
                        .collect(Collectors.toList())
        );

        model.addAttribute("bodyContent", "pilotRaces");
        return "master-template";
    }

    @GetMapping("/addDrone/{id}")
    public String addDrone(@PathVariable Integer id, Model model){
        if (pilotService.findById(id).isEmpty()){
            return "redirect:/home";
        }
        model.addAttribute("bodyContent", "addDrone");
        model.addAttribute("pilot", pilotService.findById(id).get());
        return "master-template";
    }

    @PostMapping("/addDrone/{id}")
    public String addDrone(@PathVariable Integer id,
                           @RequestParam String motors,
                           @RequestParam String esc,
                           @RequestParam String frame,
                           @RequestParam String flightController,          //TODO: add delete drone button  DONE
                           @RequestParam String battery){
        Drone drone = new Drone();
        drone.setMotors(motors);
        drone.setEsc(esc);
        drone.setFrame(frame);
        drone.setFlightController(flightController);
        drone.setBattery(battery);
        drone.setPilot(pilotService.findById(id).get());
        droneService.add(drone);
        return "redirect:/profile/" + pilotService.findById(id).get().getUsername();
    }

    @GetMapping("/editDrone/{id}")
    public String editDrone(@PathVariable Integer id, Model model){
        if (pilotService.findById(id).isEmpty()){
            return "redirect:/home";
        }
        Pilot pilot = pilotService.findById(id).get();
        if (droneService.findByPilot(pilot).isEmpty()){
            return "redirect:/home";
        }
        model.addAttribute("drone", droneService.findByPilot(pilot).get());
        model.addAttribute("pilot", pilot);
        model.addAttribute("bodyContent", "editDrone");
        return "master-template";
    }

    @PostMapping("/editDrone/{id}")
    public String editDrone(@PathVariable Integer id, HttpServletRequest http,
                            @RequestParam String motors,
                            @RequestParam String esc,
                            @RequestParam String frame,
                            @RequestParam String flightController,
                            @RequestParam String battery){
        droneService.update(droneService.findById(id).get(), motors, esc, frame, flightController, battery);
        return "redirect:/profile/" + http.getRemoteUser();
    }

    @PostMapping("/deleteDrone/{id}")
    public String deleteDrone(@PathVariable Integer id, HttpServletRequest http){
        if (pilotService.findById(id).isEmpty()){
            return "redirect:/home?PilotNotFound";
        }
        droneService.deleteByPilot(pilotService.findById(id).get());
        return "redirect:/profile/" + http.getRemoteUser();
    }

    @PostMapping("/profilePictureUpdate/{id}")
    public String changeProfilePicture(@PathVariable Integer id,
                                       @RequestPart MultipartFile photo,
                                       @RequestParam(required = false) String savePP) throws IOException {
        if (photo == null) {
            return "redirect:/edit/" + id;
        }

        if (pilotService.findById(id).isEmpty()) {
            return "redirect:/home";
        }

        Pilot pilot = pilotService.findById(id).get();
        byte[] data = photo.getBytes();
        pilotService.updateProfilePicture(pilot, data, savePP != null);
        return "redirect:/profile/" + pilot.getUsername();
    }

    @PostMapping("/addPhoto/{id}")
    public String addPhoto(@PathVariable Integer id,
                           @RequestPart MultipartFile photo) throws IOException {

        if (pilotService.findById(id).isEmpty()) {
            return "redirect:/home";
        }
        Pilot pilot = pilotService.findById(id).get();
        try{
            pilotService.addImage(pilot, photo.getBytes());
        } catch (FileSizeLimitExceededException e){
            return "redirect:/uploadContent/" + id + "?error=FileSizeLimitExceeded";
        }
        return "redirect:/profile/" + pilot.getUsername();
    }

    @PostMapping("/addVideo/{id}")
    public String addVideo(@PathVariable Integer id,
                           @RequestParam String video_link) {
        if (pilotService.findById(id).isEmpty()) {
            return "redirect:/home";
        }                                                           //TODO: remove params from link  DONE
        Pilot pilot = pilotService.findById(id).get();              //TODO: fix responsiveness on profile videos
        pilotService.addVideo(pilot, video_link);
        return "redirect:/profile/" + pilot.getUsername();
    }

    @GetMapping("/uploadContent/{id}")
    public String uploadContent(@PathVariable Integer id,
                                Model model) {
        if (pilotService.findById(id).isEmpty()) {
            return "redirect:/home?error=PilotNotFound";
        }
        model.addAttribute(pilotService.findById(id).get());
        model.addAttribute("bodyContent", "uploadContent");
        return "master-template";
    }

    @PostMapping("/changePassword/{id}")
    public String changePassword(@PathVariable Integer id,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String repeatedPassword) {
        if (pilotService.findById(id).isEmpty()) {
            return "redirect:/home?error=PilotNotFound";
        }

        Pilot pilot = pilotService.findById(id).get();
        if (pilotService.changePassword(pilot, oldPassword, newPassword, repeatedPassword).isEmpty()) {
            return "redirect:/edit/" + id + "?error=ErrorChangingPassword";     //TODO: add failsafe  DONE
        }                                                                     //TODO: add error message DONE
        return "redirect:/logout";
    }

    @PostMapping("/deleteAccount/{id}")
    public String deleteAccount(@PathVariable Integer id, HttpServletRequest http) {
        Optional<Pilot> loggedInPilot = pilotService.findByUsername(http.getRemoteUser());
        Optional<Pilot> pilot = pilotService.findById(id);
        if (pilot.isPresent() && loggedInPilot.isPresent() && pilot.get().getUsername().equals(loggedInPilot.get().getUsername())) {
            pilotService.deleteById(id);
            return "redirect:/logout";
        }
        return "redirect:/home";
    }

    @GetMapping("/deletePhoto/{id}")
    public String deletePhoto(Model model, @PathVariable Integer id) {
        if (pilotService.findById(id).isEmpty()) {
            return "redirect:/home";
        }
        model.addAttribute("photos", pilotImageService.findAllByPilot(pilotService.findById(id).get()));
        model.addAttribute("bodyContent", "deletePhoto");
        return "master-template";
    }

    @PostMapping("/deletePhoto/{id}")
    public String deletePhoto(@PathVariable Integer id, HttpServletRequest http) {
        Optional<PilotImage> pilotImage = pilotImageService.findById(id);
        if (pilotImageService.findById(id).isEmpty() || !pilotImage.get().getPilot().getUsername().equals(http.getRemoteUser())) {
            return "redirect:/home";
        }
        pilotImageService.removeImage(pilotImage.get());
        return "redirect:/profile/" + http.getRemoteUser();
    }

    @GetMapping("/deleteVideo/{id}")
    public String deleteVideo(@PathVariable Integer id, Model model){
        if (pilotService.findById(id).isEmpty()) {
            return "redirect:/home";
        }
        model.addAttribute("videos", pilotVideoService.findAllByPilot(pilotService.findById(id).get()));
        model.addAttribute("bodyContent", "deleteVideo");
        return "master-template";
    }

    @PostMapping("/deleteVideo/{id}")
    public String deleteVideo(@PathVariable Integer id, HttpServletRequest http) {
        Optional<PilotVideo> pilotVideo = pilotVideoService.findById(id);
        if (pilotVideoService.findById(id).isEmpty() || !pilotVideo.get().getPilot().getUsername().equals(http.getRemoteUser())) {
            return "redirect:/home";
        }
        pilotVideoService.deleteById(id);
        return "redirect:/profile/" + http.getRemoteUser();
    }

}
