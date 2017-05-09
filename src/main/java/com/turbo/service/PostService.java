package com.turbo.service;

import com.turbo.model.Post;
import com.turbo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rakhmetov on 09.05.17.
 */
@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post addPost(Post post) {
        return postRepository.save(post);
    }

    public Post getPost(long id) {
        return postRepository.findOne(id);
    }

}
