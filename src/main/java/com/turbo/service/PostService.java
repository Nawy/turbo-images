package com.turbo.service;

import com.turbo.model.Post;
import com.turbo.model.SearchSort;
import com.turbo.model.page.Paginator;
import com.turbo.repository.aerospike.PostRepository;
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
//        postSearchService.addPost(postWithId, mapPostToSearch);
        return postWithId;
    }

    private Post update(Post post) {
//        postSearchService.updatePost(post, mapPostToSearch);
        return postRepository.save(post);
    }

    public Post getPostById(final long id) {
//        Post post = postSearchService.getPostById(id, mapSearchToPost);
//
//        if (Objects.isNull(post)) {
//            post = postRepository.get(id);
//            postSearchService.addPost(post, mapPostToSearch); //overhead
//        }
//        return postSearchService.getPostById(id, mapSearchToPost);
        return null;
    }

    public Paginator<Post> getMostViral(int page, SearchSort sort) {
//        //fixme
//        return postSearchService.getPostsByDate(
//                LocalDate.now(),
//                page,
//                PostField.RAITING,
//                SearchOrder.DESC,
//                mapSearchToPost
//        );
        return null;
    }

    public Paginator<Post> getUserPosts(int page, long userId, SearchSort sort) {
        //FIXME
        return null;//postSearchRepository.getPostByAuthor(userId, page, postField, searchOrder);
    }

    public void delete(long id) {
        postRepository.delete(id);
    }
}
