package com.turbo.repository;

import com.turbo.model.converter.ImagePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    public ImagePath uploadImages(byte[] imageSource) {

        URI uri = uriBuilder.cloneBuilder()
                .path("/upload")
                .build().toUri();

        return restTemplate.postForObject(uri, imageSource, ImagePath.class);
    }
}
