package com.turbo.model.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by ermolaev on 2/8/17.
 */
public class ForbiddenHttpException extends HttpException {

    public ForbiddenHttpException(String message, String detailed) {
        super(HttpStatus.FORBIDDEN, message, detailed);
    }

    public ForbiddenHttpException(String message) {
        super(HttpStatus.FORBIDDEN, message, null);
    }
}
