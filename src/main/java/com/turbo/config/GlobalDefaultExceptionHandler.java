package com.turbo.config;

import com.turbo.model.exception.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by ermolaev on 2/13/17.
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    private Logger LOG = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleHttpException(Exception exception) {

        if (exception instanceof AuthenticationException) {
            return new ResponseEntity<>(new ErrorResponse("Don't have permissions, please login"), HttpStatus.UNAUTHORIZED);
        }

        if (!(exception instanceof HttpException)) {
            LOG.error("Unpredicted exception was thrown", exception);
            return new ResponseEntity<>(new ErrorResponse("We fail, sorry"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpException httpException = (HttpException) exception;
        if (httpException.getDetailed() != null) {
            LOG.info("Http exception:{} with detailed message:{}", httpException.getMessage(), httpException.getDetailed());
        } else {
            LOG.info("Http exception:{}", httpException.getMessage());
        }
        return new ResponseEntity<>(new ErrorResponse(httpException.getMessage()), httpException.getHttpStatus());
    }

    private static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
