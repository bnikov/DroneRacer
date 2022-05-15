package com.example.droneracer.Service;

import com.example.droneracer.Model.Participant;
import com.example.droneracer.Model.Pilot;
import com.example.droneracer.Model.Race;

import java.util.List;
import java.util.Optional;

public interface ParticipantService {

    List<Participant> findAll();

    List<Participant> findAllByPilot(Pilot p);

    List<Participant> findAllByRace(Race r);

    Optional<Participant> register(Race r, Pilot p);

    List<Participant> findAllByRaceAndPilot(Race r, Pilot p);

    void withdraw(Race r, Pilot p);

    void deleteAllByRaceId(Integer id);

    void deleteAllByPilotId(Integer id);
}
