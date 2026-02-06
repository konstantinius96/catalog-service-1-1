package ru.samsebemehanik.catalog.service;

import java.util.List;
import java.util.Map;

public interface AutoComponentService {

    List<Map<String, Object>> getAll();

    Map<String, Object> getById(Long id);
}
