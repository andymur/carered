package com.andymur.carered.error;

import com.andymur.carered.model.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if ("created_before".equals(ex.getName())) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid date format for 'created_before'. Expected format: YYYY-MM-DD"
                    ));
        } else if ("language".equals(ex.getName()) && ex.getRootCause() instanceof UnsupportedLanguageException) {
            return ResponseEntity.badRequest().body(new ErrorResponse(ex.getRootCause().getMessage()));
        }

        return ResponseEntity.badRequest().body(new ErrorResponse("Invalid parameter: " + ex.getName()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest().body(
                new ErrorResponse("Missing required parameter: " + ex.getParameterName())
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(RuntimeException ex) {
        if (ex instanceof IncorrectPageSizeException
                || ex instanceof IncorrectPageException) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(ex.getMessage())
            );
        }
        return ResponseEntity.badRequest().body(
                new ErrorResponse("Bad request: " + ex.getMessage())
        );
    }
}
