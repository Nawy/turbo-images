package com.turbo.model.page;

import java.util.List;

/**
 * Created by ermolaev on 5/24/17.
 */
public class Paginator<T> {
    private Page page;
    private long total;
    private List<T> items;

    public Paginator(Page page, long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public Page getPage() {
        return page;
    }

    public long getTotal() {
        return total;
    }

    public List<T> getItems() {
        return items;
    }
}
