package com.example.droneracer.Model.adapter;

import com.example.droneracer.Model.Enumerations.AuthenticationType;
import com.example.droneracer.Model.Enumerations.Role;
import com.example.droneracer.Model.Pilot;
import com.example.droneracer.repository.PilotRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class FacebookConnectionSignup implements ConnectionSignUp {

    private final PilotRepository pilotRepository;

    private final PasswordEncoder passwordEncoder;

    public FacebookConnectionSignup(PilotRepository pilotRepository, PasswordEncoder passwordEncoder) {
        this.pilotRepository = pilotRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String execute(Connection<?> connection) {
        Pilot pilot = new Pilot();
        String full_name = connection.getDisplayName();
        pilot.setFirstName(full_name.split(" ")[0]);
        pilot.setLastName(full_name.split(" ")[1]);
        pilot.setUsername(full_name);

//        Facebook facebook = (Facebook) connection.getApi();
//        String[] fields = {"email"};
//        User user = facebook.fetchObject("me", User.class, fields);
//
//        pilot.setEmail(user.getEmail());
        pilot.setEmail("facebookDroneRacerUser@yahoogmail.com");
        pilot.setMemberSince(LocalDate.now());
        pilot.setRole(Role.ROLE_USER);
        pilot.setPassword(passwordEncoder.encode("$2a$10$BV00wet0yMhljV14w6DmQeqVNpMDXwrbzZOAkkZpg5lsackwc6Jla"));
        pilot.setAuthType(AuthenticationType.FACEBOOK);
        pilot.setIsAccountNonExpired(true);
        pilot.setIsAccountNonLocked(true);
        pilot.setIsCredentialsNonExpired(true);
        pilot.setIsEnabled(true);

        if (pilotRepository.findByUsername(full_name).isEmpty()) {
            pilotRepository.save(pilot);
        }
        return pilot.getUsername();
    }
}
