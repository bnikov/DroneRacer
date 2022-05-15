package com.example.droneracer.Model;

import lombok.Data;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Entity
@Table(name = "pilot_images", schema = "public")
@Data
public class PilotImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    private Integer id;

    @Column(name = "image_data", nullable = false)
    private byte[] imageData;

    @Column(name = "is_profilepicture", nullable = false)
    private Boolean isProfilePicture = false;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pilot_id", nullable = false)
    private Pilot pilot;

    public PilotImage() {
    }

    public PilotImage(byte[] imageData, Boolean isProfilePicture, Pilot pilot) {
        this.imageData = imageData;
        this.isProfilePicture = isProfilePicture;
        this.pilot = pilot;
    }

    public String getBase64() throws UnsupportedEncodingException {
        return new String(Base64.getEncoder().encode(imageData), "UTF-8");
    }
}