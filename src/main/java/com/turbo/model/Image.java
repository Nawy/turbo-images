package com.turbo.model;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by ermolaev on 5/9/17.
 */
@Table("image")
public class Image {

    @PrimaryKey
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
