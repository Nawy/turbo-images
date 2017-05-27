package com.turbo.service;

import com.turbo.model.Post;
import com.turbo.model.page.Page;
import com.turbo.repository.couchbase.PostRepository;
import com.turbo.repository.elasticsearch.PostSearchRepository;
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

    private final PostSearchRepository postSearchRepository;
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostSearchRepository postSearchRepository, PostRepository postRepository) {
        this.postSearchRepository = postSearchRepository;
        this.postRepository = postRepository;
    }

    public Post addPost(final Post post) {
        final Post postWithId = postRepository.addPost(post);
        postSearchRepository.addPost(postWithId);
        return postWithId;
    }

    public Post getPostById(final String id) {
        Post post = postSearchRepository.getPostById(id);

        if (Objects.isNull(post)) {
            post = postRepository.get(id);
            postSearchRepository.addPost(post); //overhead
        }
        return postSearchRepository.getPostById(id);
    }

    //return not Paginator, because we shouldn't count all posts, just upload additional
    public List<Post> getLastPosts(Page page) {
        //FIXME elastic should be here!
        return Collections.emptyList();
    }

    public Post update(Post post) {
        //TODO update elastic here
        return postRepository.save(post);
    }

    public List<Post> getUserPosts(long userId){
        //TODO update elastic here
        return Collections.emptyList();
    }

    public void delete(String id) {
        postRepository.delete(id);
    }
}
