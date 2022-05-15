package com.example.droneracer.Service;

import com.example.droneracer.Model.Country;

import java.util.List;
import java.util.Optional;

public interface CountryService {

    List<Country> findAll();

    Optional<Country> findById(Integer id);

    Optional<Country> save(String country_name);

    void deleteById(Integer id);
}
