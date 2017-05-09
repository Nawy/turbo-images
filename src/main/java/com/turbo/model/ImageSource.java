package com.turbo.model;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by ermolaev on 5/9/17.
 */
@Table("image_source")
public class ImageSource {

    @PrimaryKey
    private String hash;
    private String path;

    public ImageSource() {
    }

    public ImageSource(String hash, String path) {
        this.hash = hash;
        this.path = path;
    }

    public String getHash() {
        return hash;
    }

    public String getPath() {
        return path;
    }

}
