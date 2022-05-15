package com.example.droneracer.Service.impl;

import com.example.droneracer.Model.Drone;
import com.example.droneracer.Model.Pilot;
import com.example.droneracer.Service.DroneService;
import com.example.droneracer.repository.DroneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;

    public DroneServiceImpl(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    @Override
    public Optional<Drone> findByPilot(Pilot pilot) {
        return droneRepository.findByPilot(pilot);
    }

    @Override
    @Transactional
    public Optional<Drone> add(Drone drone) {
        return Optional.of(droneRepository.save(drone));
    }

    @Override
    public Optional<Drone> findById(Integer id) {
        return droneRepository.findById(id);
    }

    @Override
    @Transactional
    public void update(Drone drone, String motors, String esc, String frame, String flightController, String battery) {
        drone.setMotors(motors);
        drone.setEsc(esc);
        drone.setFrame(frame);
        drone.setFlightController(flightController);
        drone.setBattery(battery);
        droneRepository.save(drone);
    }

    @Override
    @Transactional
    public void deleteByPilot(Pilot pilot) {
        droneRepository.deleteByPilot(pilot);
    }
}
