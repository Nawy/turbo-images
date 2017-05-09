package com.turbo.model.user;

import com.google.common.net.InetAddresses;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by ermolaev on 5/6/17.
 * <p>
 * This data for every foreign user
 */
@Table("user")
public class User {

    @PrimaryKey
    private Long id;
    private String nickname;
    private String avatarPath;
    private InetAddresses lastIp; // last lastIp from what was came in
    private String token; //password hash

    public User() {
    }

    public User(
            Long id,
            String nickname,
            String avatarPath,
            InetAddresses lastIp,
            String token
    ) {
        this.id = id;
        this.nickname = nickname;
        this.avatarPath = avatarPath;
        this.lastIp = lastIp;
        this.token = token;
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

    public InetAddresses getLastIp() {
        return lastIp;
    }

    public String getToken() {
        return token;
    }
}
