package com.turbo.model.search.entity;

import java.time.LocalDateTime;

/**
 * Created by ermolaev on 5/27/17.
 * Needs for send enough data in search engine
 */
public class UserSearchEntity {

    private String id;
    private String name;
    private String avatarPath;
//    private String ip; // last ip from what was came in
    private String email;
    private String password;
    private LocalDateTime createDate;


}
