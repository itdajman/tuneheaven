package com.itdajman.tuneheavenanalytics.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.mockito.Mockito.*;

public class CsvDataImportSchedulerTest {

    private CsvDataImportService csvDataImportService;

    private SongTrendReportingService songTrendReportingService;

    private Clock fixedClock;

    private CsvDataImportScheduler scheduler;

    @BeforeEach
    public void setUp() {
        this.csvDataImportService = Mockito.mock(CsvDataImportService.class);
        this.songTrendReportingService = Mockito.mock(SongTrendReportingService.class);
        this.fixedClock = Mockito.mock(Clock.class);
        scheduler = new CsvDataImportScheduler(
                csvDataImportService,
                songTrendReportingService,
                fixedClock
        );
    }

    @Test
    public void testImportCsvDataScheduled() {
        //given
        LocalDate lastDayOfMonth = LocalDate.of(2023, 1, 31);
        when(fixedClock.instant()).thenReturn(lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        when(fixedClock.getZone()).thenReturn(ZoneId.systemDefault());
        doNothing().when(csvDataImportService).importSongCsvData();
        doNothing().when(songTrendReportingService).generateTrending100SongsReport();
        doNothing().when(songTrendReportingService).generateSongsLosingReport();

        //when
        scheduler.importCsvData();

        // then
        verify(csvDataImportService).importSongCsvData();
        verify(songTrendReportingService).generateTrending100SongsReport();
        verify(songTrendReportingService).generateSongsLosingReport();
    }

    @Test
    public void testImportCsvDataNotScheduled() {
        //given
        LocalDate currentDate = LocalDate.of(2023, 1, 15);
        when(fixedClock.instant()).thenReturn(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        when(fixedClock.getZone()).thenReturn(ZoneId.systemDefault());

        // when
        scheduler.importCsvData();

        // then
        verify(csvDataImportService).importSongCsvData();
        verify(songTrendReportingService, never()).generateTrending100SongsReport();
        verify(songTrendReportingService, never()).generateSongsLosingReport();
    }
}
