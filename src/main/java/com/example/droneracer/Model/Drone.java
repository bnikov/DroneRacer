package com.example.droneracer.Model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "drones", schema = "public")
@Data
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drone_id", nullable = false)
    private Integer id;

    @Column(name = "motors", length = 100)
    private String motors;

    @Column(name = "esc", length = 100)
    private String esc;

    @Column(name = "frame", length = 100)
    private String frame;

    @Column(name = "flight_controller", length = 100)
    private String flightController;

    @Column(name = "battery", length = 100)
    private String battery;

    @OneToOne(optional = false)
    @JoinColumn(name = "drone_pilot_id", nullable = false)
    private Pilot pilot;

}