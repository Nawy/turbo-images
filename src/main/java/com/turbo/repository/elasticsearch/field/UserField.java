package com.turbo.repository.elasticsearch.field;

/**
 * Created by ermolaev on 5/27/17.
 */
public enum UserField {

    ID("id"),
    NAME("name"),
    EMAIL("email"),
    CREATE_DATE("create_date");

    private String fieldName;

    UserField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
