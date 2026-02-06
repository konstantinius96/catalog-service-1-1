package ru.samsebemehanik.catalog.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AutoComponentServiceImpl implements AutoComponentService {

    @Override
    public List<Map<String, Object>> getAll() {
        return List.of(
                Map.of("id", 1, "name", "Двигатель"),
                Map.of("id", 2, "name", "Подвеска")
        );
    }

    @Override
    public Map<String, Object> getById(Long id) {
        return Map.of("id", id, "name", "Двигатель");
    }
}
