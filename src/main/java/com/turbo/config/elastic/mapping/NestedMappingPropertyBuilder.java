package com.turbo.config.elastic.mapping;

import java.util.HashMap;
import java.util.Map;

public class NestedMappingPropertyBuilder {
    private MappingPropertyType type;
    private Integer ignoreAbove;
    private String format;
    private Boolean docValues;
    private Map<String, MappingProperty> properties = new HashMap<>();


    public NestedMappingPropertyBuilder setType(MappingPropertyType type) {
        this.type = type;
        return this;
    }

    public NestedMappingPropertyBuilder setIgnoreAbove(Integer ignoreAbove) {
        this.ignoreAbove = ignoreAbove;
        return this;
    }

    public NestedMappingPropertyBuilder setFormat(String format) {
        this.format = format;
        return this;
    }

    public NestedMappingPropertyBuilder setDocValues(Boolean docValues) {
        this.docValues = docValues;
        return this;
    }

    public NestedMappingPropertyBuilder addProperty(String key, MappingProperty property) {
        this.properties.put(key, property);
        return this;
    }

    public NestedMappingProperty createNestedMappingProperty() {
        return new NestedMappingProperty(type, ignoreAbove, format, docValues, properties);
    }
}