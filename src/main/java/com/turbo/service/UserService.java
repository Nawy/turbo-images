package com.turbo.service;

import com.turbo.model.exception.ForbiddenHttpException;
import com.turbo.model.User;
import com.turbo.repository.elasticsearch.content.UserSearchRepository;
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
        if (user.getName() == null) throw new ForbiddenHttpException("Can't update entity without name");
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
