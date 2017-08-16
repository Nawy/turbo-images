package com.turbo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Image image = (Image) o;

        return new EqualsBuilder()
                .append(id, image.id)
                .append(source, image.source)
                .append(thumbnail, image.thumbnail)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(source)
                .append(thumbnail)
                .toHashCode();
    }
}
