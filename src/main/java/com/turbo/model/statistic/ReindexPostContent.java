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
        this.name = firstNonNull(name, this.name);
        this.description = firstNonNull(description, this.description);
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
