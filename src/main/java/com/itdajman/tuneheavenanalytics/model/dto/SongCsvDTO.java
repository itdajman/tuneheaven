package com.itdajman.tuneheavenanalytics.model.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SongCsvDTO {
    @CsvBindByName(column = "song_name")
    private String songName;

    @CsvBindByName(column = "song_id")
    private String songId;

    @CsvBindByName(column = "artist_name")
    private String artistName;

    @CsvBindByName(column = "artist_id")
    private String artistId;

    @CsvBindByName(column = "user_id")
    private String userId;

    @CsvBindByName(column = "review_rating")
    private int reviewRating;

    @CsvBindByName(column = "genre")
    private String genre;
}
