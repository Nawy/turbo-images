package com.turbo.model.statistic;

import lombok.Data;

@Data
public class ReindexPostContent extends ReindexAction {
    private String name;
    private String description;

    public ReindexPostContent(long id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    @Override
    public ReindexAction merge(ReindexAction newValue) {
        final ReindexPostContent value = (ReindexPostContent)newValue;
        this.name = value.getName();
        this.description = value.getDescription();
        return this;
    }

    @Override
    public String getType() {
        return ActionType.POST_CONTENT;
    }
}
