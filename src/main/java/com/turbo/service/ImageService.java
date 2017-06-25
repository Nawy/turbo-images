package com.turbo.service;

import com.turbo.model.Image;
import com.turbo.model.exception.NotFoundHttpException;
import com.turbo.repository.ImageConverterRepository;
import com.turbo.repository.aerospike.ImageRepository;
import org.springframework.stereotype.Service;

/**
 * Created by ermolaev on 5/7/17.
 */
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageConverterRepository imageConverterRepository;

    public ImageService(ImageRepository imageRepository, ImageConverterRepository imageConverterRepository) {
        this.imageRepository = imageRepository;
        this.imageConverterRepository = imageConverterRepository;
    }

    public Image getImage(Long id) {
        Image image = imageRepository.get(id);
        if (image == null) throw new NotFoundHttpException("No image was found for id:" + id);
        return image;
    }

    public Image saveImage(byte[] picture) {
        Image image = imageConverterRepository.uploadImages(picture);
        return imageRepository.save(image);
    }
}
