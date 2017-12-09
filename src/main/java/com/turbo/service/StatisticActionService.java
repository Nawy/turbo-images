package com.turbo.service;

import com.turbo.model.Post;
import com.turbo.model.comment.Comment;
import com.turbo.model.statistic.ReindexAction;
import com.turbo.model.statistic.ReindexCommentContent;
import com.turbo.model.statistic.ReindexPostContent;
import com.turbo.repository.elasticsearch.content.CommentSearchRepository;
import com.turbo.repository.elasticsearch.content.PostSearchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class StatisticActionService {

    private final CommentSearchRepository commentSearchRepository;
    private final PostService postService;
    private final PostSearchRepository postSearchRepository;

    public void updateCommentContent(final ReindexCommentContent action) {
        final Comment comment = commentSearchRepository.get(action.getId());

        if(Objects.isNull(comment)) {
            return;
        }

        comment.setContent(action.getContent());
        commentSearchRepository.update(comment);
    }

    public void deleteComment(final Long id) {
        commentSearchRepository.delete(id);
    }

    public void deletePost(final Long id) {
        //TODO
    }

    public void deleteImage(final Long id) {
        // TODO
    }

    public void updatePostContent(final ReindexPostContent action) {
        final Post post = postService.getPostById(action.getId());

        if(Objects.isNull(post)) {
            return;
        }

        if(Objects.nonNull(action.getName())) {
            post.setName(action.getName());
        }

        if(Objects.nonNull(action.getDescription())) {
            post.setDescription(action.getDescription());
        }

        postSearchRepository.upsertPost(post);
    }
}
