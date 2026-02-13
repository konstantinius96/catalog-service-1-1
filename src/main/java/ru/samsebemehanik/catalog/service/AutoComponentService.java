package ru.samsebemehanik.catalog.service;

import ru.samsebemehanik.catalog.dto.ComponentCreateRequest;
import ru.samsebemehanik.catalog.dto.ComponentCreateResponse;

public interface AutoComponentService {

    ComponentCreateResponse create(ComponentCreateRequest request);
}
