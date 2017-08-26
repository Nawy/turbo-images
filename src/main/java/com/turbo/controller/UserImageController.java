package com.turbo.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.SecurityRole;
import com.turbo.model.User;
import com.turbo.model.UserImage;
import com.turbo.model.dto.UserImageDto;
import com.turbo.service.AuthorizationService;
import com.turbo.util.EncryptionService;
import com.turbo.service.UserImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rakhmetov on 01.05.17.
 */
@RestController
@RequestMapping("/api")
public class UserImageController {

    private static final Logger LOG = LoggerFactory.getLogger(UserImageController.class);

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
                userImageService.getUserImage(EncryptionService.decodeHashId(id))
        );
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/add/user/image")
    public UserImageDto saveImage(
            @RequestParam("file") MultipartFile file
    ) {
        User user = authorizationService.getCurrentUser();
        LOG.info("File uploaded with name='{}'", file.getOriginalFilename());
        try {
            UserImage userImage = userImageService.addUserImage(user.getId(), file.getBytes());
            return UserImageDto.from(userImage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    @Secured(SecurityRole.USER)
    @GetMapping("/get/user/images")
    public List<UserImageDto> getUserImages(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS") LocalDateTime startDate) {
        List<UserImage> userImages = userImageService.getCurrentUserImages(startDate);
        return userImages.stream().map(UserImageDto::from).collect(Collectors.toList());
    }

//    // TODO strange! you should define user id by session id. And bulk get doesn't work
//    @GetMapping("/get/user/images")
//    public List<UserImageDto> getUserImages(@RequestParam("user_id") long userId) {
//        List<UserImage> userImages = userImageService.getUserImages(userId);
//        return userImages.stream().map(UserImageDto::from).collect(Collectors.toList());
//    }

    @DeleteMapping("/remove/user/image")
    public void removeUserImage(@RequestBody UserImageRemoveDto userImageRemoveDto) {
        userImageService.removeUserImage(userImageRemoveDto.getUserId(), userImageRemoveDto.getUserImageId());
    }

    private static class UserImageRemoveDto {
        private long userId;
        private long userImageId;

        public UserImageRemoveDto(
                @JsonProperty(value = "user_id", required = true) long userId,
                @JsonProperty(value = "user_image_id", required = true) long userImageId
        ) {
            this.userId = userId;
            this.userImageId = userImageId;
        }

        public long getUserId() {
            return userId;
        }

        public long getUserImageId() {
            return userImageId;
        }
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
