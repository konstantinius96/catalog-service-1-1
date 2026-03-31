package ru.samsebemehanik.catalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Sort;
import ru.samsebemehanik.catalog.domain.component.AutoComponent;
import ru.samsebemehanik.catalog.dto.SearchResponse;
import ru.samsebemehanik.catalog.kafka.ComponentEventProducer;
import ru.samsebemehanik.catalog.repository.AutoComponentRepository;
import ru.samsebemehanik.catalog.repository.ComponentRelationsViewRepository;
import ru.samsebemehanik.catalog.repository.OffsetLimitPageable;

class AutoComponentServiceImplSearchTest {

    private AutoComponentRepository autoComponentRepository;
    private AutoComponentServiceImpl service;

    @BeforeEach
    void setUp() {
        autoComponentRepository = mock(AutoComponentRepository.class);
        ComponentEventProducer componentEventProducer = mock(ComponentEventProducer.class);
        OutboxService outboxService = mock(OutboxService.class);
        ComponentRelationsViewRepository componentRelationsViewRepository = mock(ComponentRelationsViewRepository.class);

        service = new AutoComponentServiceImpl(
                autoComponentRepository,
                componentEventProducer,
                outboxService,
                componentRelationsViewRepository
        );
    }

    @Test
    void shouldReturnItemsAndHasMoreTrue() {
        AutoComponent item1 = new AutoComponent("Поршень", "desc-1", null, new ObjectMapper().createObjectNode());
        AutoComponent item2 = new AutoComponent("Поршневая группа", null, null, null);

        when(autoComponentRepository.countByNameContainingIgnoreCase("По")).thenReturn(4L);
        when(autoComponentRepository.findByNameContainingIgnoreCase(eq("По"), any()))
                .thenReturn(List.of(item1, item2));

        SearchResponse response = service.searchByName("  По  ", 2, 0);

        assertEquals(4L, response.getTotal());
        assertTrue(response.isHasMore());
        assertEquals(2, response.getItems().size());
        assertEquals("Поршень", response.getItems().get(0).getName());
        assertEquals("desc-1", response.getItems().get(0).getDescription());
        assertEquals("Поршневая группа", response.getItems().get(1).getName());

        ArgumentCaptor<OffsetLimitPageable> captor = ArgumentCaptor.forClass(OffsetLimitPageable.class);
        verify(autoComponentRepository).findByNameContainingIgnoreCase(eq("По"), captor.capture());
        OffsetLimitPageable pageable = captor.getValue();
        assertEquals(0, pageable.getOffset());
        assertEquals(2, pageable.getPageSize());
        assertEquals(Sort.by(Sort.Direction.ASC, "name"), pageable.getSort());
    }

    @Test
    void shouldReturnHasMoreFalseWhenNoMoreItems() {
        when(autoComponentRepository.countByNameContainingIgnoreCase("По")).thenReturn(2L);
        when(autoComponentRepository.findByNameContainingIgnoreCase(eq("По"), any()))
                .thenReturn(List.of(new AutoComponent("Поршень", "desc", null, null)));

        SearchResponse response = service.searchByName("По", 1, 1);

        assertFalse(response.isHasMore());
    }

    @Test
    void shouldReturnEmptyItemsWhenOffsetIsOutsideResultWindow() {
        when(autoComponentRepository.countByNameContainingIgnoreCase("По")).thenReturn(4L);
        when(autoComponentRepository.findByNameContainingIgnoreCase(eq("По"), any()))
                .thenReturn(List.of());

        SearchResponse response = service.searchByName("По", 5, 50);

        assertEquals(4L, response.getTotal());
        assertFalse(response.isHasMore());
        assertTrue(response.getItems().isEmpty());
    }

    @Test
    void shouldValidateQueryLength() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.searchByName(" П ", 5, 0)
        );

        assertEquals("query must contain at least 2 characters", exception.getMessage());
    }

    @Test
    void shouldValidateLimitRange() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.searchByName("По", 51, 0)
        );

        assertEquals("limit must be between 1 and 50", exception.getMessage());
    }

    @Test
    void shouldValidateOffset() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.searchByName("По", 5, -1)
        );

        assertEquals("offset must be greater than or equal to 0", exception.getMessage());
    }
}
