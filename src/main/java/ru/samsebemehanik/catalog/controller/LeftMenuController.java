package ru.samsebemehanik.catalog.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.samsebemehanik.catalog.dto.MenuResponse;
import ru.samsebemehanik.catalog.service.AutoComponentService;

@RestController
@RequestMapping("/api/v1/leftMenu")
public class LeftMenuController {

    private final AutoComponentService autoComponentService;

    public LeftMenuController(AutoComponentService autoComponentService) {
        this.autoComponentService = autoComponentService;
    }

    @GetMapping
    public List<MenuResponse> getAll() {
        return autoComponentService.getLeftMenu();
    }
}
