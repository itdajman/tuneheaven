package com.itdajman.tuneheavenanalytics.repository;

import com.itdajman.tuneheavenanalytics.model.Song;
import com.itdajman.tuneheavenanalytics.model.TrendingSongReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * The following repository methods use native SQL queries to retrieve data. While native queries are powerful and flexible,
 * they may have some limitations and drawbacks like Portability, Type Safety and Maintainability.
 * TODO: For these reasons, consider using the Criteria API, which is a type-safe and database-agnostic way to build queries in Java.
 * The Criteria API allows you to dynamically construct queries based on your application's needs and provides a more
 * maintainable and portable solution for querying the database.
 */
public interface SongRepository extends JpaRepository<Song, Long> {
    @Query("SELECT COALESCE(AVG(s.reviewRating), 0) FROM Song s WHERE s.songId = :songId AND s.timestamp >= :dateSince AND s.timestamp <= :dateUntil")
    Double findAverageRatingForSongIdAndDateRange(
            @Param("songId") String songId,
            @Param("dateSince") Date dateSince,
            @Param("dateUntil") Date dateUntil
    );

    @Query(value = "SELECT" +
            "            s.song_name AS songName," +
            "            s.song_id AS songUuid," +
            "            AVG(CASE WHEN EXTRACT(MONTH FROM s.timestamp) = EXTRACT(MONTH FROM CURRENT_DATE) THEN s.review_rating ELSE 0 END) AS ratingThisMonth," +
            "            AVG(CASE WHEN EXTRACT(MONTH FROM s.timestamp) = EXTRACT(MONTH FROM CURRENT_DATE) - 1 THEN s.review_rating ELSE 0 END) AS ratingPreviousMonth," +
            "            AVG(CASE WHEN EXTRACT(MONTH FROM s.timestamp) = EXTRACT(MONTH FROM CURRENT_DATE) - 2 THEN s.review_rating ELSE 0 END) AS rating2MonthsBack," +
            "           (AVG(CASE WHEN EXTRACT(MONTH FROM s.timestamp) = EXTRACT(MONTH FROM CURRENT_DATE) THEN s.review_rating ELSE 0 END) - " +
            "AVG(CASE WHEN EXTRACT(MONTH FROM s.timestamp) = EXTRACT(MONTH FROM CURRENT_DATE) - 1 THEN s.review_rating ELSE 0 END)) as difference" +
            "            FROM Song s" +
            "            GROUP BY s.song_Name, s.song_id" +
            "            ORDER BY difference DESC" +
            "            LIMIT 100", nativeQuery = true)
    List<TrendingSongReportProjection> generateTrending100SongsReport();

    @Query(value = "SELECT " +
            "s.song_name AS songName, " +
            "s.song_id AS songUuid, " +
            "AVG(CASE WHEN EXTRACT(MONTH FROM s.timestamp) = EXTRACT(MONTH FROM CURRENT_DATE) THEN s.review_rating ELSE 0 END) AS ratingThisMonth, " +
            "AVG(CASE WHEN EXTRACT(MONTH FROM s.timestamp) = EXTRACT(MONTH FROM CURRENT_DATE) - 1 THEN s.review_rating ELSE 0 END) AS ratingPreviousMonth, " +
            "AVG(CASE WHEN EXTRACT(MONTH FROM s.timestamp) = EXTRACT(MONTH FROM CURRENT_DATE) - 2 THEN s.review_rating ELSE 0 END) AS rating2MonthsBack " +
            "FROM Song s " +
            "GROUP BY s.song_Name, s.song_id " +
            "HAVING (ratingPreviousMonth - ratingThisMonth) >= 0.4", nativeQuery = true)
    List<TrendingSongReportProjection> findSongsWithRatingDecrease();
}
