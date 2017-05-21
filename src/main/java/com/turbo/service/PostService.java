package com.turbo.service;

import com.turbo.model.Post;
import com.turbo.repository.couchbase.CouchbasePostRepository;
import com.turbo.repository.ElasticsearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created by rakhmetov on 09.05.17.
 */
@Service
public class PostService {

    private final ElasticsearchRepository elasticsearchRepository;
    private final CouchbasePostRepository couchbasePostRepository;

    @Autowired
    public PostService(ElasticsearchRepository elasticsearchRepository, CouchbasePostRepository couchbasePostRepository) {
        this.elasticsearchRepository = elasticsearchRepository;
        this.couchbasePostRepository = couchbasePostRepository;
    }


    public Post addPost(final Post post) {
        final Post postWithId = couchbasePostRepository.addPost(post);
        elasticsearchRepository.addPost(postWithId);
        return postWithId;
    }

    public Post getPostById(final long id) {
        return null;
    }

    public Post getPostByElasticId(final String elasticId) {
        return null;
    }

    public List<Post> getLastPost() {
        return Collections.emptyList();
    }
}
