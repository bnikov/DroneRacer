package com.example.droneracer.repository;

import com.example.droneracer.Model.Pilot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PilotRepository extends JpaRepository<Pilot, Integer> {
    Optional<Pilot> findByEmailAndPassword(String email, String password);

    Optional<Pilot> findByUsernameAndPassword(String username, String password);

    Optional<Pilot> findByUsername(String username);

    Optional<Pilot> findByEmail(String email);

    Optional<Pilot> findByResetPasswordToken(String token);
}
