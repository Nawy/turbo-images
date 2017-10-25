package com.turbo.service;

import com.turbo.model.Image;
import com.turbo.model.UserImage;
import com.turbo.model.aerospike.UserImageRepoModel;
import com.turbo.model.exception.NotFoundHttpException;
import com.turbo.repository.aerospike.collection.UserImageCollectionRepository;
import com.turbo.repository.aerospike.user.UserImageRepository;
import com.turbo.repository.elasticsearch.content.UserImageSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by rakhmetov on 26.06.17.
 */
@Service
public class UserImageService {

    private static final int PAGE_SIZE = 30;

    private final UserImageRepository userImageRepository;
    private final UserImageSearchRepository userImageSearchRepository;
    private final ImageService imageService;
    private final UserImageCollectionRepository userImageCollectionRepository;

    @Autowired
    public UserImageService(
            UserImageRepository userImageRepository,
            UserImageSearchRepository userImageSearchRepository,
            ImageService imageService,
            UserImageCollectionRepository userImageCollectionRepository
    ) {
        this.userImageRepository = userImageRepository;
        this.userImageSearchRepository = userImageSearchRepository;
        this.imageService = imageService;
        this.userImageCollectionRepository = userImageCollectionRepository;
    }

    public void removeUserImage(long userId, long userImageId) {
        userImageCollectionRepository.remove(userId, Collections.singletonList(userImageId));
        userImageRepository.delete(userImageId);
        userImageSearchRepository.delete(userImageId);
    }

    public UserImage addUserImage(long userId, byte[] picture) {
        //save image to files
        Image image = imageService.saveImage(picture);
        //save user image to content repo
        UserImage userImage = saveUserImage(
                new UserImage(
                        image,
                        userId,
                        null,
                        null,
                        LocalDateTime.now()
                )
        );
        //add to UserImageCollection
        userImageCollectionRepository.add(userId, Collections.singletonList(userImage.getId()));
        //add image to elastic search
        userImageSearchRepository.addUserImage(userImage);
        return userImage;
    }

    public UserImage getUserImage(Long id) {
        return makeUserImage(
                getUserImageContent(id)
        );
    }

    public List<UserImage> getCurrentUserImages(final long userId, final LocalDateTime dateTime) {
        final List<Long> userImageIds = userImageSearchRepository.getUserImages(userId, dateTime, PAGE_SIZE);
        return getUserImages(userImageIds);
    }

    public List<UserImage> getUserImages(Collection<Long> userImageIds) {
        if (userImageIds.isEmpty()) throw new NotFoundHttpException("No user images found");

        final List<UserImageRepoModel> userImageRepoModels = userImageRepository.bulkGet(new HashSet<>(userImageIds));

        final List<Long> imageIds = userImageRepoModels.stream()
                .map(UserImageRepoModel::getImageId)
                .collect(Collectors.toList());

        final List<Image> images = imageService.getImages(imageIds);
        final Map<Long, Image> imageMap = images.stream().collect(Collectors.toMap(Image::getId, Function.identity()));

        return userImageRepoModels.stream()
                .map(userImage -> makeUserImage(userImage, imageMap.get(userImage.getImageId())))
                .collect(Collectors.toList());
    }

    private UserImage saveUserImage(UserImage userImage) {
        return makeUserImage(
                userImageRepository.save(new UserImageRepoModel(userImage))
        );
    }

    public UserImage editUserImageDescription(long userImageId, String description) {
        UserImageRepoModel userImageRepoModel = getUserImageContent(userImageId);
        UserImageRepoModel updatedUserImage = userImageRepository.save(
                new UserImageRepoModel(
                        userImageRepoModel.getId(),
                        userImageRepoModel.getImageId(),
                        userImageRepoModel.getUserId(),
                        userImageRepoModel.getName(),
                        description,
                        userImageRepoModel.getCreateDate()
                )
        );

        UserImage userImage = makeUserImage(updatedUserImage);
        userImageSearchRepository.updateUserImage(userImage);
        return userImage;
    }

    public UserImage editUserImageName(long userImageId, String name) {
        UserImageRepoModel userImageRepoModel = getUserImageContent(userImageId);
        UserImageRepoModel updatedUserImage = userImageRepository.save(
                new UserImageRepoModel(
                        userImageRepoModel.getId(),
                        userImageRepoModel.getImageId(),
                        userImageRepoModel.getUserId(),
                        name,
                        userImageRepoModel.getDescription(),
                        userImageRepoModel.getCreateDate()
                )
        );

        UserImage userImage = makeUserImage(updatedUserImage);
        userImageSearchRepository.updateUserImage(userImage);
        return userImage;
    }

    /**
     * get Repo userImage entity
     */
    private UserImageRepoModel getUserImageContent(long id) {
        UserImageRepoModel userImageRepoModel = userImageRepository.get(id);
        if (userImageRepoModel == null) throw new NotFoundHttpException("No user image was fund for id:" + id);
        return userImageRepoModel;
    }

    /**
     * Create from 2 repo one business entity UserImage
     *
     * @param userImageRepoModel repository entity
     * @return UserImage
     */
    private UserImage makeUserImage(UserImageRepoModel userImageRepoModel) {
        Image image = imageService.getImage(userImageRepoModel.getImageId());
        return makeUserImage(userImageRepoModel, image);
    }

    private UserImage makeUserImage(UserImageRepoModel userImageRepoModel, Image image) {
        return new UserImage(
                userImageRepoModel.getId(),
                image,
                userImageRepoModel.getUserId(),
                userImageRepoModel.getName(),
                userImageRepoModel.getDescription(),
                userImageRepoModel.getCreateDate()
        );
    }
}
