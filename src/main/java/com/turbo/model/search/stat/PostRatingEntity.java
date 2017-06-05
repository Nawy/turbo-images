package com.turbo.model.search.stat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ermolaev on 6/4/17.
 */
public class PostRatingEntity {


    private long id;
    private String name;
    private String description;
    private long downs;
    private long ups;
    private long diff;

    public PostRatingEntity(
            @JsonProperty(value = "id", required = true) long id,
            @JsonProperty(value = "downs", required = true)long downs,
            @JsonProperty(value = "ups", required = true) long ups,
            @JsonProperty(value = "diff", required = true) long diff
    ) {
        this.id = id;
        this.downs = downs;
        this.ups = ups;
        this.diff = diff;
    }

    public long getId() {
        return id;
    }

    public long getDowns() {
        return downs;
    }

    public long getUps() {
        return ups;
    }

    public long getDiff() {
        return diff;
    }
}
