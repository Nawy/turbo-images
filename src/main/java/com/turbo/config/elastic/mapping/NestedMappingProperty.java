package com.turbo.config.elastic.mapping;

import java.util.Map;

public class NestedMappingProperty extends DefaultMappingProperty implements MappingProperty {

    private Map<String, MappingProperty> properties;

    public NestedMappingProperty(
            MappingPropertyType type,
            Integer ignoreAbove,
            String format,
            Boolean docValues,
            Map<String, MappingProperty> properties
    ) {
        super(type, ignoreAbove, format, docValues);
        this.properties = properties;
    }

    public Map<String, MappingProperty> getProperties() {
        return properties;
    }
}
