package com.turbo.model.search;

/**
 * Created by ermolaev on 5/27/17.
 */
public interface SearchConverter <T> {

    /**
     * Method for getting inner business objects for service layer
     * @return
     */
    T getEntity();
}
