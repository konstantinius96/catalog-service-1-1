package ru.samsebemehanik.catalog.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.samsebemehanik.catalog.domain.favourite.Favourite;
import ru.samsebemehanik.catalog.dto.FavouriteCreateRequest;
import ru.samsebemehanik.catalog.dto.FavouriteDto;
import ru.samsebemehanik.catalog.mapper.FavouriteMapper;
import ru.samsebemehanik.catalog.repository.AutoComponentRepository;
import ru.samsebemehanik.catalog.repository.FavouriteRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavouriteServiceImpl implements FavouriteService {

    private final FavouriteRepository favouriteRepository;
    private final AutoComponentRepository autoComponentRepository;

    public FavouriteServiceImpl(FavouriteRepository favouriteRepository,
                                AutoComponentRepository autoComponentRepository) {
        this.favouriteRepository = favouriteRepository;
        this.autoComponentRepository = autoComponentRepository;
    }

    @Override
    public FavouriteDto addFavourite(FavouriteCreateRequest request) {
        var component = autoComponentRepository.findById(request.getAutoComponentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Компонент не найден"));

        var favourite = new Favourite(request.getUserId(), component, LocalDateTime.now());
        var saved = favouriteRepository.save(favourite);
        return FavouriteMapper.toDto(saved);
    }

    @Override
    public List<FavouriteDto> getUserFavourites(Long userId) {
        return favouriteRepository.findAllByUserId(userId).stream()
                .map(FavouriteMapper::toDto)
                .toList();
    }
}
