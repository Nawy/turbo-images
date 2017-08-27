package com.turbo.model.search.field;

/**
 * Created by ermolaev on 6/26/17.
 */
public enum ImageField {
    ID(ImageFieldNames.ID),
    NAME(ImageFieldNames.NAME),
    DESCRIPTION(ImageFieldNames.DESCRIPTION),
    USER_ID(ImageFieldNames.USER_ID),
    CREATION_DATE(ImageFieldNames.CREATION_DATE);

    private String fieldName;

    ImageField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
