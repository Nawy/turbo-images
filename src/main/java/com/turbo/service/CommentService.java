package com.turbo.service;

import com.turbo.model.User;
import com.turbo.model.aerospike.CommentRepoModel;
import com.turbo.model.comment.Comment;
import com.turbo.model.comment.CommentReplyType;
import com.turbo.model.exception.InternalServerErrorHttpException;
import com.turbo.model.exception.NotFoundHttpException;
import com.turbo.model.search.SearchOrder;
import com.turbo.model.search.SearchSort;
import com.turbo.model.search.field.CommentField;
import com.turbo.repository.aerospike.CommentRepository;
import com.turbo.repository.elasticsearch.content.CommentSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final static int PAGE_SIZE = 50;

    private final CommentRepository commentRepository;
    private final CommentSearchRepository commentSearchRepository;
    private final UserService userService;

    public Comment save(CommentRepoModel repoModel) {
        return repoModel.getId() != null ?
                update(repoModel) :
                add(repoModel);
    }

    private Comment add(CommentRepoModel repoModel) {
        CommentRepoModel commentWithId = commentRepository.save(repoModel);
        Comment comment = get(commentWithId.getId());
        commentSearchRepository.add(comment);
        return comment;
    }

    private Comment update(CommentRepoModel repoModel) {
        CommentRepoModel postWithId = commentRepository.save(repoModel);
        Comment updatedPost = get(postWithId.getId());
        //FIXME i can update too long
        commentSearchRepository.update(updatedPost);
        return updatedPost;
    }

    public Comment get(final long id) {
        CommentRepoModel commentRepoModel = commentRepository.get(id);
        if (commentRepoModel == null) throw new NotFoundHttpException("can't find comment with such id");
        return makeComment(commentRepoModel);
    }

    public List<Comment> getByReply(
            long replyId,
            CommentReplyType replyType,
            int pageSize,
            SearchSort sort,
            SearchOrder searchOrder
    ) {
        CommentField commentField;
        switch (sort) {
            case DATE:
                commentField = CommentField.CREATION_DATE;
                break;
            case RATING:
                commentField = CommentField.RATING;
                break;
            case VIEWS:
            default:
                throw new InternalServerErrorHttpException(" unknown sorting field");
        }
        List<Long> commentIds = commentSearchRepository.getByReply(replyId, replyType, pageSize, commentField, searchOrder);
        return get(commentIds);
    }

    public List<Comment> get(final List<Long> ids) {
        if (ids.isEmpty()) return Collections.emptyList();
        List<CommentRepoModel> comments = commentRepository.bulkGet(ids);
        if (comments.isEmpty()) return Collections.emptyList();
        Set<Long> userIds = comments.stream().map(CommentRepoModel::getUserId).collect(Collectors.toSet());
        List<User> users = userService.bulkGet(userIds);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
        return comments.stream()
                .map(commentRepoModel ->
                        makeComment(
                                commentRepoModel,
                                userMap.get(commentRepoModel.getUserId())
                        )
                ).collect(Collectors.toList());
    }

    private Comment makeComment(CommentRepoModel commentRepoModel) {
        User user = userService.get(commentRepoModel.getUserId());
        return makeComment(commentRepoModel, user);
    }

    private Comment makeComment(CommentRepoModel commentRepoModel, User user) {
        return new Comment(
                commentRepoModel.getId(),
                user,
                commentRepoModel.getReplyId(),
                commentRepoModel.getReplyType(),
                commentRepoModel.getDevice(),
                commentRepoModel.getContent(),
                commentRepoModel.getCreationDate(),
                commentRepoModel.getUps(),
                commentRepoModel.getDowns(),
                commentRepoModel.getRating()
        );
    }

    public void delete(long commentId) {
        commentRepository.delete(commentId);
        //TODO add Elastic remove entity
    }
}
