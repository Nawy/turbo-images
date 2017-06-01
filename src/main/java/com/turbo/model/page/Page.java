package com.turbo.model.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.Assert;

public class Page {

    private final int page;
    private static final int SIZE = 50;

    public Page(int page) {
        this.page = page < 0 ? 0 : page;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return SIZE;
    }

    @JsonIgnore
    public int getOffset() {
        return getPage() * getSize();
    }

}
