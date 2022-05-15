package com.example.droneracer.Service;

import com.example.droneracer.Model.Enumerations.Role;
import com.example.droneracer.Model.Pilot;
import com.example.droneracer.Model.exceptions.InvalidUserCredentialsException;
import com.example.droneracer.Model.exceptions.PasswordDoesNotMatchException;
import com.example.droneracer.Model.exceptions.PilotNameAlreadyExitsException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PilotService extends UserDetailsService {
    Optional<Pilot> findById(Integer id);

    List<Pilot> listAll();

    Optional<Pilot> findByUsername(String username);

    Optional<Pilot> findByEmail(String email);

    Optional<Pilot> login(String email, String password) throws InvalidUserCredentialsException;

    Optional<Pilot> register
            (String pilot_name, String first_name, String last_name, String email, String password, String repeatPassword, LocalDate member_since, Role role)
            throws PasswordDoesNotMatchException, PilotNameAlreadyExitsException;

    Optional<Pilot> update(Integer id, String username, String email, String first_name, String last_name, String bio);

    Optional<Pilot> changePassword(Pilot pilot, String oldPassword, String newPassword, String repeatedPassword);

    Optional<Pilot> updateProfilePicture(Pilot p, byte[] photo, boolean saveOld);

    Optional<Pilot> addImage(Pilot p, byte[] photo);

    Optional<Pilot> addVideo(Pilot pilot, String video_link);

    Optional<Pilot> findByResetPasswordToken(String token);

    void setRole(Integer id, Role role);

    void updateResetPasswordToken(String token, String email);

    void updatePassword(Pilot pilot, String newPassword);

    void deleteById(Integer id);
}
