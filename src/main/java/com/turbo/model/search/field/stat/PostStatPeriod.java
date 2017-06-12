package com.turbo.model.search.field.stat;

/**
 * Created by ermolaev on 6/11/17.
 */
public enum PostStatPeriod {

    DAYS("days"),
    WEEKS("weeks"),
    MONTHS("months"),
    YEAR("year");

    private String fieldName;

    PostStatPeriod(String fieldName) {
        this.fieldName = fieldName;
    }

    //return
    public String getFieldName(PostDiffStatField diffStatField) {
        return fieldName + diffStatField.getFieldName();
    }

}
