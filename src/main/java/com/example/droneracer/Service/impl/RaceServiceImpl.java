package com.example.droneracer.Service.impl;

import com.example.droneracer.Model.City;
import com.example.droneracer.Model.Organizer;
import com.example.droneracer.Model.Race;
import com.example.droneracer.Model.exceptions.RaceNotFoundException;
import com.example.droneracer.Service.RaceService;
import com.example.droneracer.repository.RaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RaceServiceImpl implements RaceService {
    private final RaceRepository raceRepository;

    public RaceServiceImpl(RaceRepository raceRepository) {
        this.raceRepository = raceRepository;
    }

    @Override
    public Optional<Race> findById(Integer id) {
        return raceRepository.findById(id);
    }

    @Override
    public List<Race> listAll() {
        return raceRepository.findAll();
    }

    @Override
    public Optional<Race> save(String race_description, LocalDate start_date, LocalDate end_date, City city, Organizer organizer, byte[] track_photo) {
       if (track_photo == null || track_photo.length == 0){
           return Optional.of(raceRepository.save(new Race(race_description, start_date, end_date, city, organizer)));
       }
        return Optional.of(raceRepository.save(new Race(race_description, start_date, end_date, city, organizer, track_photo)));
    }

    @Override
    public Optional<Race> update(Integer id, String race_description, LocalDate start_date, LocalDate end_date, City city, Organizer organizer) {
        Race race = raceRepository.findById(id).orElseThrow(RaceNotFoundException::new);
        race.setRaceDescription(race_description);
        race.setStartDate(start_date);
        race.setEndDate(end_date);
        race.setCity(city);
        race.setOrganizer(organizer);

        return Optional.of(raceRepository.save(race));
    }

    @Override
    @Transactional
    public Optional<Race> updatePhoto(Integer id, byte[] trackPhoto) {
        Race race = raceRepository.findById(id).orElseThrow(RaceNotFoundException::new);
        race.setTrackPhoto(trackPhoto);
        return Optional.of(race);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        if(raceRepository.findById(id).isPresent()){
            raceRepository.deleteById(id);
        }
    }
}
