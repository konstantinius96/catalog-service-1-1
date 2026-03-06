package ru.samsebemehanik.catalog.controller;

import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.samsebemehanik.catalog.dto.ComponentCreateRequest;
import ru.samsebemehanik.catalog.dto.ComponentCreateResponse;
import ru.samsebemehanik.catalog.dto.ComponentEditRequest;
import ru.samsebemehanik.catalog.dto.ComponentEditResponse;
import ru.samsebemehanik.catalog.dto.AutoComponentDto;
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

    @PutMapping("/{id}")
    public ComponentEditResponse edit(@PathVariable UUID id, @Valid @RequestBody ComponentEditRequest request) {
        return autoComponentService.edit(id, request);
    }

    @GetMapping("/{id}")
    public AutoComponentDto getById(@PathVariable UUID id) {
        return autoComponentService.getById(id);
    }
}
