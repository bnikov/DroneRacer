package com.example.droneracer.repository;

import com.example.droneracer.Model.Drone;
import com.example.droneracer.Model.Pilot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Integer> {
    void deleteByPilot(Pilot pilot);

    Optional<Drone> findByPilot(Pilot pilot);

}
