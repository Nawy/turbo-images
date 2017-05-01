package com.turbo.config;

import com.turbo.model.exception.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ermolaev on 2/13/17.
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    private Logger LOG = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<Void> handleHttpException(HttpServletRequest request, HttpException e) {
        LOG.error(
                "Exception occurred with HTTP ERROR:{} and request info: {}",
                e.getHttpStatus().toString(),
                request.getContextPath() + request.getServletPath(),
                e
        );
        return ResponseEntity.status(e.getHttpStatus()).build();
    }

}
