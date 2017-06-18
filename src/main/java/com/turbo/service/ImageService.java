package com.turbo.service;

import com.turbo.model.Image;
import com.turbo.model.UserImage;
import com.turbo.model.converter.ImagePath;
import com.turbo.repository.ImageConverterRepository;
import com.turbo.repository.aerospike.ImageRepository;
import com.turbo.repository.aerospike.UserImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ermolaev on 5/7/17.
 */
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final UserImageRepository userImageRepository;
    private final ImageConverterRepository imageConverterRepository;

    @Autowired
    public ImageService(
            ImageRepository imageRepository,
            UserImageRepository userImageRepository,
            ImageConverterRepository imageConverterRepository
    ) {
        this.imageRepository = imageRepository;
        this.imageConverterRepository = imageConverterRepository;
        this.userImageRepository = userImageRepository;
    }

    public UserImage getUserImage(Long id) {
        return userImageRepository.get(id);
    }

    public boolean imageExists(Long hash) {
        return imageRepository.exists(hash);
    }

    public UserImage addImage(String username, long hash, byte[] picture) {
        Image image = imageExists(hash) ?
                imageRepository.get(hash) :
                saveImage(hash, picture);

        return userImageRepository.save(
                new UserImage(null, image, null, username)
        );
    }

    private Image saveImage(long hash, byte[] picture) {
        ImagePath imagePath = imageConverterRepository.uploadImages(picture);
        return imageRepository.save(
                new Image(hash, imagePath.getImage(), imagePath.getThumbnail())
        );
    }
}
