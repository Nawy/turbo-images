package com.turbo.model.search.stat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by ermolaev on 6/23/17.
 */
@Data
public class DiffYear {
    private long ups;
    private long downs;
    private long rating;

    public DiffYear(
            @JsonProperty(value = "ups", required = true) long ups,
            @JsonProperty(value = "downs", required = true) long downs,
            @JsonProperty(value = "rating", required = true) long rating
    ) {
        this.ups = ups;
        this.downs = downs;
        this.rating = rating;
    }

    @JsonProperty(value = "ups", required = true)
    public long getUps() {
        return ups;
    }

    @JsonProperty(value = "downs", required = true)
    public long getDowns() {
        return downs;
    }

    @JsonProperty(value = "rating", required = true)
    public long getRating() {
        return rating;
    }
}
