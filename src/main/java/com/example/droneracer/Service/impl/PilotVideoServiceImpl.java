package com.example.droneracer.Service.impl;

import com.example.droneracer.Model.Pilot;
import com.example.droneracer.Model.PilotVideo;
import com.example.droneracer.Service.PilotVideoService;
import com.example.droneracer.repository.PilotVideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PilotVideoServiceImpl implements PilotVideoService {
    private final PilotVideoRepository pilotVideoRepository;

    public PilotVideoServiceImpl(PilotVideoRepository pilotVideoRepository) {
        this.pilotVideoRepository = pilotVideoRepository;
    }

    @Override
    public List<PilotVideo> findAllByPilot(Pilot p) {
        return pilotVideoRepository.findAllByPilot(p);
    }

    @Override
    public Optional<PilotVideo> save(Pilot pilot, String video_link) {
        return Optional.of(pilotVideoRepository.save(new PilotVideo(pilot, video_link)));
    }

    @Override
    public Optional<PilotVideo> findById(Integer id) {
        return pilotVideoRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteAllByPilotId(Integer id) {
        if (!pilotVideoRepository.findAllByPilotId(id).isEmpty()){
            pilotVideoRepository.deleteAllByPilotId(id);
        }
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        pilotVideoRepository.deleteById(id);
    }
}
