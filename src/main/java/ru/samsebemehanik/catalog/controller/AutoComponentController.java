package ru.samsebemehanik.catalog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.samsebemehanik.catalog.dto.AutoComponentDto;
import ru.samsebemehanik.catalog.service.AutoComponentService;

import java.util.List;

@RestController
@RequestMapping("/components")
public class AutoComponentController {

    private final AutoComponentService autoComponentService;

    public AutoComponentController(AutoComponentService autoComponentService) {
        this.autoComponentService = autoComponentService;
    }

    @GetMapping
    public List<AutoComponentDto> getAllComponents() {
        return autoComponentService.getAll();
    }

    @GetMapping("/{id}")
    public AutoComponentDto getComponentById(@PathVariable Long id) {
        return autoComponentService.getById(id);
    }
}