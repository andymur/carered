package com.andymur.carered.error;

import com.andymur.carered.model.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ErrorResponse> handleWebInput(ServerWebInputException ex) {
        String param = ex.getMethodParameter() != null ? ex.getMethodParameter().getParameterName() : null;
        Throwable cause = ex.getCause();

        if ("created_start".equals(param) && cause instanceof java.time.format.DateTimeParseException) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid date format for 'created_start'. Expected format: YYYY-MM-DD")
            );
        } else if ("language".equals(param) && hasCause(cause, UnsupportedLanguageException.class)) {
            return ResponseEntity.badRequest().body(new ErrorResponse(cause.getCause().getCause().getMessage()));
        }

        return ResponseEntity.badRequest().body(
                new ErrorResponse("Missing required parameter: " + (ex.getReason() != null ? ex.getReason() : ex.getMessage()))
        );
    }

    private boolean hasCause(Throwable ex, Class<? extends Throwable> causeClass) {
        Throwable current = ex;
        while (current != null) {
            if (causeClass.isInstance(current)) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(RuntimeException ex) {
        if (ex instanceof IncorrectPageSizeException
                || ex instanceof IncorrectPageException) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(ex.getMessage())
            );
        } else if (ex instanceof GitHubServerError) {
            return ResponseEntity.internalServerError().body(new ErrorResponse(ex.getMessage()));
        }
        return ResponseEntity.badRequest().body(
                new ErrorResponse("Bad request: " + ex.getMessage())
        );
    }
}
