package com.turbo.config.elastic.mapping;

import java.util.HashMap;
import java.util.Map;

public class Mapping {

    private Map<String, MappingProperty> properties = new HashMap<>();

    public Mapping(Map<String, MappingProperty> properties) {
        this.properties = properties;
    }

    public Map<String, MappingProperty> getProperties() {
        return properties;
    }
}
