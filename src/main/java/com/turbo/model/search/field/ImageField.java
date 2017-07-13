package com.turbo.model.search.field;

/**
 * Created by ermolaev on 6/26/17.
 */
public enum ImageField {
    ID("id"),
    DESCRIPTION("description"),
    USER_ID("user_id"),
    CREATE_DATE("create_date");

    private String fieldName;

    ImageField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
