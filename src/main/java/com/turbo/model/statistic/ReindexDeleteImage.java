package com.turbo.model.statistic;

import lombok.Data;

@Data
public class ReindexDeleteImage extends ReindexAction {

    public ReindexDeleteImage(final int id) {
        super(id);
    }

    @Override
    public ReindexAction merge(ReindexAction newValue) {
        return this;
    }

    @Override
    public String getType() {
        return ActionType.DELETE_IMAGE;
    }
}
