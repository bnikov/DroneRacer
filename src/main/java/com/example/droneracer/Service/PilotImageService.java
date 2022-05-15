package com.example.droneracer.Service;

import com.example.droneracer.Model.Pilot;
import com.example.droneracer.Model.PilotImage;

import java.util.List;
import java.util.Optional;

public interface PilotImageService {

    List<PilotImage> findAllByPilot(Pilot p);

    List<PilotImage> findAllNonProfilePictures(Pilot p);

    Optional<PilotImage> save(PilotImage pilotImage);

    Optional<PilotImage> findProfilePicture(Pilot p);

    Optional<PilotImage> findById(Integer id);

    void removeImage(PilotImage pilotImage);

    void deleteAllByPilotId(Integer id);
}
