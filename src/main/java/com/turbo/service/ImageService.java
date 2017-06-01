package com.turbo.service;

import com.turbo.model.Image;
import com.turbo.model.exception.http.InternalServerErrorHttpException;
import com.turbo.repository.aerospike.ImageRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by ermolaev on 5/7/17.
 */
@Service
public class ImageService {

    private final static int FILE_GENERATION_TRIES = 3;

    private final ImageRepository imageRepository;
    private final String imageStoragePath;

    @Autowired
    public ImageService(ImageRepository imageRepository,
                        @Value("${image.storage.path}") String imageStoragePath
    ) {
        this.imageRepository = imageRepository;
        this.imageStoragePath = imageStoragePath;
    }

    public Image getImage(Long hash) {
        return imageRepository.get(hash);
    }

    public boolean imageExists(Long hash) {
        return imageRepository.exists(hash);
    }

    public Image addImage(long hash, byte[] picture) {
        int tries = 0;
        File file;
        do {
             file = new File(imageStoragePath + File.separator + hash + File.separator + generateRandomName());
             tries++;
             if (FILE_GENERATION_TRIES <= tries){
                 throw new InternalServerErrorHttpException("Exhausted file unique name generations attempts");
             }
        } while (file.exists());

        try {
            FileUtils.writeByteArrayToFile(file, picture);
        } catch (IOException e) {
            throw new InternalServerErrorHttpException("Can't to new  write file");
        }
        return imageRepository.save(new Image(hash, file.getAbsolutePath()));
    }

    private String generateRandomName(){
      return Long.toString(UUID.randomUUID().getMostSignificantBits()).substring(0,10);
    }

}
