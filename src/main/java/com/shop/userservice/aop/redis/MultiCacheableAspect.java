package com.innowise.userservice.aop.redis;

import com.innowise.userservice.aop.MultiCacheable;
import com.innowise.userservice.exception.MultiCacheableInvalidArgsException;
import com.innowise.userservice.redis.RedisCacheRepository;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class MultiCacheableAspect {

  private static final String ACCESS_MODIFIER_PREFIX = "get";
  private final RedisCacheRepository cacheRepository;
  private final ConversionService conversionService;

  @Around("@annotation(com.innowise.userservice.aop.MultiCacheable)")
  public Object cacheResult(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    MultiCacheable annotation = method.getAnnotation(MultiCacheable.class);
    String cacheName = annotation.value();
    String keysCollectionName = annotation.keysParamName();
    int keysParamPlace = findKeysParameterPlace(method, keysCollectionName);
    Object keyArgValue = joinPoint.getArgs()[keysParamPlace];
    List<String> keys = getKeys(keyArgValue);
    Map<String, Object> objectsFromCache = cacheRepository.getObjectsFromCache(cacheName, keys);
    keys.removeAll(objectsFromCache.keySet());
    if (keys.isEmpty()) {
      return objectsFromCache.values().stream().toList();
    }
    Class<?> keyType = ((List<?>) keyArgValue).getFirst().getClass();
    List<?> remaining = keys.stream().map(key -> conversionService.convert(key, keyType)).toList();
    Object[] args = joinPoint.getArgs();
    args[keysParamPlace] = remaining;
    List<Object> dbResult = (List<Object>) joinPoint.proceed(args);
    if (!dbResult.isEmpty()) {
      try {
        addObjectsToCache(dbResult, annotation.keyName(), annotation.value());
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
        throw MultiCacheableInvalidArgsException.invalidKey(e);
      }

    }
    List<Object> result = new ArrayList<>(dbResult);
    if (!objectsFromCache.isEmpty()) {
      result.addAll(objectsFromCache.values());
    }
    return result;
  }

  public int findKeysParameterPlace(Method method, String keysCollectionName) {
    int place = 0;
    boolean isPresent = false;
    for (Parameter parameter : method.getParameters()) {
      if (parameter.getName().matches(keysCollectionName)) {
        isPresent = true;
        break;
      }
      place++;
    }
    if (!isPresent) {
      throw MultiCacheableInvalidArgsException.invalidArgsException(keysCollectionName,
          method.getName());
    }
    return place;
  }

  public List<String> getKeys(Object data) {
    List<?> listOfKeys;
    try {
      listOfKeys = (List<?>) data;
    } catch (ClassCastException e) {
      throw MultiCacheableInvalidArgsException.keyIsNotList();
    }
    if (listOfKeys.isEmpty()) {
      return new ArrayList<>();
    }
    Class<?> keyType = listOfKeys.getFirst().getClass();
    validateConversionAvailability(keyType);
    return listOfKeys.stream().
        map(key -> (String) conversionService.convert(key, TypeDescriptor.valueOf(String.class)))
        .collect(Collectors.toList());
  }

  public void addObjectsToCache(List<?> values, String keyName, String cacheName)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Map<String, Object> keyValueRepository = new HashMap<>();
    for (Object value : values) {
      Object currentValue = value;
      Class<?> currentType = value.getClass();
      for (String name : keyName.split("\\.")) {
        String methodName = String.format("%s%s", ACCESS_MODIFIER_PREFIX,
            StringUtils.capitalize(name));
        currentValue = currentType.getMethod(methodName).invoke(currentValue);
        currentType = currentValue.getClass();
      }
      validateConversionAvailability(currentType);
      String key = conversionService.convert(currentValue, String.class);
      keyValueRepository.put(key, value);
    }
    cacheRepository.putObjectsInCache(cacheName, keyValueRepository);
  }

  public void validateConversionAvailability(Class<?> keyType) {
    boolean typeConvertible = conversionService.canConvert(keyType, String.class);
    if (!typeConvertible) {
      throw MultiCacheableInvalidArgsException.keyTypeNotConvertible(keyType);
    }
  }
}