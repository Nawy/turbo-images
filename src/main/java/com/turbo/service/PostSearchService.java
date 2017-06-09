package com.turbo.service;

import com.turbo.model.Post;
import com.turbo.model.search.content.PostContentEntity;
import com.turbo.model.search.content.PostSearchEntity;
import com.turbo.repository.elasticsearch.content.PostSearchRepository;
import org.springframework.stereotype.Service;

/**
 * Created by ermolaev on 6/2/17.
 */
@Service
public class PostSearchService {

    private final PostSearchRepository postSearchRepository;

    public PostSearchService(PostSearchRepository postSearchRepository) {
        this.postSearchRepository = postSearchRepository;
    }

    public void addPost(final Post post) {
        postSearchRepository.addPost(post);
    }

    public void updatePost(final Post post) {
       postSearchRepository.updatePost(post);
    }

    public Post getPostById(final Long id) {
        PostSearchEntity post = postSearchRepository.getPostById(id);

    }

}
