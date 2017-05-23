package com.turbo.model;

import java.io.Serializable;

/**
 * Created by ermolaev on 5/9/17.
 */
public class Image implements Serializable, IdHolder {

    private String hash;
    private String path;

    public Image() {
    }

    public Image(String hash, String path) {
        this.hash = hash;
        this.path = path;
    }

    public String getHash() {
        return hash;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String getId() {
        return hash;
    }

    @Override
    public void setId(String id) {
        hash = id;
    }
}
