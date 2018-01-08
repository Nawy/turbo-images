package com.turbo.model.statistic;

import com.turbo.util.CommonUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Data
@NoArgsConstructor
public class ReindexPost extends ReindexAction {
    private String name;
    private String description;
    private List<Long> ratings;
    private Collection<String> tags;
    private Long views;

    public ReindexPost(
            long id,
            String name,
            String description,
            List<Long> ratings,
            Collection<String> tags,
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

        this.name = CommonUtils.mergeObjects(
                this.name,
                value.getName(),
                (a, b) -> b
        );

        this.description = CommonUtils.mergeObjects(
                this.description,
                value.getDescription(),
                (a, b) -> b
        );

        this.ratings.addAll(value.ratings);
        this.tags = value.tags;
        this.views += value.views;
        return this;
    }

    @Override
    public String getType() {
        return ActionType.UPDATE_POST;
    }

    public static ReindexPost changeContent(
            final long id,
            final String name,
            final String description,
            final Collection<String> tags
    ) {
        return new ReindexPost(
                id,
                name,
                description,
                null,
                tags,
                null);
    }

    public static ReindexPost changeRating(final long id, final Long views, final Long rating) {
        return new ReindexPost(
                id,
                null,
                null,
                Collections.singletonList(rating),
                null,
                views
        );
    }
}
