package com.example.droneracer.repository;

import com.example.droneracer.Model.Participant;
import com.example.droneracer.Model.Pilot;
import com.example.droneracer.Model.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    List<Participant> findAllByPilot(Pilot p);

    List<Participant> findAllByRace(Race r);

    List<Participant> findAllByPilotId(Integer id);

    List<Participant> findAllByRaceAndPilot(Race r, Pilot p);

    void deleteByRaceAndPilot(Race r, Pilot p);

    void deleteAllByPilotId(Integer id);

    void deleteAllByRaceId(Integer id);

}
