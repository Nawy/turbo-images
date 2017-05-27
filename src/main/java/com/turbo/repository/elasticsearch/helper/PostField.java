package com.turbo.repository.elasticsearch.helper;

/**
 * Created by ermolaev on 5/23/17.
 */
public enum PostField {

    ID("id"),
    NAME("name"),
    RAITING("rating"),
    DESCRIPTION("description");

    private String fieldName;

    PostField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
