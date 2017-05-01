package com.turbo.model.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by ermolaev on 2/8/17.
 */
public class NotFoundHttpException extends HttpException {

    public NotFoundHttpException(String message, String detailed) {
        super(HttpStatus.NOT_FOUND, message, detailed);
    }

    public NotFoundHttpException(String message) {
        super(HttpStatus.NOT_FOUND, message, null);
    }
}
