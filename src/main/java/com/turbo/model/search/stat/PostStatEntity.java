package com.turbo.model.search.stat;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by ermolaev on 6/4/17.
 */
public class PostStatEntity {

    private long id;
    private String name;
    private String username;
    private List<String> descriptions;
    private List<String> tags;
    private List<DiffDay> days;
    private List<DiffWeek> weeks;
    private List<DiffMonth> months;
    private DiffYear year;

    public PostStatEntity(
            @JsonProperty(value = "id", required = true) long id,
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "username", required = true) String username,
            @JsonProperty(value = "descriptions", required = true) List<String> descriptions,
            @JsonProperty(value = "tags", required = true) List<String> tags,
            @JsonProperty(value = "days", required = true) List<DiffDay> days,
            @JsonProperty(value = "weeks", required = true) List<DiffWeek> weeks,
            @JsonProperty(value = "months", required = true) List<DiffMonth> months,
            @JsonProperty(value = "year", required = true) DiffYear year
    ) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.descriptions = descriptions;
        this.tags = tags;
        this.days = days;
        this.weeks = weeks;
        this.months = months;
        this.year = year;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<DiffDay> getDays() {
        return days;
    }

    public List<DiffWeek> getWeeks() {
        return weeks;
    }

    public List<DiffMonth> getMonths() {
        return months;
    }

    public DiffYear getYear() {
        return year;
    }
}
