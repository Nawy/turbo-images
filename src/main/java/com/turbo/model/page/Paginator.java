package com.turbo.model.page;

import java.util.List;

/**
 * Created by ermolaev on 5/24/17.
 */
public class Paginator<T> {
    private Page page;
    private List<T> items;

    public Paginator(int page, List<T> items) {
        this.page = new Page(page);
        this.items = items;
    }

    public Page getPage() {
        return page;
    }

    public List<T> getItems() {
        return items;
    }
}
