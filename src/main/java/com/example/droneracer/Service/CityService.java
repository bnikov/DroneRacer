package com.example.droneracer.Service;

import com.example.droneracer.Model.City;
import com.example.droneracer.Model.Country;

import java.util.List;
import java.util.Optional;

public interface CityService {

    List<City> findAll();

    Optional<City> findById(Integer id);

    Optional<City> save(String city_name, Country country);

    void deleteAllByCountryId(Integer id);

    void deleteById(Integer id);
}
