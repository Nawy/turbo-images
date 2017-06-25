package com.turbo.service;

import com.turbo.model.Image;
import com.turbo.model.UserImage;
import com.turbo.model.aerospike.UserImageContent;
import com.turbo.model.exception.NotFoundHttpException;
import com.turbo.repository.aerospike.UserImageRepository;
import com.turbo.repository.elasticsearch.content.UserImageSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Created by rakhmetov on 26.06.17.
 */
@Service
public class UserImageService {

    private final UserImageRepository userImageRepository;
    private final UserImageSearchRepository userImageSearchRepository;
    private final ImageService imageService;

    @Autowired
    public UserImageService(UserImageRepository userImageRepository, UserImageSearchRepository userImageSearchRepository, ImageService imageService) {
        this.userImageRepository = userImageRepository;
        this.userImageSearchRepository = userImageSearchRepository;
        this.imageService = imageService;
    }

    public UserImage addUserImage(String username, byte[] picture) {
        //save image to files
        Image image = imageService.saveImage(picture);
        //save user image to content repo
        UserImage userImage = saveUserImage(
                new UserImage(
                        image,
                        username,
                        null,
                        LocalDateTime.now()
                )
        );
        //add image to elastic search
        userImageSearchRepository.addUserImage(userImage);
        return userImage;
    }

    public UserImage getUserImage(Long id) {
        return makeUserImage(
                getUserImageContent(id)
        );
    }

    private UserImage saveUserImage(UserImage userImage) {
        return makeUserImage(
                userImageRepository.save(new UserImageContent(userImage))
        );
    }

    public UserImage editUserImageDescription(long userImageId, String description) {
        UserImageContent userImageContent = getUserImageContent(userImageId);
        UserImageContent updatedUserImage = userImageRepository.save(
                new UserImageContent(
                        userImageContent.getId(),
                        userImageContent.getUsername(),
                        description,
                        userImageContent.getCreateDate()
                )
        );
        return makeUserImage(updatedUserImage);
    }

    /**
     * get Repo userImage entity
     */
    private UserImageContent getUserImageContent(long id) {
        UserImageContent userImageContent = userImageRepository.get(id);
        if (userImageContent == null) throw new NotFoundHttpException("No user image was fund for id:" + id);
        return userImageContent;
    }

    /**
     * Create from 2 repo one business entity UserImage
     *
     * @param userImageContent repository entity
     * @return UserImage
     */
    private UserImage makeUserImage(UserImageContent userImageContent) {
        Image image = imageService.getImage(userImageContent.getImageId());
        return new UserImage(
                userImageContent.getId(),
                image,
                userImageContent.getUsername(),
                userImageContent.getDescription(),
                userImageContent.getCreateDate()
        );
    }
}
