package com.cactus.desert.desertbackend.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author EvanLuo42
 * @date 4/29/22 9:30 AM
 */
@Entity
@Table(name = "song")
@Getter
@Setter
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "song_id", nullable = false)
    private Integer songId;

    @Column(name = "song_name", nullable = false)
    private String songName;

    @Column(name = "song_artist", nullable = false)
    private String songArtist;

    @Column(name = "score_author", nullable = false)
    private String scoreAuthor;

    @Column(name = "song_file", nullable = false)
    private String songFile;

    @Column(name = "song_cover_file", nullable = false)
    private String songCoverFile;
}
