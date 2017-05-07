package com.turbo.repository;

import com.turbo.model.Image;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ermolaev on 5/7/17.
 */
public interface ImageRepository extends CrudRepository<Image, Long> {

    @Query("SELECT * FROM images WHERE id=?0")
    Image getImageById(Long id);

    @Query("SELECT * FROM images WHERE name LIKE ?0 OR description LIKE ?0")
    List<Image> findImage(String value);

    @Query("SELECT * FROM images WHERE name LIKE ?0")
    List<Image> findImageByName(String name);

    @Query("SELECT * FROM images WHERE description LIKE ?0")
    List<Image> findImageByDescription(String description);
}
