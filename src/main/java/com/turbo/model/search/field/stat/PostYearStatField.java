package com.turbo.model.search.field.stat;

/**
 * Created by ermolaev on 6/9/17.
 */
public enum PostYearStatField {
    YEAR_RAITING("year.rating"),
    YEAR_VIEWS("year.views"),
    YEAR_UPS("year.ups"),
    YEAR_DOWNS("year.downs");

    private String fieldName;

    PostYearStatField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
