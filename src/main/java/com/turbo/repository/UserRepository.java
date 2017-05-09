package com.turbo.repository;

import com.turbo.model.user.User;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ermolaev on 5/9/17.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT * FROM user WHERE email LIKE ?0")
    User findUser(String email);

}
