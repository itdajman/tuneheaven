package com.itdajman.tuneheavenanalytics.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String songName;
    private String songId;
    private String artistName;
    private String artistId;
    private String userId;
    private int reviewRating;
    private String genre;

    @CreationTimestamp
    @Column(name = "timestamp")
    @Temporal(TemporalType.DATE)
    private Date timestamp;
}
