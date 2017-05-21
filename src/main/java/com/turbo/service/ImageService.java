package com.turbo.service;

import com.turbo.model.Image;
import com.turbo.repository.aerospike.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ermolaev on 5/7/17.
 */
@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image getImage(long hash) {
        return imageRepository.get(hash);
    }

    public boolean imageExists(long hash) {
        return imageRepository.exists(hash);
    }

    public Image addImage(long hash, byte[] picture) {
        // TODO NO PATH HERE!!!
        return imageRepository.save(new Image(hash, null));
    }
}
