package com.turbo.repository;

import com.turbo.model.UserInfo;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ermolaev on 5/7/17.
 */
public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {

    @Query("SELECT * FROM users WHERE nickname = ?0")
    UserInfo getUserByNickname(String nickname);

    @Query("SELECT * FROM users WHERE nickname LIKE ?0")
    List<UserInfo> findUserByNickname(String pattern);
}
