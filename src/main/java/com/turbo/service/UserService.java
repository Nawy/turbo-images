package com.turbo.service;

import com.turbo.model.User;
import com.turbo.model.exception.ForbiddenHttpException;
import com.turbo.model.exception.NotFoundHttpException;
import com.turbo.repository.aerospike.UserRepository;
import com.turbo.repository.elasticsearch.content.UserSearchRepository;
import org.springframework.stereotype.Service;

/**
 * Created by rakhmetov on 25.05.17.
 */
@Service
public class UserService {

    private final UserSearchRepository userSearchRepository;
    private final UserRepository userRepository;

    public UserService(UserSearchRepository userSearchRepository, UserRepository userRepository) {
        this.userSearchRepository = userSearchRepository;
        this.userRepository = userRepository;
    }

    public User update(User user) {
        if (user.getName() == null) throw new ForbiddenHttpException("Can't update entity without name");
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
        User user = findByEmailInternal(email);
        if (user == null) throw new NotFoundHttpException("No user found for email:" + email);
        return user;
    }

    private User findByEmailInternal(String email) {
        String username = userSearchRepository.getUserByEmail(email);
        return username != null ?
                userRepository.get(username) :
                userRepository.getByEmail(email);
    }

    public boolean isEmailExists(String email) {
        User user = findByEmailInternal(email);
        return user != null;
    }

    public boolean isUsernameExists(String username) {
        return userRepository.exists(username);
    }

    public User get(String username) {
        return userRepository.get(username);
    }
}
