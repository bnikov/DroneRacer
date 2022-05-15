package com.example.droneracer.repository;

import com.example.droneracer.Model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    void deleteById(Integer id);

    void deleteAllByCountryId(Integer id);
}
