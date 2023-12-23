package com.itdajman.tuneheavenanalytics.service;

import com.itdajman.tuneheavenanalytics.model.TrendingSongReport;
import com.itdajman.tuneheavenanalytics.model.TrendingSongReportProjection;
import com.itdajman.tuneheavenanalytics.repository.SongRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SongTrendReportingServiceTest {

    @InjectMocks
    private SongTrendReportingService songTrendReportingService;

    @Mock
    private CsvDataImportService csvDataImportService;

    @Mock
    private SongRepository songRepository;

    @Test
    public void testGenerateTrending100SongsReport() {
        when(songRepository.generateTrending100SongsReport()).thenReturn(getTrendingSongReportProjections());

        songTrendReportingService.generateTrending100SongsReport();

        verify(csvDataImportService).saveReportToCsv(eq("trending100songs-"), eq(getTrendingSongReports()));
    }

    @Test
    public void testGenerateSongsLosingReport() {
        when(songRepository.findSongsWithRatingDecrease()).thenReturn(getTrendingSongReportProjections());

        songTrendReportingService.generateSongsLosingReport();

        verify(csvDataImportService).saveReportToCsv(eq("songs-loosing-"), eq(getTrendingSongReports()));
    }

    private static ArrayList<TrendingSongReport> getTrendingSongReports() {
        ArrayList<TrendingSongReport> trendingSongReports = new ArrayList<>();
        trendingSongReports.add(TrendingSongReport.fromProjection(getProjection1()));
        trendingSongReports.add(TrendingSongReport.fromProjection(getProjection2()));
        return trendingSongReports;
    }

    private static List<TrendingSongReportProjection> getTrendingSongReportProjections() {
        List<TrendingSongReportProjection> trendingSongReportsList = new ArrayList<>();
        trendingSongReportsList.add(getProjection1());
        trendingSongReportsList.add(getProjection2());
        return trendingSongReportsList;
    }

    private static TrendingSongReportProjection getProjection2() {
        return new TrendingSongReportProjection() {
            @Override
            public String getSongName() {
                return "Song 2";
            }
            @Override
            public String getSongUuid() {
                return "UUID2";
            }
            @Override
            public Double getRatingThisMonth() {
                return 3.0;
            }
            @Override
            public Double getRatingPreviousMonth() {
                return 3.5;
            }
            @Override
            public Double getRating2MonthsBack() {
                return 3.0;
            }
        };
    }

    private static TrendingSongReportProjection getProjection1() {
        return new TrendingSongReportProjection() {
            @Override
            public String getSongName() {
                return "Song 1";
            }
            @Override
            public String getSongUuid() {
                return "UUID1";
            }
            @Override
            public Double getRatingThisMonth() {
                return 4.5;
            }
            @Override
            public Double getRatingPreviousMonth() {
                return 4.0;
            }
            @Override
            public Double getRating2MonthsBack() {
                return 3.5;
            }
        };
    }
}
