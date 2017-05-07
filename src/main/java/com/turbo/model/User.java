package com.turbo.model;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.UUID;

/**
 * Created by ermolaev on 5/6/17.
 */
@Table
public class User {

    @PrimaryKey
    private long id;
    private String nicname;
    private String avatarPath;
    private String computer;
    private String ip;
    private String token; //hash of password

    public User() {
    }

    public User(long id, String nicname, String avatarPath, String computer, String ip, String token) {
        this.id = id;
        this.nicname = nicname;
        this.avatarPath = avatarPath;
        this.computer = computer;
        this.ip = ip;
        this.token = token;
    }
}
