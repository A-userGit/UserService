package com.innowise.userservice.exception;

public class MultiCacheableInvalidArgsException extends RuntimeException {

  public MultiCacheableInvalidArgsException(String message) {
    super(message);
  }

  public static MultiCacheableInvalidArgsException invalidArgsException(String requiredArgName,
      String method) {
    String message = String.format(
        "Argument %s is missing in MultiCacheable annotation in method %s", requiredArgName,
        method);
    return new MultiCacheableInvalidArgsException(message);
  }

  public static MultiCacheableInvalidArgsException keyTypeNotConvertible(Class<?> type) {
    String message = String.format("Key of type %s is not convertible", type.getCanonicalName());
    return new MultiCacheableInvalidArgsException(message);
  }

  public static MultiCacheableInvalidArgsException keyIsNotList() {
    String message = "Key argument is not of type List";
    return new MultiCacheableInvalidArgsException(message);
  }

  public static MultiCacheableInvalidArgsException invalidKey(Exception e) {
    String message = String.format("Key argument is invalid %s", e.getMessage());
    return new MultiCacheableInvalidArgsException(message);
  }
}
