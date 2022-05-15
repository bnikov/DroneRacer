package com.example.droneracer.repository;

import com.example.droneracer.Model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    Country findCountryByCountryName(String countryName);

    void deleteById(Integer id);

}
