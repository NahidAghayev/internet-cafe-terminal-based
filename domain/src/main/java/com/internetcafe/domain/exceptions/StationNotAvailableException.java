package com.internetcafe.domain.exceptions;

public class StationNotAvailableException extends RuntimeException {
    public StationNotAvailableException(String message) {
        super(message);
    }
}
