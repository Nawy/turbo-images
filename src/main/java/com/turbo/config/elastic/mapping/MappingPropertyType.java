package com.turbo.config.elastic.mapping;

public enum MappingPropertyType {
    DATE,
    LONG,
    KEYWORD,
    NESTED;

    @Override
    public String toString() {
        return this.toString().toLowerCase();
    }
}
