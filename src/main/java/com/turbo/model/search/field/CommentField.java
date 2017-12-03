package com.turbo.model.search.field;


public enum CommentField {

    ID(CommentFieldNames.ID),
    USER_ID(CommentFieldNames.USER_ID),
    REPLY_ID(CommentFieldNames.REPLY_ID),
    REPLY_TYPE(CommentFieldNames.REPLY_TYPE),
    DEVICE(CommentFieldNames.DEVICE),
    CONTENT(CommentFieldNames.CONTENT),
    CREATION_DATE(CommentFieldNames.CREATION_DATE),
    UPS(CommentFieldNames.UPS),
    DOWNS(CommentFieldNames.DOWNS),
    RATING(CommentFieldNames.RATING);

    private String fieldName;

    CommentField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
