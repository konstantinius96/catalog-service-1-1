package ru.samsebemehanik.catalog.service;

import java.util.UUID;
import ru.samsebemehanik.catalog.dto.AutoComponentPageResponse;
import ru.samsebemehanik.catalog.dto.ComponentCreateRequest;
import ru.samsebemehanik.catalog.dto.ComponentCreateResponse;
import ru.samsebemehanik.catalog.dto.ComponentEditRequest;
import ru.samsebemehanik.catalog.dto.ComponentEditResponse;
import ru.samsebemehanik.catalog.dto.AutoComponentDto;
import ru.samsebemehanik.catalog.dto.MenuResponse;
import ru.samsebemehanik.catalog.dto.SearchResponse;

import java.util.List;

public interface AutoComponentService {

    ComponentCreateResponse create(ComponentCreateRequest request);

    ComponentEditResponse edit(UUID id, ComponentEditRequest request);

    AutoComponentDto getById(UUID id);

    AutoComponentPageResponse getAll(int page, int size);

    SearchResponse searchByName(String query, int limit, int offset);

    List<MenuResponse> getLeftMenu();
}
