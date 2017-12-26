package com.turbo.service.statistic;

import com.turbo.model.Post;
import com.turbo.model.search.content.CommentSearchEntity;
import com.turbo.model.search.stat.PostStatEntity;
import com.turbo.model.search.stat.UpdatePostStatistic;
import com.turbo.model.statistic.ReindexCommentContent;
import com.turbo.model.statistic.ReindexPost;
import com.turbo.repository.elasticsearch.content.CommentSearchRepository;
import com.turbo.repository.elasticsearch.content.PostSearchRepository;
import com.turbo.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

@Service
@AllArgsConstructor
public class StatisticActionService {

    private final CommentSearchRepository commentSearchRepository;
    private final PostService postService;
    private final PostSearchRepository postSearchRepository;
    private final PostStatisticService postStatisticService;

    public void updateCommentContent(final ReindexCommentContent action) {
        final CommentSearchEntity comment = commentSearchRepository.get(action.getId());
        if(Objects.isNull(comment)) return;
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

    public void updatePostContent(final ReindexPost action) {
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

        long resultRating = post.getRating();
        long resultUps = post.getUps();
        long resultDowns = post.getDowns();

        for(Long value : action.getRatings()) {
            if(value > 0) {
                resultUps += value;
            } else {
                resultDowns += value;
            }
            resultRating += value;
        }

        post.setViews(post.getViews() + action.getViews());
        post.setRating(resultRating);
        post.setUps(resultUps);
        post.setDowns(resultDowns);

        postSearchRepository.upsertPost(post);
        postStatisticService.updateStaticPost(action, LocalDate.now());
    }
}
