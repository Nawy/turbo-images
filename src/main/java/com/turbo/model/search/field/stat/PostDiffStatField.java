package com.turbo.model.search.field.stat;

/**
 * Created by ermolaev on 6/11/17.
 */
public enum PostDiffStatField {

    DATE(".date"),
    RATING(".rating"),
    VIEWS(".views"),
    UPS(".ups"),
    DOWNS(".downs");

    private String fieldName;

    PostDiffStatField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
