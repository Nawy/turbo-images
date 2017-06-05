package com.turbo.model.search.stat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ermolaev on 6/4/17.
 */
public class UserRatingEntity {

    private long id;
    private long rating;

    public UserRatingEntity(
            @JsonProperty(value = "id", required = true)  long id,
            @JsonProperty(value = "rating", required = true) long rating
    ) {
        this.id = id;
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public long getRating() {
        return rating;
    }
}
