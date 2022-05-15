package com.example.droneracer.Service;

import com.example.droneracer.Model.Pilot;
import com.example.droneracer.Model.PilotVideo;

import java.util.List;
import java.util.Optional;

public interface PilotVideoService {

    List<PilotVideo> findAllByPilot(Pilot p);

    Optional<PilotVideo> save(Pilot p, String video_link);

    Optional<PilotVideo> findById(Integer id);

    void deleteById(Integer id);

    void deleteAllByPilotId(Integer id);
}
