package ru.samsebemehanik.catalog.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.samsebemehanik.catalog.dto.ComponentErrorResponse;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ComponentErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        List<String> details = exception.getBindingResult().getFieldErrors().stream()
                .map(this::toMessage)
                .toList();

        return ResponseEntity.badRequest()
                .body(new ComponentErrorResponse("VALIDATION_ERROR", "Request validation failed", details));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, JsonProcessingException.class})
    public ResponseEntity<ComponentErrorResponse> handleJsonParsing(Exception exception) {
        return ResponseEntity.badRequest()
                .body(new ComponentErrorResponse(
                        "JSON_PARSE_ERROR",
                        "Request JSON cannot be parsed",
                        List.of(exception.getMessage())
                ));
    }

    @ExceptionHandler({HibernateException.class, DataAccessException.class, ComponentProcessingException.class})
    public ResponseEntity<ComponentErrorResponse> handlePersistence(RuntimeException exception) {
        return ResponseEntity.badRequest()
                .body(new ComponentErrorResponse(
                        "PERSISTENCE_ERROR",
                        "Database operation failed",
                        List.of(exception.getMessage())
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ComponentErrorResponse> handleUnexpected(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ComponentErrorResponse(
                        "INTERNAL_ERROR",
                        "Unexpected error",
                        List.of(exception.getMessage())
                ));
    }

    private String toMessage(FieldError fieldError) {
        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }
}
