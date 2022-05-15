package com.example.droneracer.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class PasswordResetToken {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = Pilot.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "pilot_id")
    private Pilot user;

    private Date expiryDate;

    public PasswordResetToken() {
    }

    public PasswordResetToken(String token, Pilot user) {
        this.token = token;
        this.user = user;
    }
}