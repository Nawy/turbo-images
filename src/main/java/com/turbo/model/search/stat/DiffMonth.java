package com.turbo.model.search.stat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * Created by ermolaev on 6/23/17.
 */
@Data
public class DiffMonth {
    private LocalDate date;
    private long ups;
    private long downs;
    private long rating;

    public DiffMonth(
            @JsonProperty(value = "date", required = true) @JsonFormat(pattern = "MM") LocalDate date,
            @JsonProperty(value = "ups", required = true) long ups,
            @JsonProperty(value = "downs", required = true) long downs,
            @JsonProperty(value = "rating", required = true) long rating
    ) {
        this.date = date;
        this.ups = ups;
        this.downs = downs;
        this.rating = rating;
    }

    @JsonProperty(value = "date", required = true)
    @JsonFormat(pattern = "MM")
    public LocalDate getDate() {
        return date;
    }
}
