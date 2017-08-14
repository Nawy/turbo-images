package com.turbo.service;

import com.turbo.model.Image;
import com.turbo.model.UserImage;
import com.turbo.model.aerospike.UserImageContent;
import com.turbo.model.exception.NotFoundHttpException;
import com.turbo.model.search.content.ImageSearchEntity;
import com.turbo.repository.aerospike.collection.UserImageCollectionRepository;
import com.turbo.repository.aerospike.user.UserImageRepository;
import com.turbo.repository.elasticsearch.content.UserImageSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by rakhmetov on 26.06.17.
 */
@Service
public class UserImageService {

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

    //TODO paged request?
    public List<UserImage> getUserImages(long userId) {
        List<Long> userImageIds = userImageSearchRepository.getUserImages(userId);
        if(CollectionUtils.isEmpty(userImageIds)) {
            userImageIds = userImageCollectionRepository.get(userId);
        }
        return getUserImages(userImageIds);
    }

//    //TODO paged request?
//    public List<UserImage> getUserImages(long userId) {
//        List<ImageSearchEntity> userImages = userImageSearchRepository.getUserImages(userId);
//        return userImages.stream().map(
//                tone -> new UserImage(
//                        tone.getId(),
//                        new Image(
//                                11L,
//                                tone.getSourcePath(),
//                                tone.getThumbnailPath()
//                        ),
//                        tone.getUserId(),
//                        tone.getDescription(),
//                        tone.getCreateDate()
//                )
//        ).collect(Collectors.toList());
//    }

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

    public List<UserImage> getUserImages(List<Long> userImageIds) {
        final List<UserImageContent> userImageContents = userImageRepository.bulkGet(userImageIds);

        final List<Long> imageIds = userImageContents.stream()
                .map(UserImageContent::getImageId)
                .collect(Collectors.toList());

        final List<Image> images = imageService.getImages(imageIds);
        final Map<Long, Image> imageMap = images.stream().collect(Collectors.toMap(Image::getId, Function.identity()));

        return userImageContents.stream()
                .map(userImage -> makeUserImage(userImage, imageMap.get(userImage.getImageId())))
                .collect(Collectors.toList());
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
                        userImageContent.getUserId(),
                        description,
                        userImageContent.getCreateDate()
                )
        );

        userImageSearchRepository.editUserImage(
                makeUserImage(updatedUserImage)
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
                userImageContent.getUserId(),
                userImageContent.getDescription(),
                userImageContent.getCreateDate()
        );
    }

    private UserImage makeUserImage(UserImageContent userImageContent, Image image) {
        return new UserImage(
                userImageContent.getId(),
                image,
                userImageContent.getUserId(),
                userImageContent.getDescription(),
                userImageContent.getCreateDate()
        );
    }
}
