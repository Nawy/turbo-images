package com.turbo.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.turbo.model.Post;
import com.turbo.model.SecurityRole;
import com.turbo.model.User;
import com.turbo.model.page.Paginator;
import com.turbo.model.search.SearchOrder;
import com.turbo.model.search.SearchPattern;
import com.turbo.model.search.SearchPeriod;
import com.turbo.model.search.SearchSort;
import com.turbo.service.AuthorizationService;
import com.turbo.service.PostService;
import com.turbo.service.UserService;
import com.turbo.util.EncryptionService;
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
    @GetMapping("/get/user/posts")
    public Paginator<Post> getUserPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sort", defaultValue = "RATING") SearchSort sort,
            @RequestParam(value = "period", defaultValue = "ALL_TIME") SearchPeriod period,
            @RequestParam(value = "order", defaultValue = "DESC") SearchOrder order
    ) {
        User user = authorizationService.getCurrentUser();
        return new Paginator<>(
                page,
                postService.getUserPosts(page, user.getName(), new SearchPattern(period, sort, order))
        );
    }

    @Secured(SecurityRole.USER)
    @PostMapping("/edit/user/info")
    public UserDto updateUserInfo(@RequestBody UserDto userDto) {
        User user = userDto.toUser();
        userService.userFieldValidation(user.getName());
        userService.userFieldValidation(user.getPassword());
        userService.emailValidation(user.getEmail());
        User updatedUser = userService.update(user);
        return new UserDto(updatedUser);
    }

    private final class UserDto {
        private String id;
        private String name; // should be unique!
        private String avatarPath;
        private String email; // should be unique!
        private String password;
        private LocalDateTime createDate;

        public UserDto(User user) {
            this.id = EncryptionService.encodeHashId(user.getId());
            this.name = user.getName();
            this.avatarPath = user.getAvatarPath();
            this.email = user.getEmail();
            this.password = user.getPassword();
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
            this.email = email;
            this.password = password;
            this.createDate = createDate;
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

        public String getPassword() {
            return password;
        }

        @JsonProperty("create_date")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        public LocalDateTime getCreateDate() {
            return createDate;
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
    }
}
