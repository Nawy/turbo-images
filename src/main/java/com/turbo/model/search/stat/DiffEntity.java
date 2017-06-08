package com.turbo.model.search.stat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

/**
 * Created by ermolaev on 6/7/17.
 */
public class DiffEntity {

    private LocalDate date;
    private long ups;
    private long downs;
    private long diff;

    public DiffEntity(
            @JsonProperty(value = "date", required = true) @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @JsonProperty(value = "ups", required = true) long ups,
            @JsonProperty(value = "downs", required = true) long downs,
            @JsonProperty(value = "diff", required = true) long diff
    ) {
        this.ups = ups;
        this.downs = downs;
        this.diff = diff;
    }

    @JsonProperty(value = "date", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate getDate() {
        return date;
    }

    @JsonProperty(value = "ups", required = true)
    public long getUps() {
        return ups;
    }

    @JsonProperty(value = "downs", required = true)
    public long getDowns() {
        return downs;
    }

    @JsonProperty(value = "diff", required = true)
    public long getDiff() {
        return diff;
    }
}
