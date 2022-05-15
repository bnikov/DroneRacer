package com.example.droneracer.Service.impl;

import com.example.droneracer.Model.Participant;
import com.example.droneracer.Model.Pilot;
import com.example.droneracer.Model.Race;
import com.example.droneracer.Service.ParticipantService;
import com.example.droneracer.repository.ParticipantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantRepository participantRepository;

    public ParticipantServiceImpl(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Override
    public List<Participant> findAll() {
        return participantRepository.findAll();
    }

    @Override
    public List<Participant> findAllByPilot(Pilot pilot) {
        return participantRepository.findAllByPilot(pilot);
    }

    @Override
    public List<Participant> findAllByRace(Race race) {
        return participantRepository.findAllByRace(race);
    }

    @Override
    public List<Participant> findAllByRaceAndPilot(Race race, Pilot pilot) {
        return participantRepository.findAllByRaceAndPilot(race, pilot);
    }

    @Override
    public Optional<Participant> register(Race race, Pilot pilot) {
        return Optional.of(participantRepository.save(new Participant(pilot, race)));
    }

    @Override
    @Transactional
    public void withdraw(Race race, Pilot pilot) {
        participantRepository.deleteByRaceAndPilot(race, pilot);
    }

    @Override
    @Transactional
    public void deleteAllByPilotId(Integer id) {
        if (!participantRepository.findAllByPilotId(id).isEmpty()){
            participantRepository.deleteAllByPilotId(id);
        }
    }

    @Override
    @Transactional
    public void deleteAllByRaceId(Integer id) {
        participantRepository.deleteAllByRaceId(id);
    }
}
