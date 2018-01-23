package com.turbo.model.search.stat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Created by ermolaev on 6/7/17.
 */
@Data
@Setter
public class DiffDay {

    private LocalDate date;
    private long ups;
    private long downs;
    private long rating;

    public DiffDay(
            @JsonProperty(value = "date", required = true) @JsonFormat(pattern = "dd-MM-yyyy") LocalDate date,
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
    @JsonFormat(pattern = "dd-MM-yyyy")
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

    @JsonProperty(value = "rating", required = true)
    public long getRating() {
        return rating;
    }
}
