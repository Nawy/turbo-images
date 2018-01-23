package com.turbo.model.search.stat;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Data
@Builder
@ToString
public class UpdatePostStatistic {

    private long id;
    private String name;
    private String description;
    private Set<String> tags;
    private long ups;
    private long downs;
    private long rating;
    private long views;
}
