package com.turbo;

import com.turbo.model.DeviceType;
import com.turbo.model.Rating;
import com.turbo.model.RatingStatus;
import com.turbo.model.aerospike.CommentRepoModel;
import com.turbo.model.aerospike.PostRepoModel;
import com.turbo.service.CommentService;
import com.turbo.service.PostService;
import com.turbo.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Created by ermolaev on 5/28/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class CommentRatingTest {

    @Mock
    private PostService postService;
    @Mock
    private UserService userService;

    @InjectMocks
    private CommentService commentService;

    private final static long USER_ID = 1000;
    private final static long POST_ID = 100;
    private final static long COMMENT_ID = 200;
    private final static String NO_VALUE_STRING = "TEST";

    private CommentRepoModel comment;
    private PostRepoModel post;

    @Before
    public void setup() {
        comment = new CommentRepoModel(
                COMMENT_ID,
                USER_ID,
                null,
                DeviceType.UNKNOWN,
                NO_VALUE_STRING,
                null,
                new Rating(),
                false,
                new HashMap<>()
        );
        post = new PostRepoModel(
                POST_ID,
                NO_VALUE_STRING,
                null,
                0,
                null,
                null,
                null,
                USER_ID,
                null,
                true,
                NO_VALUE_STRING,
                Collections.singletonMap(COMMENT_ID, comment)
        );
        when(postService.getRawPost(anyLong())).thenReturn(post);
    }

    @Test
    public void haveRatingUpAndRecieveUp() {
        Rating rating = comment.getRating();
        rating.changeRating(true);
        comment.getRatingHistory().put(USER_ID, RatingStatus.UP);

        commentService.changeCommentRating(USER_ID, POST_ID, COMMENT_ID, RatingStatus.UP);

        assertThat(rating.getRating()).isEqualTo(1);
        assertThat(rating.getUps()).isEqualTo(1);
        assertThat(rating.getDowns()).isEqualTo(0);
        assertThat(comment.getRatingHistory().get(USER_ID)).isEqualByComparingTo(RatingStatus.UP);
    }

    @Test
    public void haveRatingDownAndRecieveUp() {
        Rating rating = comment.getRating();
        rating.changeRating(false);
        comment.getRatingHistory().put(USER_ID, RatingStatus.DOWN);

        commentService.changeCommentRating(USER_ID, POST_ID, COMMENT_ID, RatingStatus.UP);

        assertThat(rating.getRating()).isEqualTo(1);
        assertThat(rating.getUps()).isEqualTo(1);
        assertThat(rating.getDowns()).isEqualTo(0);
        assertThat(comment.getRatingHistory().get(USER_ID)).isEqualByComparingTo(RatingStatus.UP);
    }

    @Test
    public void haveRatingEmptyAndRecieveUp() {
        Rating rating = comment.getRating();

        commentService.changeCommentRating(USER_ID, POST_ID, COMMENT_ID, RatingStatus.UP);

        assertThat(rating.getRating()).isEqualTo(1);
        assertThat(rating.getUps()).isEqualTo(1);
        assertThat(rating.getDowns()).isEqualTo(0);
        assertThat(comment.getRatingHistory().get(USER_ID)).isEqualByComparingTo(RatingStatus.UP);
    }

    @Test
    public void haveRatingUpAndRecieveDown() {
        Rating rating = comment.getRating();
        rating.changeRating(true);
        comment.getRatingHistory().put(USER_ID, RatingStatus.UP);

        commentService.changeCommentRating(USER_ID, POST_ID, COMMENT_ID, RatingStatus.DOWN);

        assertThat(rating.getRating()).isEqualTo(-1);
        assertThat(rating.getUps()).isEqualTo(0);
        assertThat(rating.getDowns()).isEqualTo(1);
        assertThat(comment.getRatingHistory().get(USER_ID)).isEqualByComparingTo(RatingStatus.DOWN);
    }

    @Test
    public void haveRatingDownAndRecieveDown() {
        Rating rating = comment.getRating();
        rating.changeRating(false);
        comment.getRatingHistory().put(USER_ID, RatingStatus.DOWN);

        commentService.changeCommentRating(USER_ID, POST_ID, COMMENT_ID, RatingStatus.DOWN);

        assertThat(rating.getRating()).isEqualTo(-1);
        assertThat(rating.getUps()).isEqualTo(0);
        assertThat(rating.getDowns()).isEqualTo(1);
        assertThat(comment.getRatingHistory().get(USER_ID)).isEqualByComparingTo(RatingStatus.DOWN);
    }

    @Test
    public void haveRatingEmptyAndRecieveDown() {
        Rating rating = comment.getRating();

        commentService.changeCommentRating(USER_ID, POST_ID, COMMENT_ID, RatingStatus.DOWN);

        assertThat(rating.getRating()).isEqualTo(-1);
        assertThat(rating.getUps()).isEqualTo(0);
        assertThat(rating.getDowns()).isEqualTo(1);
        assertThat(comment.getRatingHistory().get(USER_ID)).isEqualByComparingTo(RatingStatus.DOWN);
    }

    @Test
    public void haveRatingUpAndRecieveEmpty() {
        Rating rating = comment.getRating();
        rating.changeRating(true);
        comment.getRatingHistory().put(USER_ID, RatingStatus.UP);

        commentService.changeCommentRating(USER_ID, POST_ID, COMMENT_ID, RatingStatus.EMPTY);

        assertThat(rating.getRating()).isEqualTo(0);
        assertThat(rating.getUps()).isEqualTo(0);
        assertThat(rating.getDowns()).isEqualTo(0);
        assertThat(comment.getRatingHistory().get(USER_ID)).isNull();
    }

    @Test
    public void haveRatingDownAndRecieveEmpty() {
        Rating rating = comment.getRating();
        rating.changeRating(false);
        comment.getRatingHistory().put(USER_ID, RatingStatus.DOWN);

        commentService.changeCommentRating(USER_ID, POST_ID, COMMENT_ID, RatingStatus.EMPTY);

        assertThat(rating.getRating()).isEqualTo(0);
        assertThat(rating.getUps()).isEqualTo(0);
        assertThat(rating.getDowns()).isEqualTo(0);
        assertThat(comment.getRatingHistory().get(USER_ID)).isNull();
    }

    @Test
    public void haveRatingEmptyAndRecieveEmpty() {
        Rating rating = comment.getRating();

        commentService.changeCommentRating(USER_ID, POST_ID, COMMENT_ID, RatingStatus.EMPTY);

        assertThat(rating.getRating()).isEqualTo(0);
        assertThat(rating.getUps()).isEqualTo(0);
        assertThat(rating.getDowns()).isEqualTo(0);
        assertThat(comment.getRatingHistory().get(USER_ID)).isNull();
    }
}
