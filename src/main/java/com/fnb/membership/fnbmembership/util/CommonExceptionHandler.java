package com.fnb.membership.fnbmembership.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

/**
 * A common handler class for exceptions.
 * As the number of exceptions handled in this handler grows, it should be sepreated into multiple classes according
 * to their concerns.
 */
@ControllerAdvice
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * A method for handling ConstraintViolationException
     * When a ConstraintViolationException occurs, it should return a 400 error code.
     * @param e the exception
     * @param request the current request
     * @return
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e, WebRequest request) {

        String bodyOfResponse = "A invalid arguments has been requested.";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
