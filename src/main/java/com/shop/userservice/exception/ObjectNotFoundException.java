package com.innowise.userservice.exception;

public class ObjectNotFoundException extends RuntimeException {

  public ObjectNotFoundException(String message) {
    super(message);
  }

  public static ObjectNotFoundException entityNotFound(Object type, String fieldName, Object id) {
    String message = String.format("%s with field %s and value %s not found", type, fieldName, id);
    return new ObjectNotFoundException(message);
  }

}
