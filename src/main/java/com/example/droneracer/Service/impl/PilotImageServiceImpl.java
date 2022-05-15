package com.example.droneracer.Service.impl;

import com.example.droneracer.Model.Pilot;
import com.example.droneracer.Model.PilotImage;
import com.example.droneracer.Service.PilotImageService;
import com.example.droneracer.repository.PilotImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PilotImageServiceImpl implements PilotImageService {

    private final PilotImageRepository pilotImageRepository;

    public PilotImageServiceImpl(PilotImageRepository pilotImageRepository) {
        this.pilotImageRepository = pilotImageRepository;
    }

    @Override
    public List<PilotImage> findAllByPilot(Pilot p) {
        return pilotImageRepository.findAllByPilot(p);
    }

    @Override
    @Transactional
    public void removeImage(PilotImage pilotImage) {
        pilotImageRepository.delete(pilotImage);
    }

    @Override
    public Optional<PilotImage> save(PilotImage pilotImage) {
        return Optional.of(pilotImageRepository.save(pilotImage));
    }

    @Override
    public List<PilotImage> findAllNonProfilePictures(Pilot pilot) {
        return pilotImageRepository.findAllByPilot(pilot).
                stream()
                .filter(i -> !i.getIsProfilePicture()).collect(Collectors.toList());
    }

    @Override
    public Optional<PilotImage> findProfilePicture(Pilot pilot) {
        return pilotImageRepository.findAllByPilot(pilot)
                .stream().filter(p -> p.getIsProfilePicture()).findAny();

    }

    @Override
    public Optional<PilotImage> findById(Integer id) {
        return pilotImageRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteAllByPilotId(Integer id) {
        if (!pilotImageRepository.findAllByPilotId(id).isEmpty()){
            pilotImageRepository.deleteAllByPilotId(id);
        }
    }
}
