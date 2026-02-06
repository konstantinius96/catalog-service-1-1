package ru.samsebemehanik.catalog.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.samsebemehanik.catalog.dto.FavouriteCreateRequest;
import ru.samsebemehanik.catalog.dto.FavouriteDto;
import ru.samsebemehanik.catalog.service.FavouriteService;

import java.util.List;

@RestController
@RequestMapping("/favourites")
public class FavouriteController {

    private final FavouriteService favouriteService;

    public FavouriteController(FavouriteService favouriteService) {
        this.favouriteService = favouriteService;
    }

    @PostMapping
    public FavouriteDto addFavourite(@Valid @RequestBody FavouriteCreateRequest request) {
        return favouriteService.addFavourite(request);
    }

    @GetMapping("/users/{userId}")
    public List<FavouriteDto> getUserFavourites(@PathVariable Long userId) {
        return favouriteService.getUserFavourites(userId);
    }
}
