package com.turbo.service;

import com.turbo.model.Image;
import com.turbo.model.UserImage;
import com.turbo.model.converter.ImagePath;
import com.turbo.model.search.content.ImageSearchEntity;
import com.turbo.repository.ImageConverterRepository;
import com.turbo.repository.aerospike.ImageRepository;
import com.turbo.repository.aerospike.UserImageRepository;
import com.turbo.repository.elasticsearch.content.ImageSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by ermolaev on 5/7/17.
 */
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final UserImageRepository userImageRepository;
    private final ImageConverterRepository imageConverterRepository;
    private final ImageSearchRepository imageSearchRepository;

    @Autowired
    public ImageService(
            ImageRepository imageRepository,
            UserImageRepository userImageRepository,
            ImageConverterRepository imageConverterRepository,
            ImageSearchRepository imageSearchRepository
    ) {
        this.imageRepository = imageRepository;
        this.imageConverterRepository = imageConverterRepository;
        this.userImageRepository = userImageRepository;
        this.imageSearchRepository = imageSearchRepository;
    }

    public UserImage getUserImage(Long id) {
        return userImageRepository.get(id);
    }

    public boolean imageExists(Long hash) {
        return imageRepository.exists(hash);
    }

    public UserImage addImage(String username, byte[] picture) {
        ImagePath imagePath = imageConverterRepository.uploadImages(picture);
        UserImage image = new UserImage(
                0L,
                imagePath.getThumbnail(),
                imagePath.getImage(),
                "",
                "",
                username,
                LocalDateTime.now()
        );
        //TODO need add correct to aerospike and add correct id generation
        //add image to elastic search
        imageSearchRepository.addImage(image);
        return image;
    }

    private Image saveImage( byte[] picture) {
        ImagePath imagePath = imageConverterRepository.uploadImages(picture);
        return imageRepository.save(
                new Image(null, imagePath.getImage(), imagePath.getThumbnail())
        );
    }
}
