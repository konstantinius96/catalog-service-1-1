package ru.samsebemehanik.catalog.dto;

import java.util.List;

public class AutoComponentPageResponse {

    private List<AutoComponentDto> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public AutoComponentPageResponse(List<AutoComponentDto> content, int page, int size, long totalElements, int totalPages) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<AutoComponentDto> getContent() {
        return content;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
