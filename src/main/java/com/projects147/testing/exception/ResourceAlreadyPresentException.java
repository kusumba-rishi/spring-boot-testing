package com.projects147.testing.exception;

public class ResourceAlreadyPresentException extends RuntimeException {

    public ResourceAlreadyPresentException(String message) {
        super(message);
    }

    public ResourceAlreadyPresentException(String message, Throwable cause) {
        super(message, cause);
    }

}
