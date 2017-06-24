package com.turbo.config;

import com.turbo.model.exception.HttpException;
import com.turbo.model.exception.InternalServerErrorHttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ermolaev on 2/13/17.
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    private Logger LOG = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleHttpException(HttpServletRequest request, Exception exception) {

        if(exception instanceof AuthenticationException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!(exception instanceof HttpException)) {
            LOG.error("Unpredicted exception was thrown", exception);
        }

        HttpException e = exception instanceof HttpException ?
                (HttpException) exception :
                new InternalServerErrorHttpException("Unknown error");

        LOG.error(
                "Exception occurred with HTTP ERROR:{} and request info: {}",
                e.getHttpStatus().toString(),
                request.getContextPath() + request.getServletPath(),
                e
        );
        return ResponseEntity.status(e.getHttpStatus()).build();
    }


}
