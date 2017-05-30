package com.turbo.service;

import com.turbo.model.Post;
import com.turbo.model.page.Page;
import com.turbo.model.page.Paginator;
import com.turbo.repository.couchbase.PostRepository;
import com.turbo.repository.elasticsearch.PostSearchRepository;
import com.turbo.repository.elasticsearch.field.PostField;
import com.turbo.repository.elasticsearch.helper.SearchOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Paginator<Post> getLastPosts(Page page, int lastDays) {
        //TODO scylla ?
        return postSearchRepository.getLastPosts(page, lastDays, PostField.RAITING, SearchOrder.DESC);
    }

    public Post update(Post post) {
        postSearchRepository.updatePost(post);
        return postRepository.save(post);
    }

    public Paginator<Post> getUserPosts(Page page, String userId, PostField postField, SearchOrder searchOrder) {
       return postSearchRepository.getPostByAuthor(userId, page, postField, searchOrder);
    }

    public void delete(String id) {
        postRepository.delete(id);
    }
}
