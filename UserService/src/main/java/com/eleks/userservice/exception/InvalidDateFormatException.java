package com.eleks.userservice.exception;

public class InvalidDateFormatException extends RuntimeException {

    public InvalidDateFormatException() {
        super();
    }

    public InvalidDateFormatException(String message) {
        super(message);
    }
}