package com.turbo.model.statistic;

import lombok.Data;

import java.util.List;

@Data
public class ReindexPostRating extends ReindexAction {

    private List<Long> values;

    public ReindexPostRating(long id, List<Long> values) {
        super(id);
        this.values = values;
    }

    @Override
    public ReindexAction merge(ReindexAction newValue) {
        final ReindexPostRating value = (ReindexPostRating)newValue;
        this.values.addAll(value.getValues());
        return this;
    }

    @Override
    public String getType() {
        return ActionType.UPDATE_POST_RATING;
    }
}
