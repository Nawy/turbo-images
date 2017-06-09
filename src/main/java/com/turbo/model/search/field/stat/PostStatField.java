package com.turbo.model.search.field.stat;

/**
 * Created by ermolaev on 6/9/17.
 */
public enum PostStatField {

    ID("id"),
    NAME("name"),
    DESCRIPTIONS("descriptions"),
    AUTHOR("author_id"),
    TAGS("tags"),
    DAYS_DATE("days.date"),
    WEEKS_DATE("weeks.date"),
    MONTHS_DATE("months.date"),
    YEAR_DATE("year.date");

    private String fieldName;

    PostStatField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
