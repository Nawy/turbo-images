package com.turbo.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.SecurityRole;
import com.turbo.model.User;
import com.turbo.model.UserImage;
import com.turbo.model.dto.UserImageDto;
import com.turbo.service.AuthorizationService;
import com.turbo.service.HashIdService;
import com.turbo.service.UserImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

/**
 * Created by rakhmetov on 01.05.17.
 */
@RestController
public class UserImageController {

    private final UserImageService userImageService;
    private final AuthorizationService authorizationService;

    @Autowired
    public UserImageController(UserImageService userImageService, AuthorizationService authorizationService) {
        this.userImageService = userImageService;
        this.authorizationService = authorizationService;
    }

    @GetMapping("/get/user/image/{id}")
    public UserImageDto getImageInfo(@PathVariable("id") String id) {
        return UserImageDto.from(
                userImageService.getUserImage(HashIdService.decodeHashId(id))
        );
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/add/user/image")
    public UserImageDto saveImage(
            @RequestBody byte[] source
    ) {
        User user = authorizationService.getCurrentUser();
        UserImage userImage = userImageService.addUserImage(user.getName(), source);
        return UserImageDto.from(userImage);
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/edit/user/image/description")
    public UserImageDto saveUserImage(@RequestBody UserImageDescriptionDto descriptionDto) {
        UserImage userImage = userImageService.editUserImageDescription(
                descriptionDto.getUserImageId(),
                descriptionDto.getDescription()
        );
        return UserImageDto.from(userImage);
    }

    private static class UserImageDescriptionDto {
        private long userImageId;
        private String description;

        public UserImageDescriptionDto(
                @JsonProperty(value = "user_image_id", required = true) long userImageId,
                @JsonProperty(value = "description", required = true) String description
        ) {
            this.userImageId = userImageId;
            this.description = description;
        }

        public long getUserImageId() {
            return userImageId;
        }

        public String getDescription() {
            return description;
        }
    }

}
