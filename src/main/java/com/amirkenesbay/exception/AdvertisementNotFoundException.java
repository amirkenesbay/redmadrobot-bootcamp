package com.amirkenesbay.exception;

public class AdvertisementNotFoundException extends RuntimeException{
    public AdvertisementNotFoundException(String message) {
        super(message);
    }
}
