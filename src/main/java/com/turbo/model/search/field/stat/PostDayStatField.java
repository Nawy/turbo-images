package com.turbo.model.search.field.stat;

/**
 * Created by ermolaev on 6/9/17.
 */
public enum PostDayStatField {


    DAY_RAITING("days.rating"),
    DAY_VIEWS("days.views"),
    DAY_UPS("days.ups"),
    DAY_DOWNS("days.downs");

    private String fieldName;

    PostDayStatField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
