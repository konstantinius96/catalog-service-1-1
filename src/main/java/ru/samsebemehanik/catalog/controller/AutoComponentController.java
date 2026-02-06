package ru.samsebemehanik.catalog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class AutoComponentController {

    @GetMapping("/components")
    public List<Map<String, Object>> getAllComponents() {
        return List.of(
                Map.of(
                        "id", 1L,
                        "name", "Двигатель",
                        "description", "Основной силовой агрегат"
                ),
                Map.of(
                        "id", 2L,
                        "name", "Подвеска",
                        "description", "Система амортизации"
                )
        );
    }

    @GetMapping("/components/{id}")
    public Map<String, Object> getComponentById(@PathVariable Long id) {
        return Map.of(
                "id", id,
                "name", "Двигатель",
                "description", "Подробное описание компонента"
        );
    }
}
