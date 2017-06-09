package com.turbo.model.search.field;

/**
 * Created by ermolaev on 6/9/17.
 */
public enum PortSortField {

    RAITING("rating"),
    VIEWS("views"),
    UPS("ups"),
    DOWNS("downs");

    private String fieldName;

    PortSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
