package com.turbo.repository;

import com.turbo.model.Post;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ermolaev on 5/7/17.
 */
public interface PostRepository extends CrudRepository<Post, Long> {

    @Query("SELECT * FROM post WHERE name LIKE ?0 OR description LIKE ?0")
    Iterable<Post> findPost(String value);

    @Query("SELECT * FROM post WHERE name LIKE ?0")
    Iterable<Post> findPostByName(String name);

    @Query("SELECT * FROM post WHERE description LIKE ?0")
    Iterable<Post> findPostByDescription(String description);


}
