package com.example.droneracer.Service.impl;

import com.example.droneracer.Model.Enumerations.AuthenticationType;
import com.example.droneracer.Model.Enumerations.Role;
import com.example.droneracer.Model.Pilot;
import com.example.droneracer.Model.PilotImage;
import com.example.droneracer.Model.exceptions.*;
import com.example.droneracer.Service.*;
import com.example.droneracer.repository.PilotRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PilotServiceImpl implements PilotService, UserDetailsService {

    private final PilotRepository pilotRepository;
    private final PasswordEncoder passwordEncoder;
    private final PilotImageService pilotImageService;
    private final PilotVideoService pilotVideoService;
    private final ParticipantService participantService;
    private final DroneService droneService;

    public PilotServiceImpl(PilotRepository pilotRepository,
                            PasswordEncoder passwordEncoder,
                            PilotImageService pilotImageService,
                            PilotVideoService pilotVideoService,
                            ParticipantService participantService,
                            DroneService droneService) {
        this.pilotRepository = pilotRepository;
        this.passwordEncoder = passwordEncoder;
        this.pilotImageService = pilotImageService;
        this.pilotVideoService = pilotVideoService;
        this.participantService = participantService;
        this.droneService = droneService;
    }

    @Override
    public Optional<Pilot> findById(Integer id) {
        return pilotRepository.findById(id);
    }

    @Override
    public List<Pilot> listAll() {
        return pilotRepository.findAll();
    }

    @Override
    public Optional<Pilot> findByUsername(String username) {
        return pilotRepository.findByUsername(username);
    }

    @Override
    public Optional<Pilot> findByEmail(String email) {
        return pilotRepository.findByEmail(email);
    }

    @Override
    public Optional<Pilot> login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidArgsException();
        }

        if (pilotRepository.findByUsername(username).isPresent()) {
            Pilot pilot = pilotRepository.findByUsername(username).get();
            boolean result = passwordEncoder.matches(password, pilot.getPassword());
            if (result) {
                return Optional.of(pilot);
            }
        }
        throw new InvalidUserCredentialsException();
    }


    @Override
    public Optional<Pilot> register(String username, String first_name, String last_name, String email, String password, String repeatPassword, LocalDate member_since, Role role) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidArgsException();
        }

        if (!password.equals(repeatPassword)) {
            throw new PasswordDoesNotMatchException();
        }

        if (pilotRepository.findByUsername(username).isPresent()) {
            throw new PilotNameAlreadyExitsException();
        }
        String encodedPassword = passwordEncoder.encode(password);
        Pilot pilot = new Pilot(username, first_name, last_name, email, encodedPassword, member_since, role);
        pilot.setAuthType(AuthenticationType.valueOf("DATABASE"));
        pilot.setIsAccountNonExpired(true);
        pilot.setIsAccountNonLocked(true);
        pilot.setIsCredentialsNonExpired(true);
        pilot.setIsEnabled(true);
        return Optional.of(pilotRepository.save(pilot));
    }

    @Override
    @Transactional
    public Optional<Pilot> update(Integer id, String username, String email, String first_name, String last_name, String bio) {
        Pilot pilot = pilotRepository.findById(id).orElseThrow(PilotNotFoundException::new);
        pilot.setUsername(username);
        pilot.setEmail(email);
        pilot.setFirstName(first_name);
        pilot.setLastName(last_name);
        pilot.setBio(bio);
        return Optional.of(pilot);
    }

    @Override
    @Transactional
    public Optional<Pilot> changePassword(Pilot pilot, String oldPassword, String newPassword, String repeatedPassword) {
        if (passwordEncoder.matches(oldPassword, pilot.getPassword()) && newPassword.equals(repeatedPassword)) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            pilot.setPassword(encodedPassword);
        } else {
            return Optional.empty();
        }
        return Optional.of(pilot);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return pilotRepository.findByUsername(username).orElseThrow(RuntimeException::new);
    }

    @Override
    public Optional<Pilot> updateProfilePicture(Pilot p, byte[] photo, boolean saveOld) {
        List<PilotImage> images = pilotImageService.findAllByPilot(p);
        try {
            PilotImage currentPP = images.stream().filter(PilotImage::getIsProfilePicture).findFirst().orElseThrow(NoProfilePictureException::new);

            if (!saveOld && currentPP != null) {
                pilotImageService.removeImage(currentPP);
            } else {
                assert currentPP != null;
                currentPP.setIsProfilePicture(false);
            }
        } catch (NoProfilePictureException ignored) {

        }

        pilotImageService.save(new PilotImage(photo, true, p));
        return Optional.of(p);
    }

    @Override
    public Optional<Pilot> addImage(Pilot pilot, byte[] photo) {
        pilotImageService.save(new PilotImage(photo, false, pilot));
        return Optional.of(pilot);
    }

    @Override
    public Optional<Pilot> addVideo(Pilot pilot, String video_link) {
        video_link = video_link.split("&")[0];
        if (!video_link.contains("http")) {
            if (video_link.contains("www.")) {
                video_link = "www." + video_link;
            }
            video_link = "https://" + video_link;
        }
        pilotVideoService.save(pilot, video_link);
        return Optional.of(pilot);
    }

    @Override
    public Optional<Pilot> findByResetPasswordToken(String token) {
        return pilotRepository.findByResetPasswordToken(token);
    }

    @Override
    @Transactional
    public void updateResetPasswordToken(String token, String email) {
        Pilot pilot = pilotRepository.findByEmail(email).orElseThrow(PilotNotFoundException::new);
        pilot.setResetPasswordToken(token);
        pilotRepository.save(pilot);
    }

    @Override
    @Transactional
    public void updatePassword(Pilot pilot, String newPassword) {
        pilot.setPassword(passwordEncoder.encode(newPassword));
        pilot.setResetPasswordToken(null);
        pilotRepository.save(pilot);
    }

    @Override
    @Transactional
    public void setRole(Integer id, Role role) {
        Pilot pilot = pilotRepository.findById(id).get();
        pilot.setRole(role);
        pilotRepository.save(pilot);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        participantService.deleteAllByPilotId(id);
        pilotVideoService.deleteAllByPilotId(id);
        pilotImageService.deleteAllByPilotId(id);
        droneService.deleteByPilot(pilotRepository.findById(id).get());
        pilotRepository.deleteById(id);
    }


}
