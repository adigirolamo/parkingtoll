package com.adigi.parkingtoll.exception;

public class NotDefiniedAlgorithmException extends RuntimeException {

    public NotDefiniedAlgorithmException() {
        super();
    }

    public NotDefiniedAlgorithmException(String message) {
        super(message);
    }

    public NotDefiniedAlgorithmException(String message, Throwable cause) {
        super(message, cause);
    }
}
