package com.itdajman.tuneheavenanalytics.mapper;

import com.itdajman.tuneheavenanalytics.model.Song;
import com.itdajman.tuneheavenanalytics.model.dto.SongCsvDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SongCsvMapper {

    @Mapping(target = "id", ignore = true)
    Song toSong(SongCsvDTO songCsvDTO);
}
