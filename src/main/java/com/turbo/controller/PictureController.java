package com.turbo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rakhmetov on 01.05.17.
 */
@RestController
public class PictureController {

    @GetMapping("/get/image/{id}")
    public byte[] getImage(@PathVariable("id") long imageId) {
        return null;
    }
}
