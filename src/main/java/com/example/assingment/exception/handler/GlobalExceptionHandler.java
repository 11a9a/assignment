package com.example.assingment.exception.handler;

import jakarta.validation.UnexpectedTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<Object> handleUnexpectedTypeException(final UnexpectedTypeException ex) {
        log.error("UnexpectedTypeException message is --> {}", ex.getMessage());
        return new ResponseEntity<>(new GenericResponse("Bad Request", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> handleHttpClientErrorException(final HttpClientErrorException ex) {
        log.error("HttpClientErrorException message is --> {}", ex.getMessage());
        return new ResponseEntity<>(new GenericResponse("Server Error", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handlerNoSuchElementException(NoSuchElementException ex) {
        log.error("NoSuchElementException message is --> {}", ex.getMessage());
        return new ResponseEntity<>(new GenericResponse("No data found for this identifier", ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleInternal(final Exception ex) {
        log.error("Exception message is --> {}", ex.getMessage());
        return new ResponseEntity<>(new GenericResponse("Server Error", ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException message is --> {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(
                new GenericResponse("Bad Request",
                        List.of(errors)), HttpStatus.BAD_REQUEST);
    }
}
