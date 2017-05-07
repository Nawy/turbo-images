package com.turbo.repository;

import com.turbo.model.User;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ermolaev on 5/7/17.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT * FROM users WHERE nickname = ?0")
    User getUserByNickname(String nickname);

    @Query("SELECT * FROM users WHERE nickname LIKE ?0")
    List<User> findUserByNickname(String pattern);
}
