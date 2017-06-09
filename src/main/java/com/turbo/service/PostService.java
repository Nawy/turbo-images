package com.turbo.service;

import com.turbo.model.Post;
import com.turbo.model.SearchPeriod;
import com.turbo.model.SearchSort;
import com.turbo.model.page.Paginator;
import com.turbo.model.search.field.PostField;
import com.turbo.repository.aerospike.PostRepository;
import com.turbo.repository.elasticsearch.content.PostSearchRepository;
import com.turbo.repository.elasticsearch.helper.SearchOrder;
import com.turbo.repository.elasticsearch.stat.PostStatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Created by rakhmetov on 09.05.17.
 */
@Service
public class PostService {

    private final PostSearchRepository postSearchRepository;
    private final PostStatRepository postStatRepository;
    private final PostRepository postRepository;

    @Autowired
    public PostService(
            PostSearchRepository postSearchRepository,
            PostRepository postRepository,
            PostStatRepository postStatRepository
    ) {
        this.postSearchRepository = postSearchRepository;
        this.postStatRepository = postStatRepository;
        this.postRepository = postRepository;
    }

    public Post save(Post post) {
        return post.getId() == null ?
                update(post) :
                addPost(post);
    }

    private Post addPost(Post post) {
        Post postWithId = postRepository.save(post);
        postSearchRepository.addPost(postWithId);
        return postWithId;
    }

    private Post update(Post post) {
        postSearchRepository.updatePost(post);
        return postRepository.save(post);
    }

    public Post getPostById(final long id) {
        return postRepository.get(id);
    }

    public Paginator<Post> getMostViral(int page, SearchPeriod period, SearchSort sort) {
        switch (period) {
            case DAY: break;
            case WEEK: break;
            case MONTH: break;
            case YEAR: break;
        }
        LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1);

    }

    public Paginator<Post> getUserPosts(int page, long userId, SearchSort sort) {

    }

    public void delete(long id) {
        postRepository.delete(id);
    }
}
