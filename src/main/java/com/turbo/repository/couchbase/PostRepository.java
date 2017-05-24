package com.turbo.repository.couchbase;

import com.couchbase.client.java.Bucket;
import com.turbo.model.Post;
import com.turbo.repository.util.JsonParser;
import com.turbo.repository.util.UserIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * Created by ermolaev on 5/17/17.
 */
@Repository
public class PostRepository extends AbstractCouchbaseRepository<Post> {

    private final Bucket bucket;

    @Autowired
    public PostRepository(@Qualifier("post-bucket") Bucket bucket, JsonParser jsonParser) {
        super(bucket, jsonParser, Post.class);
        this.bucket = bucket;
    }

    public Post addPost(final Post post) {
        //add post
        return post;
    }

    @Override
    public String generateId(String userId) {
        return UserIdGenerator.generateCommentKey(userId);
    }

}
