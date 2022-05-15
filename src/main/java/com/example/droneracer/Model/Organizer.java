package com.example.droneracer.Model;

import lombok.Data;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Entity
@Table(name = "organizers", schema = "public")
@Data
public class Organizer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organizer_id", nullable = false)
    private Integer id;

    @Column(name = "organizer_name", nullable = false, length = 100)
    private String organizerName;

    @Column(name = "url", nullable = false, length = 100)
    private String url;

    @Column(name = "photo")
    private byte[] photo;

    public Organizer() {
    }

    public Organizer(String organizerName, String url, byte[] photo) {
        this.organizerName = organizerName;
        this.url = url;
        this.photo = photo;
    }

    public String getBase64() throws UnsupportedEncodingException {
        return new String(Base64.getEncoder().encode(photo), "UTF-8");
    }
}