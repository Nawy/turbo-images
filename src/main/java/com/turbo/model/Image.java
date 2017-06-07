package com.turbo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by ermolaev on 5/9/17.
 */
public class Image implements Serializable, IdHolder {

    private Long hash;
    private String fullImagePath;
    private String smallImagePath;

    public Image() {
    }

    public Image(Long hash, String fullImagePath, String smallImagePath) {
        this.hash = hash;
        this.fullImagePath = fullImagePath;
        this.smallImagePath = smallImagePath;
    }

    public Long getHash() {
        return hash;
    }

    @JsonProperty("full_image_path")
    public String getFullImagePath() {
        return fullImagePath;
    }

    @JsonProperty("small_image_path")
    public String getSmallImagePath() {
        return smallImagePath;
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
