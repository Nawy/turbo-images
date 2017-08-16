package com.turbo.config.elastic.mapping;

public class DefaultMappingPropertyBuilder {
    private MappingPropertyType type;
    private Integer ignoreAbove;
    private String format;
    private Boolean docValues;

    public DefaultMappingPropertyBuilder setType(MappingPropertyType type) {
        this.type = type;
        return this;
    }

    public DefaultMappingPropertyBuilder setIgnoreAbove(Integer ignoreAbove) {
        this.ignoreAbove = ignoreAbove;
        return this;
    }

    public DefaultMappingPropertyBuilder setFormat(String format) {
        this.format = format;
        return this;
    }

    public DefaultMappingPropertyBuilder setDocValues(Boolean docValues) {
        this.docValues = docValues;
        return this;
    }

    public DefaultMappingProperty createMappingProperty() {
        return new DefaultMappingProperty(type, ignoreAbove, format, docValues);
    }
}