package com.adigi.parkingtoll.exception;

public class WrongStateException extends RuntimeException {

    public WrongStateException() {
        super();
    }

    public WrongStateException(String message) {
        super(message);
    }

    public WrongStateException(String message, Throwable cause) {
        super(message, cause);
    }

}
