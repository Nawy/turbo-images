package com.turbo.service;

import com.turbo.model.exception.ForbiddenHttpException;
import com.turbo.model.User;
import com.turbo.repository.elasticsearch.content.UserContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rakhmetov on 25.05.17.
 */
@Service
public class UserService {

    private final UserContentRepository userContentRepository;

    @Autowired
    public UserService(UserContentRepository userContentRepository) {
        this.userContentRepository = userContentRepository;
    }

    public User update(User user) {
        if (user.getId() == null) throw new ForbiddenHttpException("Can't update entity without id");
        //FIXME
        User dbUser = null;//userRepository.save(user);
        userContentRepository.updateUser(dbUser);
        return dbUser;
    }

    public User add(User user) {
        //FIXME
        User dbUser = null;//userRepository.save(user);
        userContentRepository.addUser(dbUser);
        return dbUser;
    }

    public User findByEmail(String email) {
        //FIXME HERE SHOULD BE ELASTIC!!!
        return null;
    }
}
