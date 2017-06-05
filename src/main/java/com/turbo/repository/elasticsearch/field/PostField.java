package com.turbo.repository.elasticsearch.field;

/**
 * Created by ermolaev on 5/23/17.
 */
public enum PostField {

    ID("id"),
    NAME("name"),
    DESCRIPTIONS("descriptions"),
    RAITING("rating"),
    VIEWS("views"),
    UPS("ups"),
    DOWNS("downs"),
    AUTHOR("author_id"),
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
