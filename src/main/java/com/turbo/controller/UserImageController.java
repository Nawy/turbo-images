package com.turbo.controller;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.turbo.model.SecurityRole;
import com.turbo.model.User;
import com.turbo.model.UserImage;
import com.turbo.model.dto.UserImageDto;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.service.AuthorizationService;
import com.turbo.service.UserImageService;
import com.turbo.util.EncryptionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rakhmetov on 01.05.17.
 */
@RestController
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserImageController {

    private final UserImageService userImageService;
    private final AuthorizationService authorizationService;

    @GetMapping("/get/user/image/{id}")
    public UserImageDto getImageInfo(@PathVariable("id") String id) {
        return UserImageDto.from(
                userImageService.getUserImage(EncryptionService.decodeHashId(id))
        );
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/add/user/image")
    public UserImageDto saveImage(
            @RequestParam("file") MultipartFile file
    ) {
        User user = authorizationService.getCurrentUser();
        log.info("File uploaded with name='{}'", file.getOriginalFilename());
        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            log.error("Can't parse multipart file", e);
            throw new InternalServerErrorHttpException("Can't parse multipart file");
        }
        UserImage userImage = userImageService.addUserImage(user.getId(), bytes);
        return UserImageDto.from(userImage);
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/edit/user/image/description")
    public UserImageDto saveUserImage(@RequestBody UserImageEditDto descriptionDto) {
        UserImage userImage = userImageService.editUserImageDescription(
                EncryptionService.decodeHashId(descriptionDto.getUserImageId()),
                descriptionDto.getField() //description
        );
        return UserImageDto.from(userImage);
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/edit/user/image/name")
    public UserImageDto editUserImageName(@RequestBody UserImageEditDto descriptionDto) {
        UserImage userImage = userImageService.editUserImageName(
                EncryptionService.decodeHashId(descriptionDto.getUserImageId()),
                descriptionDto.getField() // userImage name
        );
        return UserImageDto.from(userImage);
    }

    @Secured(SecurityRole.USER)
    @GetMapping("/get/user/images")
    public List<UserImageDto> getUserImages(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS") LocalDateTime startDate
    ) {
        long userId = authorizationService.getCurrentUserId();
        List<UserImage> userImages = userImageService.getCurrentUserImages(userId, startDate);
        return userImages.stream().map(UserImageDto::from).collect(Collectors.toList());
    }

    @DeleteMapping("/remove/user/image")
    public void removeUserImage(@RequestBody UserImageRemoveDto userImageRemoveDto) {
        userImageService.removeUserImage(
                EncryptionService.decodeHashId(userImageRemoveDto.getUserId()),
                EncryptionService.decodeHashId(userImageRemoveDto.getUserImageId())
        );
    }

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    private static class UserImageRemoveDto {
        private String userId;
        private String userImageId;
    }

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    private static class UserImageEditDto {
        private String userImageId;
        private String field;
    }

}
