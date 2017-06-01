package com.turbo.controller;

import com.turbo.model.Image;
import com.turbo.service.HashIdService;
import com.turbo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

/**
 * Created by rakhmetov on 01.05.17.
 */
@RestController
public class ImageController {

    private final ImageService imageService;
    private final HashIdService hashIdService;

    @Autowired
    public ImageController(ImageService imageService, HashIdService hashIdService) {
        this.imageService = imageService;
        this.hashIdService = hashIdService;
    }

    @GetMapping("/get/image/{hash}")
    public Image getImageInfo(@PathVariable("hash") String hash) {
        return imageService.getImage(
                hashIdService.decodeHashId(hash)
        );
    }

    @GetMapping("/image/exists")
    public Map<String, Boolean> checkImageExists(@RequestParam("hash") String hash) {
        return Collections.singletonMap(
                "exists",
                imageService.imageExists(hashIdService.decodeHashId(hash))
        );
    }

    @PostMapping("/add/image")
    public void addImageSource(
            @RequestParam("hash") String hash,
            @RequestBody byte[] source
    ) {
        imageService.addImage(
                hashIdService.decodeHashId(hash),
                source
        );
    }
}
