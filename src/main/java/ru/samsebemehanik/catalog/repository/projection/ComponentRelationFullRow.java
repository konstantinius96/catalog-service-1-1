package ru.samsebemehanik.catalog.repository.projection;

import java.util.UUID;

public interface ComponentRelationFullRow {

    String getRelationType();

    UUID getFromId();

    String getFromName();

    UUID getToId();

    String getToName();
}
