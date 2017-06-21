package com.turbo.model.search.field;

/**
 * Created by ermolaev on 5/27/17.
 */
public enum UserField {

    NAME("name"),
    EMAIL("email"),
    RATING("rating"),
    CREATE_DATE("create_date");

    private String fieldName;

    UserField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldKeyword() {
        return fieldName + ".keyword";
    }
}
