package com.turbo.model.user;

/**
 * Created by ermolaev on 5/6/17.
 * <p>
 * This data for every foreign user
 */
public class User {

    private Long id;
    private String nickname;
    private String avatarPath;
    private String lastIp; // last lastIp from what was came in
    private String email;
    private String password;

    public User() {
    }

    public User(Long id, String nickname, String avatarPath, String lastIp, String email, String password) {
        this.id = id;
        this.nickname = nickname;
        this.avatarPath = avatarPath;
        this.lastIp = lastIp;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public String getLastIp() {
        return lastIp;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
