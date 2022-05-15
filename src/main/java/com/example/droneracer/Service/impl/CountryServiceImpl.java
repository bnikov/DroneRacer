package com.example.droneracer.Service.impl;

import com.example.droneracer.Model.Country;
import com.example.droneracer.Service.CountryService;
import com.example.droneracer.repository.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> findAll() {
        return countryRepository.findAll();
    }

    @Override
    public Optional<Country> findById(Integer id) {
        return countryRepository.findById(id);
    }

    @Override
    public Optional<Country> save(String country_name) {
        return Optional.of(countryRepository.save(new Country(country_name)));
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        countryRepository.deleteById(id);
    }
}
