package com.example.droneracer.Service;

import com.example.droneracer.Model.Drone;
import com.example.droneracer.Model.Pilot;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface DroneService {

    Optional<Drone> findById(Integer id);

    Optional<Drone> findByPilot(Pilot pilot);

    Optional<Drone> add(Drone drone);

    void update(Drone drone, String motors, String esc, String frame, String flightController, String battery);

    void deleteByPilot(Pilot pilot);
}
