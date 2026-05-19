package com.loadify.util;

import java.time.LocalDateTime;

public class ResponseStructure<T> {
    private int statusCode;
    private String message;
    private T data;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ResponseStructure(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseStructure<T> success(int statusCode, String message, T data) {
        return new ResponseStructure<>(statusCode, message, data);
    }

    public int getStatusCode() { return statusCode; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
