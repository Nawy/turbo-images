package com.turbo.service;

import com.turbo.model.Nullable;
import com.turbo.model.page.Paginator;
import com.turbo.model.search.entity.PostSearchEntity;
import com.turbo.repository.elasticsearch.PostSearchRepository;
import com.turbo.repository.elasticsearch.field.PostField;
import com.turbo.repository.elasticsearch.helper.SearchOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by ermolaev on 6/2/17.
 */
@Service
public class PostSearchService {

    private final PostSearchRepository postSearchRepository;

    @Autowired
    public PostSearchService(PostSearchRepository postSearchRepository) {
        this.postSearchRepository = postSearchRepository;
    }

    public <T> void addPost(T externalPost, Function<T, PostSearchEntity> mapFunction) {
        postSearchRepository.addPost(mapFunction.apply(externalPost));
    }

    public <T> void updatePost(T externalPost, Function<T, PostSearchEntity> mapFunction) {
        postSearchRepository.updatePost(mapFunction.apply(externalPost));
    }

    public <T> T getPostById(final Long id, Function<PostSearchEntity, T> mapFunction) {
        return mapFunction.apply(postSearchRepository.getPostById(id));
    }

    public <T> Paginator<T> getPostByAuthor(
            final String authorId,
            final int page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder,
            Function<PostSearchEntity, T> mapFunction
    ) {
        List<PostSearchEntity> results = postSearchRepository.getPostByAuthor(
                authorId,
                page,
                postField,
                searchOrder
        );
        return new Paginator<>(
                page,
                results
                        .stream()
                        .map(mapFunction)
                        .collect(Collectors.toList())
        );
    }

    public <T> Paginator<T> findPostByName(
            final String name,
            final int page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder,
            Function<PostSearchEntity, T> mapFunction
    ) {
        List<PostSearchEntity> results = postSearchRepository.findPostByName(
                name,
                page,
                postField,
                searchOrder
        );

        return new Paginator<>(
                page,
                results
                        .stream()
                        .map(mapFunction)
                        .collect(Collectors.toList())
        );
    }


    public <T> Paginator<T> findPostByDescription(
            final String description,
            final int page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder,
            Function<PostSearchEntity, T> mapFunction
    ) {
        List<PostSearchEntity> results = postSearchRepository.findPostByDescription(
                description,
                page,
                postField,
                searchOrder
        );

        return new Paginator<>(
                page,
                results
                        .stream()
                        .map(mapFunction)
                        .collect(Collectors.toList())
        );
    }

    public <T> Paginator<T> getPostsByDate(
            final LocalDate postDate,
            final int page,
            @Nullable final PostField postField,
            @Nullable final SearchOrder searchOrder,
            Function<PostSearchEntity, T> mapFunction
    ) {
        List<PostSearchEntity> results = postSearchRepository.getPostsByDate(
                postDate,
                page,
                postField,
                searchOrder
        );

        return new Paginator<>(
                page,
                results
                        .stream()
                        .map(mapFunction)
                        .collect(Collectors.toList())
        );
    }
}
