package com.turbo.repository.elasticsearch.helper;

import org.elasticsearch.search.sort.SortOrder;

/**
 * Created by ermolaev on 5/23/17.
 */
public class SearchSort {
    private String fieldName;
    private SortOrder order;

    public SearchSort(String fieldName, SortOrder order) {
        this.fieldName = fieldName;
        this.order = order;
    }

    public String getFieldName() {
        return fieldName;
    }

    public SortOrder getOrder() {
        return order;
    }
}
