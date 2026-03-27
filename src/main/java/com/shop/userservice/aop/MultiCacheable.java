package com.innowise.userservice.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiCacheable {

  String value() default "cache";

  String keysParamName() default "keys";

  String keyName() default "key";
}
