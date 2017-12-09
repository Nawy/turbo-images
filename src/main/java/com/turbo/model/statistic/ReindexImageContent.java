package com.turbo.model.statistic;

import lombok.Data;

import static com.google.common.base.MoreObjects.firstNonNull;

@Data
public class ReindexImageContent extends ReindexAction {

    private String name;
    private String description;

    public ReindexImageContent(long id, String name, String description) {
        super(id);
        this.name = firstNonNull(name, this.name);
        this.description = firstNonNull(description, this.description);
    }

    @Override
    public ReindexAction merge(ReindexAction newValue) {
        final ReindexImageContent value = (ReindexImageContent)newValue;
        this.name = value.getName();
        this.description = value.getDescription();
        return this;
    }

    @Override
    public String getType() {
        return ActionType.UPDATE_IMAGE_CONTENT;
    }
}
