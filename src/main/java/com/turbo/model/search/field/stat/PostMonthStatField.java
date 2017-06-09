package com.turbo.model.search.field.stat;

/**
 * Created by ermolaev on 6/9/17.
 */
public enum PostMonthStatField {

    MONTH_RAITING("months.rating"),
    MONTH_VIEWS("months.views"),
    MONTH_UPS("months.ups"),
    MONTH_DOWNS("months.downs");

    private String fieldName;

    PostMonthStatField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
