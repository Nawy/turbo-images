package com.turbo.model.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by ermolaev on 2/8/17.
 */
public class UnauthorizedHttpException extends HttpException {

    public UnauthorizedHttpException(String message, String detailed) {
        super(HttpStatus.UNAUTHORIZED, message, detailed);
    }

    public UnauthorizedHttpException(String message) {
        super(HttpStatus.UNAUTHORIZED, message, null);
    }
}
