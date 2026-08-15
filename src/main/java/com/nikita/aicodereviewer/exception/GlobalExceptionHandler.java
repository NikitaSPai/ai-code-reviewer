package com.nikita.aicodereviewer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

/** Global exception handler for handling exceptions in the application. */
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception ex) {

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {

    String message = ex.getBindingResult().getFieldError().getDefaultMessage();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
  }

  @ExceptionHandler(HttpClientErrorException.TooManyRequests.class)
  public ResponseEntity<String> handleTooManyRequests(HttpClientErrorException.TooManyRequests ex) {

    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
        .body("Gemini API quota exceeded. Please try again later.");
  }
}
