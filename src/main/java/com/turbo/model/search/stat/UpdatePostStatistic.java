package com.turbo.model.search.stat;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdatePostStatistic {

    private long id;
    private String name;
    private String description;
    private List<String> tags;
    private long ups;
    private long downs;
    private long rating;
    private long views;
}
