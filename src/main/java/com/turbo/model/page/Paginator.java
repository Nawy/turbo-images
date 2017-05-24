package com.turbo.model.page;

import java.util.List;

/**
 * Created by ermolaev on 5/24/17.
 */
public class Paginator<T> {
    private long from;
    private long size;
    private long total;
    private List<T> items;

    public Paginator(long from, long size, long total, List<T> items) {
        this.from = from;
        this.size = size;
        this.total = total;
        this.items = items;
    }

    public long getFrom() {
        return from;
    }

    public long getSize() {
        return size;
    }

    public long getTotal() {
        return total;
    }

    public List<T> getItems() {
        return items;
    }
}
