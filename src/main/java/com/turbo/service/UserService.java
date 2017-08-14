package com.turbo.service;

import com.turbo.model.User;
import com.turbo.model.exception.BadRequestHttpException;
import com.turbo.model.exception.ForbiddenHttpException;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.model.exception.NotFoundHttpException;
import com.turbo.repository.aerospike.user.UserNameEmailRepository;
import com.turbo.repository.aerospike.user.UserRepository;
import com.turbo.repository.elasticsearch.content.UserSearchRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by rakhmetov on 25.05.17.
 */
@Service
public class UserService {

    private final UserSearchRepository userSearchRepository;
    private final UserNameEmailRepository userNameEmailRepository;
    private final UserRepository userRepository;
    private final int maxFieldSize;

    @Autowired
    public UserService(
            UserSearchRepository userSearchRepository,
            UserNameEmailRepository userNameEmailRepository,
            UserRepository userRepository,
            @Value("${auth.field.size}") int maxFieldSize
    ) {
        this.userSearchRepository = userSearchRepository;
        this.userNameEmailRepository = userNameEmailRepository;
        this.userRepository = userRepository;
        this.maxFieldSize = maxFieldSize;
    }

    public void userFieldValidation(String field) {
        if (StringUtils.isBlank(field)) {
            throw new BadRequestHttpException("None of fields can be blank");
        }
        if (field.length() >= maxFieldSize) {
            throw new BadRequestHttpException("None of the fields could be longer than:" + maxFieldSize);
        }
    }

    public void emailValidation(String email) {
        userFieldValidation(email);
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new BadRequestHttpException("Email incorrect format!");
        }
    }

    public User updateUserPassword(long oldUserId, String oldPassword, String newPassword) {
        User oldUser = get(oldUserId);
        if (!oldUser.getPassword().equals(oldPassword)) throw new ForbiddenHttpException("Incorrect old password!");

        User userUpdateFields = new User(null, null, null, newPassword);
        return updateUser(oldUser, userUpdateFields);
    }

    public User updateUserName(long oldUserId, String newName) {
        User oldUser = get(oldUserId);

        User userUpdateFields = new User(newName, null, null, null);
        return updateUser(oldUser, userUpdateFields);
    }

    public User updateUserEmail(long oldUserId, String newEmail) {
        User oldUser = get(oldUserId);

        User userUpdateFields = new User(null, null, newEmail, null);
        return updateUser(oldUser, userUpdateFields);
    }

    // userWithUpdateFields - have not null fields that should be updated
    private User updateUser(User oldUser, User userWithUpdateFields) {
        User userWithUpdatedFields = new User(oldUser, userWithUpdateFields);

        User dbUser = userRepository.save(userWithUpdatedFields);
        updateNameEmailKey(oldUser, dbUser);
        userSearchRepository.updateUser(dbUser);
        return dbUser;
    }

    public User add(User user) {
        isBlankNameEmailValidation(user);
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
        if (user == null) throw new NotFoundHttpException("No user found for id:" + userId);
        return user;
    }

    private void updateNameEmailKey(User oldUser, User newUser) {
        isBlankNameEmailValidation(oldUser);
        isBlankNameEmailValidation(newUser);

        updateNameEmailKey(oldUser.getEmail(), newUser.getEmail(), newUser.getId());
        updateNameEmailKey(oldUser.getName(), newUser.getName(), newUser.getId());
    }

    private void updateNameEmailKey(String oldKey, String newKey, long userId) {
        if (oldKey == null) throw new InternalServerErrorHttpException("Old key can't be null!");
        if (oldKey.equals(newKey)) return;
        userNameEmailRepository.delete(oldKey);
        userNameEmailRepository.save(newKey, userId);
    }

    private void addNameEmailKey(User user) {
        if (user.getId() == null) throw new ForbiddenHttpException("Can't save name/email entity without id");
        isBlankNameEmailValidation(user);
        userNameEmailRepository.save(user.getEmail(), user.getId());
        userNameEmailRepository.save(user.getName(), user.getId());
    }

    private void isBlankNameEmailValidation(User user) {
        if (StringUtils.isBlank(user.getName()) || StringUtils.isBlank(user.getEmail())) {
            throw new ForbiddenHttpException("entity with name/email blank");
        }
    }
}
