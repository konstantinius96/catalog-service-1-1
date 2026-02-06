package ru.samsebemehanik.catalog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.samsebemehanik.catalog.dto.AutoComponentDto;
import ru.samsebemehanik.catalog.service.AutoComponentService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/components")
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
private final AutoComponentService autoComponentService;

 
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

        @GetMapping("/components/{id}")
        public Map<String, Object> getComponentById(@PathVariable Long id) {
            return Map.of(
                    "id", id,
                    "name", "Двигатель",
                    "description", "Подробное описание компонента"
            );
            @GetMapping("/{id}")
            public AutoComponentDto getComponentById(@PathVariable Long id) {
                return autoComponentService.getById(id);
            }
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
