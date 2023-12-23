package com.itdajman.tuneheavenanalytics.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.itdajman.tuneheavenanalytics.model.dto.AverageRatingDto;
import com.itdajman.tuneheavenanalytics.repository.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class SongServiceTest {
    private SongService songService;

    private SongRepository songRepository;

    @BeforeEach
    public void setUp() {
        this.songRepository = Mockito.mock(SongRepository.class);
        Clock fixedClock = Clock.fixed(Instant.parse("2024-04-01T22:59:20Z"), ZoneId.systemDefault());
        songService = new SongService(songRepository, fixedClock);
    }

    @Test
    public void getAverageRatingForSong() throws ParseException {
        //given
        String songId = "yourSongId";
        String dateSince = "20230101";
        String dateUntil = "20231231";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date since = dateFormat.parse(dateSince);
        Date until = dateFormat.parse(dateUntil);

        //when
        when(songRepository.findAverageRatingForSongIdAndDateRange(songId, since, until)).thenReturn(4.5);
        Double averageRating = songService.getAverageRatingForSong(songId, dateSince, dateUntil).averageRating();

        //then
        verify(songRepository).findAverageRatingForSongIdAndDateRange(songId, since, until);
        assertEquals(4.5, averageRating);
    }

    @Test
    public void getAverageRatingForSongWithInvalidDateFormat() {
        String songId = "yourSongId";
        String dateSince = "2023-01-01";
        String dateUntil = "2023-12-31";

        assertThrows(IllegalArgumentException.class, () -> songService.getAverageRatingForSong(songId, dateSince, dateUntil));
    }

    @Test
    public void testGetAverageRatingForThreeMonths() {
        //given
        String songId = "123";
        List<AverageRatingDto> expectedResults = new ArrayList<>();
        expectedResults.add(new AverageRatingDto("202401", "4,25"));
        expectedResults.add(new AverageRatingDto("202402", "4,75"));
        expectedResults.add(new AverageRatingDto("202403", "4,15"));

        //when
        when(songRepository.findAverageRatingForSongIdAndDateRange(eq(songId), any(), any())).thenReturn(4.25, 4.75, 4.15);
        List<AverageRatingDto> result = songService.getAverageRatingForThreeMonths(songId);

        //then
        verify(songRepository, times(3)).findAverageRatingForSongIdAndDateRange(eq(songId), any(), any());
        assertEquals(expectedResults, result);
    }
}
