package com.turbo.service;

import com.turbo.model.comment.Comment;
import com.turbo.model.statistic.ReindexAction;
import com.turbo.model.statistic.ReindexCommentContent;
import com.turbo.repository.elasticsearch.content.CommentSearchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class StatisticActionService {

    private final CommentSearchRepository commentSearchRepository;

    public void updateCommentContent(final ReindexAction action) {
        ReindexCommentContent actionResult = action.getOriginalValue();
        final Comment comment = commentSearchRepository.get(actionResult.getId());

        if(Objects.isNull(comment)) {
            return;
        }

        comment.setContent(actionResult.getContent());
        commentSearchRepository.update(comment);
    }

    public void deleteComment(final Long id) {
        commentSearchRepository.delete(id);
    }

    public void deletePost(final Long id) {
        commentSearchRepository.delete(id);
    }

    public void deleteImage(final Long id) {
        commentSearchRepository.delete(id);
    }
}
