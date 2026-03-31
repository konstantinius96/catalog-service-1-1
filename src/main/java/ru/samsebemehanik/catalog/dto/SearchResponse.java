package ru.samsebemehanik.catalog.dto;

import java.util.List;

public class SearchResponse {

    private long total;
    private boolean hasMore;
    private List<SearchItem> items;

    public SearchResponse(long total, boolean hasMore, List<SearchItem> items) {
        this.total = total;
        this.hasMore = hasMore;
        this.items = items;
    }

    public long getTotal() {
        return total;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public List<SearchItem> getItems() {
        return items;
    }
}
