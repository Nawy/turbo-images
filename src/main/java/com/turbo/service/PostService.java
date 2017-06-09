package com.turbo.service;

import com.turbo.model.Post;
import com.turbo.model.SearchSort;
import com.turbo.model.page.Paginator;
import com.turbo.repository.aerospike.PostRepository;
import com.turbo.repository.elasticsearch.helper.SearchOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rakhmetov on 09.05.17.
 */
@Service
public class PostService {

    private final PostSearchService postSearchService;
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostSearchService postSearchService, PostRepository postRepository) {
        this.postSearchService = postSearchService;
        this.postRepository = postRepository;
    }

    public Post save(Post post) {
        return post.getId() == null ?
                update(post) :
                addPost(post);
    }

    private Post addPost(Post post) {
        Post postWithId = postRepository.save(post);
        postSearchService.addPost(postWithId);
        return postWithId;
    }

    private Post update(Post post) {
        postSearchService.updatePost(post);
        return postRepository.save(post);
    }

    public Post getPostById(final long id) {
        return postRepository.get(id);
    }

    public Paginator<Post> getMostViral(int page, SearchSort sort, SearchOrder searchOrder) {

    }

    public Paginator<Post> getUserPosts(int page, long userId, SearchSort sort) {

    }

    public void delete(long id) {
        postRepository.delete(id);
    }
}
