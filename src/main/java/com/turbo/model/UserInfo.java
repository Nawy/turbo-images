package com.turbo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.UUID;

/**
 * Created by ermolaev on 5/6/17.
 */
@Table("user_info")
public class UserInfo {

    @PrimaryKey
    private long id;
    private String nicname;
    private String avatarPath;
    private String device;
    private String ipaddr;
    private String token; //password hash

    public UserInfo() {
    }

    public UserInfo(
            @JsonProperty("id") long id,
            @JsonProperty("nickname") String nicname,
            @JsonProperty("avatar_path") String avatarPath,
            @JsonProperty("device") String device,
            @JsonProperty("ipaddr") String ipaddr,
            String token
    ) {
        this.id = id;
        this.nicname = nicname;
        this.avatarPath = avatarPath;
        this.device = device;
        this.ipaddr = ipaddr;
        this.token = token;
    }

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    @JsonProperty("nickname")
    public String getNicname() {
        return nicname;
    }

    @JsonProperty("avatar_path")
    public String getAvatarPath() {
        return avatarPath;
    }

    @JsonProperty("device")
    public String getDevice() {
        return device;
    }

    @JsonProperty("ip")
    public String getIpaddr() {
        return ipaddr;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }
}
