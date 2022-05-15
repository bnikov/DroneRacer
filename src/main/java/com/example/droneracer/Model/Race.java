package com.example.droneracer.Model;

import lombok.Data;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Base64;

@Entity
@Table(name = "races", schema = "public")
@Data
public class Race {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "race_id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "track_photo")
    private byte[] trackPhoto;

    @Column(name = "race_description", length = 100)
    private String raceDescription;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organizer_id", nullable = false)
    private Organizer organizer;


    public Race() {
    }

    public Race(String raceDescription, LocalDate startDate, LocalDate endDate, City city, Organizer organizer, byte[] trackPhoto) {
        this.trackPhoto = trackPhoto;
        this.raceDescription = raceDescription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.city = city;
        this.organizer = organizer;
    }

    public Race(String raceDescription, LocalDate startDate, LocalDate endDate, City city, Organizer organizer) {
        this.raceDescription = raceDescription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.city = city;
        this.organizer = organizer;
    }

    public String getBase64() throws UnsupportedEncodingException {
        return new String(Base64.getEncoder().encode(trackPhoto), "UTF-8");
    }
}