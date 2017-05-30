package com.turbo.repository.elasticsearch.field;

/**
 * Created by ermolaev on 5/23/17.
 */
public enum PostField {

    ID("id"),
    NAME("name"),
    RAITING("rating"), //FIXME such field don't exists anymore???
    AUTHOR("author_id"), //FIXME NEED SEARCH BY POST authorId!!!! JUst created enum and don't know will it work?
    CREATION_DATE("create_date"),
    DESCRIPTION("description");

    private String fieldName;

    PostField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
