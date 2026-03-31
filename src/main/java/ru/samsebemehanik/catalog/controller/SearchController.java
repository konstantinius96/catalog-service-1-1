package ru.samsebemehanik.catalog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.samsebemehanik.catalog.dto.SearchResponse;
import ru.samsebemehanik.catalog.service.AutoComponentService;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    private final AutoComponentService autoComponentService;

    public SearchController(AutoComponentService autoComponentService) {
        this.autoComponentService = autoComponentService;
    }

    @GetMapping
    public SearchResponse search(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        return autoComponentService.searchByName(query, limit, offset);
    }
}
