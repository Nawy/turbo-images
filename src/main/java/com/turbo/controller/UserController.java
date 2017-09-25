package com.turbo.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.Post;
import com.turbo.model.SecurityRole;
import com.turbo.model.User;
import com.turbo.model.exception.BadRequestHttpException;
import com.turbo.model.page.Paginator;
import com.turbo.model.search.SearchOrder;
import com.turbo.model.search.SearchPattern;
import com.turbo.model.search.SearchPeriod;
import com.turbo.model.search.SearchSort;
import com.turbo.service.AuthorizationService;
import com.turbo.service.PostService;
import com.turbo.service.UserService;
import com.turbo.util.EncryptionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

/**
 * Created by rakhmetov on 24.05.17.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    private final AuthorizationService authorizationService;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public UserController(AuthorizationService authorizationService, UserService userService, PostService postService) {
        this.authorizationService = authorizationService;
        this.userService = userService;
        this.postService = postService;
    }

    @Secured(SecurityRole.USER)
    @GetMapping("/get/user/info")
    public UserDto getCurrentUser() {
        return new UserDto(
                authorizationService.getCurrentUser()
        );
    }

    @GetMapping("/get/user/by_name_or_email")
    public UserDto getUserByEmail(@RequestParam("name_or_email") String nameOrEmail) {
        userService.userFieldValidation(nameOrEmail);
        return new UserDto(
                userService.get(nameOrEmail)
        );
    }

    @GetMapping("/is/user/exists")
    public Map<String, Boolean> userExists(@RequestParam("name_or_email") String nameOrEmail) {
        userService.userFieldValidation(nameOrEmail);
        return Collections.singletonMap(
                "exists",
                userService.isEmailOrNameExists(nameOrEmail)
        );
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/update/user/password")
    public UserDto updateUserPassword(@RequestBody UserPasswordChangeDto userPasswordChangeDto) {
        String newPassword = userPasswordChangeDto.getNewPassword();
        String oldPassword = userPasswordChangeDto.getOldPassword();
        validateField(newPassword, "new_password");
        validateField(oldPassword, "old_password");
        if (oldPassword.equals(newPassword)) throw new BadRequestHttpException("Old and new Passwords are the same!");
        long currentUserId = authorizationService.getCurrentUserId();
        User user = userService.updateUserPassword(currentUserId, oldPassword, newPassword);
        return new UserDto(user);
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/update/user/name")
    public UserDto updateUserName(@RequestBody UserFieldUpdateDto userPasswordChangeDto) {
        String newName = userPasswordChangeDto.getNewField();
        validateField(newName, "new_name");
        long currentUserId = authorizationService.getCurrentUserId();
        User user = userService.updateUserName(currentUserId, newName);
        return new UserDto(user);
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/update/user/email")
    public UserDto updateUserEmail(@RequestBody UserFieldUpdateDto userPasswordChangeDto) {
        String newEmail = userPasswordChangeDto.getNewField();
        validateEmail(newEmail);
        long currentUserId = authorizationService.getCurrentUserId();
        User user = userService.updateUserEmail(currentUserId, newEmail);
        return new UserDto(user);
    }

    private void validateField(String field, String fieldName) {
        if (StringUtils.isBlank(field)) throw new BadRequestHttpException(fieldName + " can't be blank");
        userService.userFieldValidation(field);
    }

    private void validateEmail(String field) {
        if (StringUtils.isBlank(field)) throw new BadRequestHttpException("email can't be blank");
        userService.emailValidation(field);
    }

    private static class UserFieldUpdateDto {
        private String newField;

        public UserFieldUpdateDto(@JsonProperty("new_user_field") String newField) {
            this.newField = newField;
        }

        public String getNewField() {
            return newField;
        }
    }

    private static class UserPasswordChangeDto {
        private String oldPassword;
        private String newPassword;

        public UserPasswordChangeDto(
                @JsonProperty(value = "old_password", required = true) String oldPassword,
                @JsonProperty(value = "new_password", required = true) String newPassword
        ) {
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
        }

        public String getOldPassword() {
            return oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }
    }

    private final class UserDto {

        private String id;
        private String name;
        private String avatarPath;
        private String email;
        private String password;
        private LocalDateTime createDate;

        public UserDto(User user) {
            this.id = EncryptionService.encodeHashId(user.getId());
            this.name = user.getName();
            this.avatarPath = user.getAvatarPath();
            this.password = user.getPassword();
            this.email = user.getEmail();
            this.createDate = user.getCreateDate();
        }

        public UserDto(
                @JsonProperty("id") String id,
                @JsonProperty("name") String name,
                @JsonProperty("avatar_path") String avatarPath,
                @JsonProperty("email") String email,
                @JsonProperty("password") String password,
                @JsonProperty("create_date") LocalDateTime createDate
        ) {
            this.id = id;
            this.name = name;
            this.avatarPath = avatarPath;
            this.password = password;
            this.email = email;
            this.createDate = createDate;
        }

        @JsonIgnore
        public User toUser() {
            return new User(
                    EncryptionService.decodeHashId(id),
                    name,
                    avatarPath,
                    email,
                    password,
                    createDate
            );
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @JsonProperty("avatar_path")
        public String getAvatarPath() {
            return avatarPath;
        }

        public String getEmail() {
            return email;
        }

        @JsonProperty("create_date")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        public LocalDateTime getCreateDate() {
            return createDate;
        }

        @JsonIgnore
        public String getPassword() {
            return password;
        }
    }
}
