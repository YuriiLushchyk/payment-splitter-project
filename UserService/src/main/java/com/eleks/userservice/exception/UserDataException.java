package com.eleks.userservice.exception;

public class UserDataException extends RuntimeException {

    public UserDataException() {
        super();
    }

    public UserDataException(String message) {
        super(message);
    }
}