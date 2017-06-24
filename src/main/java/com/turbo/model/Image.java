package com.turbo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by ermolaev on 5/9/17.
 */
//TODO Depricated, already don't needed because we have another
public class Image implements Serializable, IdHolder {

    private Long id;
    private String fullImagePath;
    private String smallImagePath;

    public Image() {
    }

    public Image(Long id, String fullImagePath, String smallImagePath) {
        this.id = id;
        this.fullImagePath = fullImagePath;
        this.smallImagePath = smallImagePath;
    }

    @Override
    public Long getId() {
        return id;
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
    public void setId(Long id) {
        this.id = id;
    }
}
