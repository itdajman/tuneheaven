package com.itdajman.tuneheavenanalytics.service;

import com.itdajman.tuneheavenanalytics.model.Song;
import com.itdajman.tuneheavenanalytics.model.dto.AverageRatingDto;
import com.itdajman.tuneheavenanalytics.model.dto.AverageSongRatingDto;
import com.itdajman.tuneheavenanalytics.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    private final Clock clock;

    public void saveAll(List<Song> songs) {
        songRepository.saveAll(songs);
    }

    public AverageSongRatingDto getAverageRatingForSong(String songId, String dateSince, String dateUntil) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateFormat.setLenient(false);
        Date since;
        Date until;
        try {
            since = dateFormat.parse(dateSince);
            until = dateFormat.parse(dateUntil);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateSince + " or " + dateUntil + ". Expected format: yyyyMMdd");
        }
        Double averageSongRating = songRepository.findAverageRatingForSongIdAndDateRange(songId, since, until);
        return new AverageSongRatingDto(averageSongRating);
    }

    public List<AverageRatingDto> getAverageRatingForThreeMonths(String songId) {
        LocalDate currentDate = LocalDate.now(clock);

        LocalDate threeMonthsAgo = currentDate.minusMonths(3);
        LocalDate twoMonthsAgo = currentDate.minusMonths(2);
        LocalDate oneMonthAgo = currentDate.minusMonths(1);

        Double ratingThreeMonthsAgo = calculateAverageRatingForMonth(songId, threeMonthsAgo);
        Double ratingTwoMonthsAgo = calculateAverageRatingForMonth(songId, twoMonthsAgo);
        Double ratingOneMonthAgo = calculateAverageRatingForMonth(songId, oneMonthAgo);

        List<AverageRatingDto> resultList = new ArrayList<>();
        resultList.add(new AverageRatingDto(threeMonthsAgo.format(DateTimeFormatter.ofPattern("yyyyMM")), String.format("%.2f", ratingThreeMonthsAgo)));
        resultList.add(new AverageRatingDto(twoMonthsAgo.format(DateTimeFormatter.ofPattern("yyyyMM")), String.format("%.2f", ratingTwoMonthsAgo)));
        resultList.add(new AverageRatingDto(oneMonthAgo.format(DateTimeFormatter.ofPattern("yyyyMM")), String.format("%.2f", ratingOneMonthAgo)));
        return resultList;
    }

    private Double calculateAverageRatingForMonth(String songId, LocalDate month) {
        LocalDate startOfMonth = month.withDayOfMonth(1);
        LocalDate endOfMonth = month.withDayOfMonth(month.lengthOfMonth());
        return songRepository.findAverageRatingForSongIdAndDateRange(songId,
                Date.from(startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(endOfMonth.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()));
    }
}
