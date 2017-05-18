package com.turbo.service;

import com.turbo.model.Post;
import com.turbo.repository.CouchbaseRepository;
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
    private final CouchbaseRepository couchbaseRepository;

    @Autowired
    public PostService(ElasticsearchRepository elasticsearchRepository, CouchbaseRepository couchbaseRepository) {
        this.elasticsearchRepository = elasticsearchRepository;
        this.couchbaseRepository = couchbaseRepository;
    }


    public Post addPost(final Post post) {
        return elasticsearchRepository.addPost(post);
    }

    public Post getPostById(final long id) {
        return postRepository.findOne(id);
    }

    public Post getPostByElasticId(final String elasticId) {
        return null;
    }

    public List<Post> getLastPost() {
        return Collections.emptyList();
    }
}
