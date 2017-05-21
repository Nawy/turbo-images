package com.turbo.repository.couchbase;

import com.couchbase.client.java.Bucket;
import com.turbo.model.Post;
import com.turbo.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * Created by ermolaev on 5/17/17.
 */
@Repository
public class CouchbasePostRepository extends AbstractCouchbaseRepository<Post> {

    private final Bucket bucket;

    @Autowired
    public CouchbasePostRepository(@Qualifier("post-bucket") Bucket bucket) {
        super(bucket, Post.class);
        this.bucket = bucket;
    }

    public Post addPost(final Post post) {
        //add post
        return post;
    }
}
