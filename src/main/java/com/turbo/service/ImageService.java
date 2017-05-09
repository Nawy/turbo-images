package com.turbo.service;

import com.turbo.model.Image;
import com.turbo.model.Post;
import com.turbo.repository.ImageRepository;
import com.turbo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ermolaev on 5/7/17.
 */
@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(PostRepository postRepository, ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image getImage(long hash) {
        return imageRepository.findOne(hash);
    }

    public boolean imageExists(long hash) {
        return imageRepository.exists(hash);
    }

    //FIXME
    public long addImage(Post imageInfo) {
        return 0;
    }

    public void addImageSource(long hash, byte[] source) {

    }
}
