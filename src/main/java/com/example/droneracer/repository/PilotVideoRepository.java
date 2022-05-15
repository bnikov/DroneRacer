package com.example.droneracer.repository;

import com.example.droneracer.Model.Pilot;
import com.example.droneracer.Model.PilotVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PilotVideoRepository extends JpaRepository<PilotVideo, Integer> {
    List<PilotVideo> findAllByPilot(Pilot p);

    List<PilotVideo> findAllByPilotId(Integer id);

    void deleteById(Integer id);

    void deleteAllByPilotId(Integer id);
}
