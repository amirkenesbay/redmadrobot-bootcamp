package com.amirkenesbay.exception;

public class UserEmailNotFoundException extends RuntimeException{
    public UserEmailNotFoundException(String message) {
        super(message);
    }
}
