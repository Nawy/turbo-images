package com.turbo.controller;

import com.turbo.model.ImageInfo;
import com.turbo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/get/image/info/{id}")
    public ImageInfo getImageInfo(@PathVariable("id") long imageId) {
        return imageService.getImageInfo(imageId);
    }

    @GetMapping("/check/image/exists")
    public Boolean checkImageExists(@RequestParam("hash") String hash) {
        return imageService.checkImageExists(hash);
    }

    @PostMapping("/add/image/source")
    public void addImageSource(
            @RequestParam("hash") String hash,
            @RequestBody byte[] source
    ) {
        imageService.addImageSource(hash, source);
    }

    @PostMapping("/add/image/info")
    public Integer addImageInfo(@RequestBody ImageInfo imageInfo) {
        return imageService.addImageInfo(imageInfo);
    }
}
