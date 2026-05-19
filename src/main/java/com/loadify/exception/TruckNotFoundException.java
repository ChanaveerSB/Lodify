package com.loadify.exception;

public class TruckNotFoundException extends RuntimeException {
    public TruckNotFoundException(String message) { super(message); }
}
