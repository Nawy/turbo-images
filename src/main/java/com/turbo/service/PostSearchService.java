package com.turbo.service;

import com.turbo.model.Post;
import com.turbo.model.search.content.PostContentEntity;
import com.turbo.model.search.content.PostSearchEntity;
import com.turbo.repository.elasticsearch.content.PostContentRepository;
import org.springframework.stereotype.Service;

/**
 * Created by ermolaev on 6/2/17.
 */
@Service
public class PostSearchService {

    private final PostContentRepository postContentRepository;

    public PostSearchService(PostContentRepository postContentRepository) {
        this.postContentRepository = postContentRepository;
    }

    public void addPost(final Post post) {
        postContentRepository.addPost(post);
    }

    public void updatePost(final Post post) {
       postContentRepository.updatePost(post);
    }

    public Post getPostById(final Long id) {
        PostSearchEntity post = postContentRepository.getPostById(id);

    }

}
