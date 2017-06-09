package com.turbo.service;

import com.turbo.model.Post;
import com.turbo.model.SearchSort;
import com.turbo.model.search.field.PostField;
import com.turbo.repository.elasticsearch.content.PostSearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Long> getPostsByUser(long userId, int page, SearchSort searchSort) {
        postSearchRepository.getPostByAuthor(userId,page, PostField.AUTHOR)
    }

}
