package com.example.controllers;


import com.example.domain.models.response.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller advice to handle exceptions etc.
 */
@ControllerAdvice
public class ControllerAdviceHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgument(final IllegalArgumentException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Illegal Argument Exception");
        response.setException(ex);
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponse handleDataIntegrityViolationException(final DataIntegrityViolationException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setException(ex);
        response.setMessage("Data Integrity Exception");
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ErrorResponse handleDataIntegrityViolationException(final EmptyResultDataAccessException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setException(ex);
        response.setMessage("Empty Result Exception");
        return response;
    }
}
