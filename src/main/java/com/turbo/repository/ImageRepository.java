package com.turbo.repository;

import com.turbo.model.Image;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ermolaev on 5/9/17.
 */
public interface ImageRepository extends CrudRepository<Image, Long> {

}
