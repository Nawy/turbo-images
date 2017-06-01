package com.turbo.service;

import com.turbo.model.Post;
import com.turbo.model.SearchSort;
import com.turbo.model.page.Page;
import com.turbo.model.page.Paginator;
import com.turbo.repository.aerospike.PostRepository;
import com.turbo.repository.elasticsearch.PostSearchRepository;
import com.turbo.repository.elasticsearch.field.PostField;
import com.turbo.repository.elasticsearch.helper.SearchOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        final Post postWithId = postRepository.save(post);
        postSearchRepository.addPost(postWithId);
        return postWithId;
    }

    public Post getPostById(final long id) {
        Post post = postSearchRepository.getPostById(id);

        if (Objects.isNull(post)) {
            post = postRepository.get(id);
            postSearchRepository.addPost(post); //overhead
        }
        return postSearchRepository.getPostById(id);
    }

    public Paginator<Post> getMostViral(int page, SearchSort sort) {
        //fixme
        return postSearchRepository.getLastPosts(page, 0, PostField.RAITING, SearchOrder.DESC);
    }

    public Post update(Post post) {
        postSearchRepository.updatePost(post);
        return postRepository.save(post);
    }

    public Paginator<Post> getUserPosts(int page, long userId, SearchSort sort) {
        //FIXME
        return null;//postSearchRepository.getPostByAuthor(userId, page, postField, searchOrder);
    }

    public void delete(long id) {
        postRepository.delete(id);
    }
}
