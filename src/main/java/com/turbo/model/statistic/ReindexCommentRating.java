package com.turbo.model.statistic;

import lombok.Data;

import java.util.List;

@Data
public class ReindexCommentRating extends ReindexAction {

    private List<Long> values;

    public ReindexCommentRating(long id, List<Long> values) {
        super(id);
        this.values = values;
    }

    @Override
    public ReindexAction merge(ReindexAction newValue) {
        final ReindexCommentRating value = (ReindexCommentRating)newValue;
        this.values.addAll(value.getValues());
        return this;
    }

    @Override
    public String getType() {
        return ActionType.UPDATE_COMMENT_RATING;
    }
}
