package com.turbo.service;

import com.turbo.model.Post;
import com.turbo.model.page.Page;
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

        if (Objects.isNull(post)) {
            post = postRepository.get(id);
            postElasticRepository.addPost(post); //overhead
        }
        return postElasticRepository.getPostById(id);
    }

    //return not Paginator, because we shouldn't count all posts, just upload additional
    public List<Post> getLastPost(Page page) {
        //FIXME elastic should be here!
        return Collections.emptyList();
    }

    public Post update(Post post) {
        //TODO update elastic here
        return postRepository.save(post);
    }

    public void delete(String id) {
        postRepository.delete(id);
    }
}
