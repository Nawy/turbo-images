package com.turbo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by ermolaev on 5/9/17.
 */
public class Image implements Serializable, IdHolder {

    private Long id;
    private String source;
    private String thumbnail;

    protected Image() {
    }

    public Image(Long id, String source, String thumbnail) {
        this.id = id;
        this.source = source;
        this.thumbnail = thumbnail;
    }

    public Image(String source, String thumbnail) {
        this.source = source;
        this.thumbnail = thumbnail;
    }

    @Override
    public Long getId() {
        return id;
    }

    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    @JsonProperty("thumbnail")
    public String getThumbnail() {
        return thumbnail;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
