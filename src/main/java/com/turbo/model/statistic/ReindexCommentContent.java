package com.turbo.model.statistic;

import lombok.Data;


@Data
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
        return ActionType.COMMENT_CONTENT;
    }
}
