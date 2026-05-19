package com.loadify.exception;

import com.loadify.util.ResponseStructure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            UserNotFoundException.class,
            TruckNotFoundException.class,
            BookingNotFoundException.class
    })
    public ResponseEntity<ResponseStructure<String>> handleNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseStructure.success(404, ex.getMessage(), null));
    }

    @ExceptionHandler({
            InvalidCapacityException.class,
            DuplicateEmailException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ResponseStructure<String>> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.badRequest()
                .body(ResponseStructure.success(400, ex.getMessage(), null));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ResponseStructure<String>> handleUnauthorized(UnauthorizedAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ResponseStructure.success(403, ex.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseStructure<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest()
                .body(ResponseStructure.success(400, "Validation failed", errors));
    }
}
