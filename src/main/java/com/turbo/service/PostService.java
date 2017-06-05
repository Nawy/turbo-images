package com.turbo.service;

import com.turbo.model.Post;
import com.turbo.model.SearchSort;
import com.turbo.model.page.Paginator;
import com.turbo.model.search.content.PostContentEntity;
import com.turbo.repository.aerospike.PostRepository;
import com.turbo.repository.elasticsearch.field.PostField;
import com.turbo.repository.elasticsearch.helper.SearchOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Function;

/**
 * Created by rakhmetov on 09.05.17.
 */
@Service
public class PostService {

    private final PostSearchService postSearchService;
    private final PostRepository postRepository;

    private final Function<Post, PostContentEntity> mapPostToSearch;
    private final Function<PostContentEntity, Post> mapSearchToPost;

    @Autowired
    public PostService(PostSearchService postSearchService, PostRepository postRepository) {
        this.postSearchService = postSearchService;
        this.postRepository = postRepository;

        this.mapPostToSearch = p -> new PostContentEntity(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getUps(),
                p.getDowns(),
                p.getViewCount(),
                p.getPreviewPath(),
                p.getDeviceType(),
                p.getTags(),
                p.getAuthorId(),
                p.getCreateDate(),
                p.isVisible()
        );

        this.mapSearchToPost = p -> new Post(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getUps(),
                p.getDowns(),
                p.getViewCount(),
                p.getPreviewPath(),
                null,
                p.getDeviceType(),
                p.getTags(),
                p.getAuthorId(),
                p.getCreateDate(),
                p.isVisible()
        );
    }

    public Post addPost(final Post post) {
        final Post postWithId = postRepository.save(post);
        postSearchService.addPost(postWithId, mapPostToSearch);
        return postWithId;
    }

    public Post getPostById(final long id) {
        Post post = postSearchService.getPostById(id, mapSearchToPost);

        if (Objects.isNull(post)) {
            post = postRepository.get(id);
            postSearchService.addPost(post, mapPostToSearch); //overhead
        }
        return postSearchService.getPostById(id, mapSearchToPost);
    }

    public Paginator<Post> getMostViral(int page, SearchSort sort) {
        //fixme
        return postSearchService.getPostsByDate(
                LocalDate.now(),
                page,
                PostField.RAITING,
                SearchOrder.DESC,
                mapSearchToPost
        );
    }

    public Post update(Post post) {
        postSearchService.updatePost(post, mapPostToSearch);
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
