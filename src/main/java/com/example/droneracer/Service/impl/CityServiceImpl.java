package com.example.droneracer.Service.impl;

import com.example.droneracer.Model.City;
import com.example.droneracer.Model.Country;
import com.example.droneracer.Service.CityService;
import com.example.droneracer.repository.CityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public List<City> findAll() {
        return cityRepository.findAll();
    }

    @Override
    public Optional<City> findById(Integer id) {
        return cityRepository.findById(id);
    }

    @Override
    public Optional<City> save(String city_name, Country country) {
        return Optional.of(cityRepository.save(new City(city_name, country)));
    }

    @Override
    @Transactional
    public void deleteAllByCountryId(Integer id) {
        cityRepository.deleteAllByCountryId(id);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        cityRepository.deleteById(id);
    }
}
