package com.turbo.repository;

import com.turbo.model.Post;
import org.springframework.stereotype.Repository;

/**
 * Created by ermolaev on 5/17/17.
 */
@Repository
public class CouchbaseRepository {

    public Post addPost(final Post post) {
        //add post
        return post;
    }
}
