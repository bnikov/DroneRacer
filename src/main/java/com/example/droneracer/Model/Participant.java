package com.example.droneracer.Model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "participants", schema = "public")
@Data
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pilot_id", nullable = false)
    private Pilot pilot;

    @ManyToOne(optional = false)
    @JoinColumn(name = "race_id", nullable = false)
    private Race race;

    public Participant() {
    }

    public Participant(Pilot pilot, Race race) {
        this.pilot = pilot;
        this.race = race;
    }
}