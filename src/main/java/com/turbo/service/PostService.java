package com.turbo.service;

import com.turbo.model.Post;
import com.turbo.repository.couchbase.PostRepository;
import com.turbo.repository.elasticsearch.PostElasticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by rakhmetov on 09.05.17.
 */
@Service
public class PostService {

    private final PostElasticRepository postElasticRepository;
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostElasticRepository postElasticRepository, PostRepository postRepository) {
        this.postElasticRepository = postElasticRepository;
        this.postRepository = postRepository;
    }


    public Post addPost(final Post post) {
        final Post postWithId = postRepository.addPost(post);
        postElasticRepository.addPost(postWithId);
        return postWithId;
    }

    public Post getPostById(final String id) {
        Post post = postElasticRepository.getPostById(id);

        if(Objects.isNull(post)) {
            // TODO request post from couchbase by id
            // post = postRepository.get(1); MOCK
            postElasticRepository.addPost(post);
        }
        return postElasticRepository.getPostById(id);
    }

    public List<Post> getLastPost() {
        return Collections.emptyList();
    }
}
