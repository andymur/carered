package com.andymur.carered.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if ("created_before".equals(ex.getName())) {
            return ResponseEntity.badRequest().body(
                    "Invalid date format for 'created_before'. Expected format: YYYY-MM-DD"
            );
        }

        return ResponseEntity.badRequest().body("Invalid parameter: " + ex.getName());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParam(MissingServletRequestParameterException ex) {
        if (ex.getParameterName().equals("created_before")) {
            return ResponseEntity.badRequest()
                    .body("Missing required query parameter 'created_before' (format: YYYY-MM-DD)");
        }
        return ResponseEntity.badRequest().body("Missing required parameter: " + ex.getParameterName());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        if (ex.getMessage().contains("Invalid language")) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return ResponseEntity.badRequest().body("Bad request: " + ex.getMessage());
    }
}
