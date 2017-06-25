package com.turbo.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Created by ermolaev on 6/17/17.
 */
@Repository
public class ImageConverterRepository {

    private final UriComponentsBuilder uriBuilder;
    private final RestTemplate restTemplate;

    @Autowired
    public ImageConverterRepository(
            RestTemplate restTemplate,
            @Value("${turboconverter.host}") String host
    ) {
        this.restTemplate = restTemplate;
        this.uriBuilder = UriComponentsBuilder.fromUriString(host);
    }

    public Image uploadImages(byte[] imageSource) {

        URI uri = uriBuilder.cloneBuilder()
                .path("/upload")
                .build().toUri();

        ImagePath imagePath = restTemplate.postForObject(uri, imageSource, ImagePath.class);
        return new Image(imagePath.getImage(), imagePath.getThumbnail());
    }

    public void deleteImages(
            final String thumbnail,
            final String source
    ) {
        URI uri = uriBuilder.cloneBuilder()
                .path("/delete")
                .build().toUri();

        ImagePath imagePath = new ImagePath(source, thumbnail);

        restTemplate.exchange(
                uri,
                HttpMethod.DELETE,
                new HttpEntity<>(imagePath),
                Void.class
        );
    }

    private static class ImagePath {

        private String image;
        private String thumbnail;

        public ImagePath(
                @JsonProperty(value = "image", required = true) String image,
                @JsonProperty(value = "thumbnail", required = true) String thumbnail
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
}
