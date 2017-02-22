package com.example.domain.models.response;

import java.io.Serializable;

/**
 * Response sent when an error occurs
 */
public class ErrorResponse implements Serializable {
    private String message;
    private Throwable exception;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }
}
