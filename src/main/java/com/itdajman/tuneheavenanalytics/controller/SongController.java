package com.itdajman.tuneheavenanalytics.controller;

import com.itdajman.tuneheavenanalytics.model.dto.AverageSongRatingDto;
import com.itdajman.tuneheavenanalytics.model.dto.AverageSongRatingThreeMothsDto;
import com.itdajman.tuneheavenanalytics.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/songs")
public class SongController {
    private final SongService songService;

    @GetMapping("/{songId}/avg")
    public AverageSongRatingDto getAverageRatingForSong(
            @PathVariable String songId,
            @RequestParam String since,
            @RequestParam String until) {
        return songService.getAverageRatingForSong(songId, since, until);
    }

    @GetMapping("/{songId}/avg-three-months")
    public AverageSongRatingThreeMothsDto getAverageRatingForThreeMonths(@PathVariable String songId) {
        return new AverageSongRatingThreeMothsDto(songService.getAverageRatingForThreeMonths(songId));
    }
}
