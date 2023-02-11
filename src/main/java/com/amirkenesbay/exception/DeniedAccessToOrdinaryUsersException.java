package com.amirkenesbay.exception;

public class DeniedAccessToOrdinaryUsersException extends RuntimeException{
    public DeniedAccessToOrdinaryUsersException(String message) {
        super(message);
    }
}
