package com.turbo.model.statistic;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReindexPostViews extends ReindexAction {

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
        return ActionType.UPDATE_POST_VIEWS;
    }
}
