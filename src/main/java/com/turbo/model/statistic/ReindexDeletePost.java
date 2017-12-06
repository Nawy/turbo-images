package com.turbo.model.statistic;

import lombok.Data;

@Data
public class ReindexDeletePost extends ReindexAction {

    public ReindexDeletePost(final int id) {
        super(id);
    }

    @Override
    public ReindexAction merge(ReindexAction newValue) {
        return this;
    }

    @Override
    public String getType() {
        return ActionType.DELETE_POST;
    }
}
