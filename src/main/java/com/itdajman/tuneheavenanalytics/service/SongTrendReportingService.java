package com.itdajman.tuneheavenanalytics.service;

import com.itdajman.tuneheavenanalytics.model.TrendingSongReport;
import com.itdajman.tuneheavenanalytics.model.TrendingSongReportProjection;
import com.itdajman.tuneheavenanalytics.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongTrendReportingService {
    private static final String TRENDING_FILE_NAME_PREFIX = "trending100songs-";
    private static final String LOOSING_FILE_NAME_PREFIX = "songs-loosing-";
    private final CsvDataImportService csvDataImportService;
    private final SongRepository songRepository;

    public void generateTrending100SongsReport() {
        List<TrendingSongReport> trendingSongReports = getTrendingSongReports();
        csvDataImportService.saveReportToCsv(TRENDING_FILE_NAME_PREFIX, trendingSongReports);
    }

    private List<TrendingSongReport> getTrendingSongReports() {
        List<TrendingSongReportProjection> trendingSongReportsList =
                songRepository.generateTrending100SongsReport();
        return getTrendingSongReports(trendingSongReportsList);
    }

    public void generateSongsLosingReport() {
        List<TrendingSongReport> trendingSongReports = getLosingSongReports();
        csvDataImportService.saveReportToCsv(LOOSING_FILE_NAME_PREFIX, trendingSongReports);
    }

    private List<TrendingSongReport> getLosingSongReports() {
        List<TrendingSongReportProjection> trendingSongReportsList =
                songRepository.findSongsWithRatingDecrease();
        return getTrendingSongReports(trendingSongReportsList);
    }

    private static List<TrendingSongReport> getTrendingSongReports(List<TrendingSongReportProjection> trendingSongReportsList) {
        return trendingSongReportsList.stream()
                .map(TrendingSongReport::fromProjection)
                .collect(Collectors.toList());
    }
}
