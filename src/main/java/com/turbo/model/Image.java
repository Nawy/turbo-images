package com.turbo.model;

import java.io.Serializable;

/**
 * Created by ermolaev on 5/9/17.
 */
public class Image implements Serializable, IdHolder {

    private Long hash;
    private String path;

    public Image() {
    }

    public Image(Long hash, String path) {
        this.hash = hash;
        this.path = path;
    }

    public long getHash() {
        return hash;
    }

    public String getPath() {
        return path;
    }

    @Override
    public Long getId() {
        return hash;
    }

    @Override
    public void setId(Long id) {
        hash = id;
    }
}
