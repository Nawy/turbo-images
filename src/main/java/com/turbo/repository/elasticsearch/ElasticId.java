package com.turbo.repository.elasticsearch;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ermolaev on 6/9/17.
 */
public class ElasticId {
    private Long id;

    public ElasticId(@JsonProperty("id") Long id) {
        this.id = id;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }
}
