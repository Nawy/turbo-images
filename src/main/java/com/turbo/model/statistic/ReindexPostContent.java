package com.turbo.model.statistic;

import com.turbo.model.Nullable;
import lombok.Data;

import static com.google.common.base.MoreObjects.firstNonNull;


@Data
public class ReindexPostContent extends ReindexAction {
    private String name;
    private String description;

    public ReindexPostContent(long id, @Nullable final String name, @Nullable final String description) {
        super(id);
        this.name = name != null ? name : this.name;
        this.description = description != null ? description : this.description;
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
        return ActionType.UPDATE_POST_CONTENT;
    }
}
