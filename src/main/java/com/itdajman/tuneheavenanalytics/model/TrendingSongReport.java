package com.itdajman.tuneheavenanalytics.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class TrendingSongReport {
    private String songName;
    private String songUuid;
    private Double ratingThisMonth;
    private Double ratingPreviousMonth;
    private Double rating2MonthsBack;

    public String toCsvString() {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append(songName).append(",");
        csvBuilder.append(songUuid).append(",");
        csvBuilder.append(ratingThisMonth).append(",");
        csvBuilder.append(ratingPreviousMonth).append(",");
        csvBuilder.append(rating2MonthsBack);
        return csvBuilder.toString();
    }

    public static TrendingSongReport fromProjection(TrendingSongReportProjection trendingSongReportProjection) {
        return new TrendingSongReport(
                trendingSongReportProjection.getSongName(),
                trendingSongReportProjection.getSongUuid(),
                trendingSongReportProjection.getRatingThisMonth(),
                trendingSongReportProjection.getRatingPreviousMonth(),
                trendingSongReportProjection.getRating2MonthsBack()
        );
    }
}

