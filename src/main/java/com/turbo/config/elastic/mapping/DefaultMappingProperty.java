package com.turbo.config.elastic.mapping;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultMappingProperty implements MappingProperty {

    private final MappingPropertyType type;
    private final Integer ignoreAbove;
    private final String format;
    private final Boolean docValues;

    public DefaultMappingProperty(MappingPropertyType type, Integer ignoreAbove, String format, Boolean docValues) {
        this.type = type;
        this.ignoreAbove = ignoreAbove;
        this.format = format;
        this.docValues = docValues;
    }

    @JsonProperty("type")
    public MappingPropertyType getType() {
        return type;
    }

    @JsonProperty("ignore_above")
    public Integer getIgnoreAbove() {
        return ignoreAbove;
    }

    @JsonProperty("format")
    public String getFormat() {
        return format;
    }

    @JsonProperty("doc_values")
    public Boolean getDocValues() {
        return docValues;
    }
}
