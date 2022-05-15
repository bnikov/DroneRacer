package com.example.droneracer.repository;

import com.example.droneracer.Model.Pilot;
import com.example.droneracer.Model.PilotImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PilotImageRepository extends JpaRepository<PilotImage, Integer> {
    List<PilotImage> findAllByPilot(Pilot p);

    List<PilotImage> findAllByPilotId(Integer id);

    Optional<PilotImage> findById(Integer id);

    void delete(PilotImage pilotImage);

    void deleteAllByPilotId(Integer id);
}
