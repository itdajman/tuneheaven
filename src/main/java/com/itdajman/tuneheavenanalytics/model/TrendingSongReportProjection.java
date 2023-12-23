package com.itdajman.tuneheavenanalytics.model;

public interface TrendingSongReportProjection {
    String getSongName();

    String getSongUuid();

    Double getRatingThisMonth();

    Double getRatingPreviousMonth();

    Double getRating2MonthsBack();
}
