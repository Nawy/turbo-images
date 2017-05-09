package com.turbo.repository;

import com.turbo.model.ImageInfo;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ermolaev on 5/7/17.
 */
public interface ImageInfoRepository extends CrudRepository<ImageInfo, Long> {

    @Query("SELECT * FROM images WHERE id=?0")
    ImageInfo getImageById(Long id);

    @Query("SELECT * FROM images WHERE name LIKE ?0 OR description LIKE ?0")
    List<ImageInfo> findImage(String value);

    @Query("SELECT * FROM images WHERE name LIKE ?0")
    List<ImageInfo> findImageByName(String name);

    @Query("SELECT * FROM images WHERE description LIKE ?0")
    List<ImageInfo> findImageByDescription(String description);
}
