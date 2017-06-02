package com.turbo.model.exception.data;

/**
 * Created by ermolaev on 5/25/17.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
