package com.turbo.model.exception.http;

import org.springframework.http.HttpStatus;

/**
 * Created by ermolaev on 2/8/17.
 */
public abstract class HttpException extends RuntimeException {
    private HttpStatus httpStatus;
    private String name;
    private String detailed;

    public HttpException(HttpStatus httpStatus, String message, String detailed) {
        super(message);
        this.httpStatus = httpStatus;
        this.detailed = detailed;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatusm) {
        this.httpStatus = httpStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetailed() {
        return detailed;
    }

    public void setDetailed(String detailed) {
        this.detailed = detailed;
    }
}
