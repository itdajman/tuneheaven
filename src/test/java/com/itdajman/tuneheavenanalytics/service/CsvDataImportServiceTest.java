package com.itdajman.tuneheavenanalytics.service;

import com.itdajman.tuneheavenanalytics.mapper.SongCsvMapper;
import com.itdajman.tuneheavenanalytics.model.Song;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Slf4j
public class CsvDataImportServiceTest {

    private CsvDataImportService csvDataImportService;

    @Mock
    private SongService songService;

    @Captor
    private ArgumentCaptor<List<Song>> captor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Clock fixedClock = Clock.fixed(Instant.parse("2025-03-24T22:59:20Z"), ZoneId.systemDefault());
        SongCsvMapper songCsvMapper = Mappers.getMapper(SongCsvMapper.class);
        csvDataImportService = new CsvDataImportService(songCsvMapper, songService, fixedClock);
        ReflectionTestUtils.setField(csvDataImportService, "csvFilePath", "./src/test/resources/");
        ReflectionTestUtils.setField(csvDataImportService, "csvFileNamePrefix", "tuneheaven-songs-");
    }

    @Test
    public void testImportSongCsvData() {
        //given
        Song expectedSong = getExpectedSong();

        //when
        csvDataImportService.importSongCsvData();

        // then
        verify(songService, times(1)).saveAll(captor.capture());
        List<Song> capturedSongs = captor.getValue();
        assertEquals(14, capturedSongs.size());
        Song capturedSong = capturedSongs.get(0);
        assertEquals(expectedSong, capturedSong);
    }

    private static Song getExpectedSong() {
        return Song.builder().songName("Bit by the Wild Bytes")
                .songId("97e06a5a-5d6d-4676-8a2b-25c5afc01213")
                .artistName("The Bittles")
                .artistId("892c5326-26b5-4022-9104-c44480237421")
                .userId("34746d86-7aa2-43cd-bcd7-c400c2d29200")
                .reviewRating(4)
                .genre("EDM")
                .build();
    }
}