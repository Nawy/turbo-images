package com.turbo.model.search.field;

/**
 * Created by ermolaev on 5/23/17.
 */
public enum PostField {

    ID("id"),
    NAME("name"),
    DESCRIPTIONS("descriptions"),
    RATING("rating"),
    VIEWS("views"),
    AUTHOR("username"),
    TAGS("tags"),
    CREATION_DATE("create_date");

    private String fieldName;

    PostField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
