package com.turbo.model.search.field.stat;

/**
 * Created by ermolaev on 6/9/17.
 */
public enum PostWeeksStatField {

    WEEK_RAITING("weeks.rating"),
    WEEK_VIEWS("weeks.views"),
    WEEK_UPS("weeks.ups"),
    WEEK_DOWNS("weeks.downs");

    private String fieldName;

    PostWeeksStatField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
