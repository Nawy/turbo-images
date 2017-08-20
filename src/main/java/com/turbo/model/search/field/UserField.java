package com.turbo.model.search.field;

/**
 * Created by ermolaev on 5/27/17.
 */
public enum UserField {

    ID(UserFieldNames.ID),
    NAME(UserFieldNames.NAME),
    EMAIL(UserFieldNames.EMAIL),
    RATING(UserFieldNames.RATING),
    CREATION_DATE(UserFieldNames.CREATION_DATE);

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
