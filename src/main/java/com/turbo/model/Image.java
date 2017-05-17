package com.turbo.model;

/**
 * Created by ermolaev on 5/9/17.
 */
public class Image {

    private long hash;
    private String path;

    public Image() {
    }

    public Image(long hash, String path) {
        this.hash = hash;
        this.path = path;
    }

    public long getHash() {
        return hash;
    }

    public String getPath() {
        return path;
    }

}
