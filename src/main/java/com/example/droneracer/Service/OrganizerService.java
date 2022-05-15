package com.example.droneracer.Service;

import com.example.droneracer.Model.Organizer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface OrganizerService {

    List<Organizer> findAll();

    Optional<Organizer> findById(Integer id);

    Optional<Organizer> save(String organizer_name, String url, MultipartFile photo) throws IOException;

    void update(Integer id, String organizer_name, String url);

    void updatePhoto(Integer id, MultipartFile photo) throws IOException;

    void deleteById(Integer id);
}
