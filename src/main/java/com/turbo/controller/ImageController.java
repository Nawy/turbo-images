package com.turbo.controller;

import com.turbo.model.Image;
import com.turbo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rakhmetov on 01.05.17.
 */
@RestController
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/get/image/source/{id}")
    public byte[] getImageSource(@PathVariable("id") long imageId) {
        return null;
    }

    @GetMapping("/get/image/info/{id}")
    public Image getImageInfo(@PathVariable("id") long imageId) {
        return imageService.getImageInfo(imageId);
    }
}
