package com.ifarmr.exception.customExceptions;

public class DuplicateMerchandiseException extends RuntimeException {
    public DuplicateMerchandiseException(String message) {
        super(message);
    }
}
