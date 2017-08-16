package com.turbo.config.elastic.mapping;

import java.util.HashMap;
import java.util.Map;

public class MappingBuilder {

    private Map<String, MappingProperty> properties = new HashMap<>();

    public MappingBuilder addProperty(String key, MappingProperty property) {
        this.properties.put(key, property);
        return this;
    }

    public Mapping createMapping() {
        return new Mapping(properties);
    }
}
