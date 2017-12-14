package com.turbo.model.statistic;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ReindexPostTags extends ReindexAction {

    private List<String> tags;

    public ReindexPostTags(long id, List<String> tags) {
        super(id);
        this.tags = tags;
    }

    @Override
    public ReindexAction merge(ReindexAction newValue) {
        final ReindexPostTags value = (ReindexPostTags)newValue;
        this.tags = value.tags;
        return this;
    }

    @Override
    public String getType() {
        return ActionType.UPDATE_POST_TAGS;
    }
}
