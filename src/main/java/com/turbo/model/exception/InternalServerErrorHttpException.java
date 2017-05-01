package com.turbo.model.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by ermolaev on 2/8/17.
 */
public class InternalServerErrorHttpException extends HttpException {

    public InternalServerErrorHttpException(String message, String detailed) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message, detailed);
    }

    public InternalServerErrorHttpException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message, null);
    }
}
