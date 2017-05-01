package com.turbo.model.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by ermolaev on 2/8/17.
 */
public class BadRequestHttpException extends HttpException {

    public BadRequestHttpException(String message, String detailed) {
        super(HttpStatus.BAD_REQUEST, message, detailed);
    }

    public BadRequestHttpException(String message) {
        super(HttpStatus.BAD_REQUEST, message, null);
    }
}
