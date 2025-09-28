package com.innowise.userservice.exception.handler;

import com.innowise.userservice.exception.MultiCacheableInvalidArgsException;
import com.innowise.userservice.exception.ObjectNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandlerController {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(final
  MethodArgumentNotValidException e) {
    Map<String, String> errors = new HashMap<>();
    e.getBindingResult().getFieldErrors().forEach(error ->
        errors.put(error.getField(), error.getDefaultMessage()));
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, String>> handleConstraintViolationException(
      final ConstraintViolationException e) {
    Map<String, String> errors = new HashMap<>();
    for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
      errors.put(constraintViolation.getRootBeanClass().getSimpleName() + "."
          + constraintViolation.getPropertyPath(), e.getMessage());
    }
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(ObjectNotFoundException.class)
  public ResponseEntity<String> handleObjectNotFoundException(final ObjectNotFoundException e) {
    return ResponseEntity.notFound().header("error", e.getMessage()).build();
  }

  @ExceptionHandler(SQLException.class)
  public ResponseEntity<String> handleSQLException(final SQLException e) {
    return ResponseEntity.internalServerError().body(e.getMessage());
  }

  @ExceptionHandler(MultiCacheableInvalidArgsException.class)
  public ResponseEntity<String> handleMultiCacheableInvalidArgsException(
      final MultiCacheableInvalidArgsException e) {
    return ResponseEntity.internalServerError().body(e.getMessage());
  }

  @ExceptionHandler(EmptyResultDataAccessException.class)
  public ResponseEntity<String> handleEmptyResultDataAccessException(
      final EmptyResultDataAccessException e) {
    return ResponseEntity.unprocessableEntity().body(e.getMessage());
  }
}
