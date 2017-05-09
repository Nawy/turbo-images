package com.turbo.service;

import com.turbo.model.ImageInfo;
import com.turbo.model.UserInfo;
import com.turbo.repository.ImageInfoRepository;
import com.turbo.repository.ImageSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ermolaev on 5/7/17.
 */
@Service
public class ImageService {

    private final ImageInfoRepository imageInfoRepository;
    private final ImageSourceRepository imageSourceRepository;

    @Autowired
    public ImageService(ImageInfoRepository imageInfoRepository, ImageSourceRepository imageSourceRepository) {
        this.imageInfoRepository = imageInfoRepository;
        this.imageSourceRepository = imageSourceRepository;
    }

    public ImageInfo getImageInfo(final long imageId) {
        return imageInfoRepository.getImageById(imageId);
    }

    public Boolean checkImageExists(final String hash) {
        return imageSourceRepository.exists(hash);
    }

    public Integer addImageInfo(final ImageInfo imageInfo) {
        return 0;
    }

    public void addImageSource(final String hash, final byte[] source) {

    }
}
