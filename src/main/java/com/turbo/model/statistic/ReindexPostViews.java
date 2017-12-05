package com.turbo.model.statistic;

import lombok.Data;

@Data
public class ReindexPostViews extends ReindexAction  {

    private Long views;

    public ReindexPostViews(long id, Long value) {
        super(id);
        this.views = value;
    }

    @Override
    public ReindexAction merge(ReindexAction newValue) {
        final ReindexPostViews value = (ReindexPostViews)newValue;
        this.views += value.getViews();
        return this;
    }

    @Override
    public String getType() {
        return ActionType.POST_VIEWS;
    }
}
