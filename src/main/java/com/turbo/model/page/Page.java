package com.turbo.model.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.Assert;

public class Page {

    private final int page;
    private final int size;

    public Page(int page, int size) {
        this.page = page < 0 ? 0 : page;
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    @JsonIgnore
    public int getOffset() {
        return getPage() * getSize();
    }

}
