package com.turbo.service;

import com.turbo.model.User;
import com.turbo.model.exception.ForbiddenHttpException;
import com.turbo.model.exception.NotFoundHttpException;
import com.turbo.repository.aerospike.user.UserNameEmailRepository;
import com.turbo.repository.aerospike.user.UserRepository;
import com.turbo.repository.elasticsearch.content.UserSearchRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rakhmetov on 25.05.17.
 */
@Service
public class UserService {

    private final UserSearchRepository userSearchRepository;
    private final UserNameEmailRepository userNameEmailRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserSearchRepository userSearchRepository, UserNameEmailRepository userNameEmailRepository, UserRepository userRepository) {
        this.userSearchRepository = userSearchRepository;
        this.userNameEmailRepository = userNameEmailRepository;
        this.userRepository = userRepository;
    }

    public User update(User user) {
        if (user.getId() == null) throw new ForbiddenHttpException("Can't update entity without id");
        isBlankNameEmailValidation(user, "Can't update user with blank name/email");
        User oldUser = get(user.getId());
        User newUser = userRepository.save(user);
        updateNameEmailKey(oldUser, newUser);
        userSearchRepository.updateUser(newUser);
        return newUser;
    }

    public User add(User user) {
        isBlankNameEmailValidation(user, "Can't add user with blank name/email");
        User dbUser = userRepository.save(user);
        addNameEmailKey(dbUser);
        userSearchRepository.addUser(dbUser);
        return dbUser;
    }

    public boolean isEmailOrNameExists(String emailOrName) {
        return userNameEmailRepository.exists(emailOrName);
    }

    public User get(String emailOrName) {
        NotFoundHttpException notFound = new NotFoundHttpException("No user found for email/name:" + emailOrName);
        if (StringUtils.isBlank(emailOrName)) throw notFound;
        Long userId = userNameEmailRepository.get(emailOrName);
        if (userId == null) throw notFound;
        try {
            return get(userId);
        } catch (NotFoundHttpException e) {
            throw notFound;
        }
    }

    public User get(long userId) {
        User user = userRepository.get(userId);
        if (user == null) throw new NotFoundHttpException("No user found for id:" + user);
        return user;
    }

    private void updateNameEmailKey(User oldUser, User newUser) {
        deleteNameEmailKey(oldUser);
        addNameEmailKey(newUser);
    }

    private void deleteNameEmailKey(User user) {
        isBlankNameEmailValidation(user, "Can't delete name/email entity with name/email blank");
        userNameEmailRepository.delete(user.getName());
        userNameEmailRepository.delete(user.getEmail());
    }

    private void addNameEmailKey(User user) {
        if (user.getId() == null) throw new ForbiddenHttpException("Can't save name/email entity without id");
        isBlankNameEmailValidation(user, "Can't save name/email entity with name/email blank");
        userNameEmailRepository.save(user.getEmail(), user.getId());
        userNameEmailRepository.save(user.getName(), user.getId());
    }

    private void isBlankNameEmailValidation(User user, String message) {
        if (StringUtils.isBlank(user.getName()) || StringUtils.isBlank(user.getEmail())) {
            throw new ForbiddenHttpException(message);
        }
    }
}
