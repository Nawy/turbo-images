package com.turbo.controller;

import com.turbo.model.SecurityRole;
import com.turbo.model.User;
import com.turbo.model.UserImage;
import com.turbo.model.dto.UserImageDto;
import com.turbo.service.AuthorizationService;
import com.turbo.service.HashIdService;
import com.turbo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

/**
 * Created by rakhmetov on 01.05.17.
 */
@RestController
public class ImageController {

    private final ImageService imageService;
    private final AuthorizationService authorizationService;

    @Autowired
    public ImageController(ImageService imageService, AuthorizationService authorizationService) {
        this.imageService = imageService;
        this.authorizationService = authorizationService;
    }

    @GetMapping("/get/image/{id}")
    public UserImageDto getImageInfo(@PathVariable("id") String id) {
        return UserImageDto.from(
                imageService.getUserImage(HashIdService.decodeHashId(id))
        );
    }

    @GetMapping("/image/exists")
    public Map<String, Boolean> checkImageExists(@RequestParam("hash") String hash) {
        return Collections.singletonMap(
                "exists",
                imageService.imageExists(HashIdService.decodeHashId(hash))
        );
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/save/image")
    public UserImageDto saveImage(@RequestBody byte[] source) {
        User user = authorizationService.getCurrentUser();
        UserImage userImage = imageService.addImage(
                user.getName(),
                source
        );
        return UserImageDto.from(userImage);
    }
}
