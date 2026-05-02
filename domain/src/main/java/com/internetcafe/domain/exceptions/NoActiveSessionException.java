package com.internetcafe.domain.exceptions;

public class NoActiveSessionException extends RuntimeException {
    public NoActiveSessionException(String message) {
        super(message);
    }
}
