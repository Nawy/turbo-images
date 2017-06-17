package com.turbo.model.converter;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ermolaev on 6/17/17.
 */
public class ImagePath {

    private String image;
    private String thumbnail;

    public ImagePath(
            @JsonProperty(value ="image", required = true) String image,
            @JsonProperty(value ="thumbnail", required = true) String thumbnail
    ) {
        this.image = image;
        this.thumbnail = thumbnail;
    }

    public String getImage() {
        return image;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
