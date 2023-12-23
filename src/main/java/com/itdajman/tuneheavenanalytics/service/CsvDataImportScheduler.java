package com.itdajman.tuneheavenanalytics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CsvDataImportScheduler {
    private final CsvDataImportService csvDataImportService;
    private final SongTrendReportingService songTrendReportingService;
    private final Clock clock;

    @Scheduled(cron = "0 0 23 * * ?")
    public void importCsvData() {
        csvDataImportService.importSongCsvData();
        LocalDate currentDate = LocalDate.now(clock);
        if (currentDate.getDayOfMonth() == currentDate.lengthOfMonth()) {
            songTrendReportingService.generateTrending100SongsReport();
            songTrendReportingService.generateSongsLosingReport();
        }
    }
}
