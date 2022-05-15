package com.example.droneracer.Model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "pilot_videos", schema = "public")
@Data
public class PilotVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id", nullable = false)
    private Integer id;

    @Column(name = "video_link", nullable = false, length = 100)
    private String videoLink;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pilot_id", nullable = false)
    private Pilot pilot;

    public PilotVideo() {
    }

    public PilotVideo(Pilot pilot, String videoLink) {
        this.videoLink = videoLink;
        this.pilot = pilot;
    }
}