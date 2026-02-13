package ru.samsebemehanik.catalog.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.samsebemehanik.catalog.dto.ComponentCreateRequest;
import ru.samsebemehanik.catalog.dto.ComponentCreateResponse;
import ru.samsebemehanik.catalog.service.AutoComponentService;

@RestController
@RequestMapping("/api/v1/autocomponents")
public class AutoComponentController {

    private final AutoComponentService autoComponentService;

    public AutoComponentController(AutoComponentService autoComponentService) {
        this.autoComponentService = autoComponentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ComponentCreateResponse create(@Valid @RequestBody ComponentCreateRequest request) {
        return autoComponentService.create(request);
    }
}
