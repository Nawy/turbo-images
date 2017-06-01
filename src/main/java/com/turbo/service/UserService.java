package com.turbo.service;

import com.turbo.model.exception.http.ForbiddenHttpException;
import com.turbo.model.user.User;
import com.turbo.repository.elasticsearch.UserSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rakhmetov on 25.05.17.
 */
@Service
public class UserService {

    private final UserSearchRepository userSearchRepository;

    @Autowired
    public UserService(UserSearchRepository userSearchRepository) {
        this.userSearchRepository = userSearchRepository;
    }

    public User update(User user) {
        if (user.getId() == null) throw new ForbiddenHttpException("Can't update entity without id");
        //FIXME
        User dbUser = null;//userRepository.save(user);
        userSearchRepository.updateUser(dbUser);
        return dbUser;
    }

    public User add(User user) {
        //FIXME
        User dbUser = null;//userRepository.save(user);
        userSearchRepository.addUser(dbUser);
        return dbUser;
    }

    public User findByEmail(String email) {
        //FIXME HERE SHOULD BE ELASTIC!!!
        return null;
    }
}
