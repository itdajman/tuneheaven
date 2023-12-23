package com.itdajman.tuneheavenanalytics.service;

import com.itdajman.tuneheavenanalytics.exception.ReportException;
import com.itdajman.tuneheavenanalytics.mapper.SongCsvMapper;
import com.itdajman.tuneheavenanalytics.model.Song;
import com.itdajman.tuneheavenanalytics.model.TrendingSongReport;
import com.itdajman.tuneheavenanalytics.model.dto.SongCsvDTO;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CsvDataImportService {

    @Value("${csv.file.prefix-name}")
    private String csvFileNamePrefix;

    @Value("${csv.file.path}")
    private String csvFilePath;

    private final SongCsvMapper songCsvMapper;

    private final SongService songService;

    private final Clock clock;

    @Transactional
    public void importSongCsvData() {
        try (Reader reader = new FileReader(getFileName(csvFileNamePrefix));
             CSVReader csvReader = new CSVReader(reader)) {
            CsvToBean<SongCsvDTO> csvToBean = new CsvToBeanBuilder<SongCsvDTO>(csvReader)
                    .withType(SongCsvDTO.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<SongCsvDTO> csvDtos = csvToBean.parse();
            List<Song> songs = csvDtos.stream()
                    .map(songCsvMapper::toSong)
                    .collect(Collectors.toList());
            songService.saveAll(songs);
        } catch (Exception e) {
            log.error("An error occurred while importing CSV data: " + e.getMessage(), e);
            throw new RuntimeException("Failed to import CSV data. Please check the CSV file.", e);
        }
    }

    private String getFileName(String csvFileNamePrefix1) {
        LocalDate currentDate = LocalDate.now(clock);
        return csvFilePath + csvFileNamePrefix1 + currentDate + ".csv";
    }

    void saveReportToCsv(String csvFileNamePrefix, List<TrendingSongReport> reportEntries) {
        String fileName = getFileName(csvFileNamePrefix);
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("song_name,song_uuid,rating_this_month,rating_previous_month,rating_2months_back");
            for (TrendingSongReport reportEntry : reportEntries) {
                writer.println(reportEntry.toCsvString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReportException("Failed to save report to CSV file: " + fileName, e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}