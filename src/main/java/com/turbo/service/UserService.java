package com.turbo.service;

import com.turbo.model.exception.http.ForbiddenHttpException;
import com.turbo.model.user.User;
import com.turbo.repository.couchbase.UserRepository;
import com.turbo.repository.elasticsearch.UserSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rakhmetov on 25.05.17.
 */
@Service
public class UserService {

    private final UserSearchRepository userSearchRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserSearchRepository userSearchRepository, UserRepository userRepository) {
        this.userSearchRepository = userSearchRepository;
        this.userRepository = userRepository;
    }

    public User update(User user) {
        if (user.getId() == null) throw new ForbiddenHttpException("Can't update entity without id");
        User dbUser = userRepository.save(user);
        userSearchRepository.updateUser(dbUser);
        return dbUser;
    }

    public User add(User user) {
        User dbUser = userRepository.save(user);
        userSearchRepository.addUser(dbUser);
        return dbUser;
    }

    public User findByEmail(String email) {
        //FIXME HERE SHOULD BE ELASTIC!!!
        return userRepository.findByEmail(email);
    }
}
