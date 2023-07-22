package com.clonecoding.pinterest.pin.exception;

public class PinNotFoundException extends RuntimeException{
    public PinNotFoundException(String message) {
        super(message);
    }
}
