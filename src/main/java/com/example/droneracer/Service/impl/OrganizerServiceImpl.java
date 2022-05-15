package com.example.droneracer.Service.impl;

import com.example.droneracer.Model.Organizer;
import com.example.droneracer.Model.exceptions.OrganizerNotFoundException;
import com.example.droneracer.Service.OrganizerService;
import com.example.droneracer.repository.OrganizerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizerServiceImpl implements OrganizerService {
    private final OrganizerRepository organizerRepository;

    public OrganizerServiceImpl(OrganizerRepository organizerRepository) {
        this.organizerRepository = organizerRepository;
    }

    @Override
    public List<Organizer> findAll() {
        return organizerRepository.findAll();
    }

    @Override
    public Optional<Organizer> findById(Integer id) {
        return organizerRepository.findById(id);
    }

    @Override
    public Optional<Organizer> save(String organizer_name, String url, MultipartFile photo) throws IOException {
        return Optional.of(organizerRepository.save(new Organizer(organizer_name, url, photo.getBytes())));
    }

    @Override
    @Transactional
    public void update(Integer id, String organizer_name, String url) {
        Organizer organizer = organizerRepository.findById(id).orElseThrow(OrganizerNotFoundException::new);
        organizer.setOrganizerName(organizer_name);
        organizer.setUrl(url);
    }

    @Override
    @Transactional
    public void updatePhoto(Integer id, MultipartFile photo) throws IOException {
        Organizer organizer = organizerRepository.findById(id).orElseThrow(OrganizerNotFoundException::new);
        if (!photo.isEmpty()){
            organizer.setPhoto(photo.getBytes());
        }
    }

    @Override
    public void deleteById(Integer id) {
        organizerRepository.deleteById(id);
    }
}
