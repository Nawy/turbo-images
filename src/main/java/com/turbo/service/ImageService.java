package com.turbo.service;

import com.turbo.model.Image;
import com.turbo.model.UserImage;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.repository.aerospike.ImageRepository;
import com.turbo.repository.aerospike.UserImageRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by ermolaev on 5/7/17.
 */
@Service
public class ImageService {

    private final static int FILE_GENERATION_TRIES = 3;

    private final ImageRepository imageRepository;
    private final UserImageRepository userImageRepository;
    private final String imageStoragePath;

    @Autowired
    public ImageService(
            ImageRepository imageRepository,
            UserImageRepository userImageRepository,
            @Value("${image.storage.path}") String imageStoragePath
    ) {
        this.imageRepository = imageRepository;
        this.imageStoragePath = imageStoragePath;
        this.userImageRepository = userImageRepository;
    }

    public UserImage getUserImage(Long id) {
        return userImageRepository.get(id);
    }

    public boolean imageExists(Long hash) {
        return imageRepository.exists(hash);
    }

    public UserImage addImage(long userId, long hash, byte[] picture) {
        Image image = imageExists(hash) ?
                imageRepository.get(hash) :
                saveImage(hash, picture);

        return userImageRepository.save(
                new UserImage(null, image, null, userId)
        );
    }


    private Image saveImage(long hash, byte[] picture) {
        File fullSizeImageFile = saveImageFile(hash, picture);
        File smallImageFile = makeSmallImage(picture);

        return imageRepository.save(
                new Image(hash, fullSizeImageFile.getAbsolutePath(), smallImageFile.getAbsolutePath())
        );
    }

    private File saveImageFile(long hash, byte[] picture) {
        int tries = 0;
        File file;
        do {
            file = new File(imageStoragePath + File.separator + hash + File.separator + generateRandomName());
            tries++;
            if (FILE_GENERATION_TRIES <= tries) {
                throw new InternalServerErrorHttpException("Exhausted file unique name generations attempts");
            }
        } while (file.exists());

        try {
            FileUtils.writeByteArrayToFile(file, picture);
        } catch (IOException e) {
            throw new InternalServerErrorHttpException("Can't to new  write file");
        }
        return file;
    }

    public File makeSmallImage(byte[] picture) {
        long hash = Arrays.hashCode(picture);
        byte[] smallPicture = null;  //TODO MAKE SMALL SIZE PICTURE TOO
        return saveImageFile(hash, smallPicture);
    }

    private String generateRandomName() {
        return Long.toString(UUID.randomUUID().getMostSignificantBits()).substring(0, 10);
    }

}
