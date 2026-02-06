package ru.samsebemehanik.catalog.mapper;

import ru.samsebemehanik.catalog.domain.favourite.Favourite;
import ru.samsebemehanik.catalog.dto.FavouriteDto;

public class FavouriteMapper {

    public static FavouriteDto toDto(Favourite favourite) {
        return new FavouriteDto(
                favourite.getId(),
                favourite.getUserId(),
                AutoComponentMapper.toDto(favourite.getAutoComponent()),
                favourite.getAddedAt(),
                favourite.getVersion()
        );
    }
}
