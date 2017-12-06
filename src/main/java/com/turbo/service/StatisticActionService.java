package com.turbo.service;

import com.turbo.repository.elasticsearch.content.CommentSearchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatisticActionService {

    private final CommentSearchRepository commentSearchRepository;

    public void deleteComment(final Long id) {
        commentSearchRepository.delete(id);
    }
}
