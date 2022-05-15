package com.example.droneracer.Service;

import com.example.droneracer.Model.City;
import com.example.droneracer.Model.Organizer;
import com.example.droneracer.Model.Race;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RaceService {

    Optional<Race> findById(Integer id);

    List<Race> listAll();

    Optional<Race> save(String race_description,
                         LocalDate start_date,
                         LocalDate end_date,
                         City city,
                         Organizer organizer,
                         byte[] track_photo);

    Optional<Race> update(Integer id,
                String race_description,
                LocalDate start_date,
                LocalDate end_date,
                City city,
                Organizer organizer);

    Optional<Race> updatePhoto(Integer id, byte[] trackPhoto);

    void deleteById(Integer id);
}
