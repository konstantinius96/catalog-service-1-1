package ru.samsebemehanik.catalog.service;

import java.util.UUID;
import ru.samsebemehanik.catalog.dto.ComponentCreateRequest;
import ru.samsebemehanik.catalog.dto.ComponentCreateResponse;
import ru.samsebemehanik.catalog.dto.ComponentEditRequest;
import ru.samsebemehanik.catalog.dto.ComponentEditResponse;
import ru.samsebemehanik.catalog.dto.AutoComponentDto;

public interface AutoComponentService {

    ComponentCreateResponse create(ComponentCreateRequest request);

    ComponentEditResponse edit(UUID id, ComponentEditRequest request);

    AutoComponentDto getById(UUID id);
}
