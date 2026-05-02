package com.internetcafe.domain.exceptions;

public class ActiveSessionExistsException extends RuntimeException {
    public ActiveSessionExistsException(String message) {
        super(message);
    }
}
