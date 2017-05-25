package com.turbo.service;

import com.turbo.model.exception.http.ForbiddenHttpException;
import com.turbo.model.user.User;
import com.turbo.repository.couchbase.UserRepository;
import com.turbo.repository.elasticsearch.UserElasticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rakhmetov on 25.05.17.
 */
@Service
public class UserService {

    private final UserElasticRepository userElasticRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserElasticRepository userElasticRepository, UserRepository userRepository) {
        this.userElasticRepository = userElasticRepository;
        this.userRepository = userRepository;
    }

    public User update(User user) {
        if (user.getId() == null) throw new ForbiddenHttpException("Can't update entity without id");
        User dbUser = userRepository.save(user);
        userElasticRepository.updateUser(dbUser);
        return dbUser;
    }

    public User add(User user) {
        User dbUser = userRepository.save(user);
        userElasticRepository.addUser(dbUser);
        return dbUser;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
