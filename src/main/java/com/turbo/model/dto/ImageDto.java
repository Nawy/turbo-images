package com.turbo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.Image;
import com.turbo.util.EncryptionService;

/**
 * Created by ermolaev on 7/12/17.
 */
public class ImageDto {
    private String id;
    private String source;
    private String thumbnail;

    protected ImageDto() {
    }

    public ImageDto(String id, String source, String thumbnail) {
        this.id = id;
        this.source = source;
        this.thumbnail = thumbnail;
    }

    @JsonProperty("id")
    public String getId() {
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

    public static ImageDto from(Image image) {
        return new ImageDto(
                EncryptionService.encodeHashId(image.getId()),
                image.getSource(),
                image.getThumbnail()
        );
    }
}
