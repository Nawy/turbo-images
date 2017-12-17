package com.turbo.model.statistic;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;


@Data
@NoArgsConstructor
public class ReindexPost extends ReindexAction {
    private String name;
    private String description;
    private List<Long> ratings;
    private List<String> tags;
    private Long views;

    public ReindexPost(
            long id,
            String name,
            String description,
            List<Long> ratings,
            List<String> tags,
            Long views
    ) {
        super(id);
        this.name = name;
        this.description = description;
        this.ratings = ratings;
        this.tags = tags;
        this.views = views;
    }

    @Override
    public ReindexAction merge(ReindexAction newValue) {
        final ReindexPost value = (ReindexPost)newValue;
        this.name = value.getName();
        this.description = value.getDescription();
        return this;
    }

    @Override
    public String getType() {
        return ActionType.UPDATE_POST;
    }

    public static ReindexPost changeContent(final long id, final String name, final String description) {
        return new ReindexPost(id, name, description, null, null, null);
    }

    public static ReindexPost changeTags(final long id, final List<String> tags) {
        return new ReindexPost(id, null, null, null, tags, null);
    }

    public static ReindexPost changeRating(final long id, final Long rating) {
        return new ReindexPost(
                id,
                null,
                null,
                Collections.singletonList(rating),
                null,
                null
        );
    }

    public static ReindexPost changeViews(final long id, final Long views) {
        return new ReindexPost(
                id,
                null,
                null,
                null,
                null,
                views
        );
    }
}
