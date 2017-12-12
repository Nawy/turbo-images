package com.turbo.model.statistic;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ReindexCommentContent extends ReindexAction {

    private String content;

    public ReindexCommentContent(long id, String content) {
        super(id);
        this.content = content;
    }

    @Override
    public ReindexAction merge(ReindexAction newValue) {
        final ReindexCommentContent value = (ReindexCommentContent)newValue;
        this.content = value.getContent();
        return this;
    }

    @Override
    public String getType() {
        return ActionType.UPDATE_COMMENT_CONTENT;
    }
}
