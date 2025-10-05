package com.innowise.userservice.unit.aop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.innowise.userservice.aop.MultiCacheable;
import com.innowise.userservice.aop.redis.MultiCacheableAspect;
import com.innowise.userservice.entity.User;
import com.innowise.userservice.exception.MultiCacheableInvalidArgsException;
import com.innowise.userservice.redis.RedisCacheRepository;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

@DisplayName("Tests for redis aspect")
@ExtendWith(MockitoExtension.class)
public class MultiCacheableAspectUnitTest {

  @Mock
  private RedisCacheRepository cacheRepository;
  @Mock
  private ConversionService conversionService;

  @InjectMocks
  MultiCacheableAspect multiCacheableAspect;

  @Test
  @DisplayName("test cacheResult with no db data addition")
  public void cacheResultTestCacheOnly() {
    ProceedingJoinPoint mockedJointPoint = Mockito.mock(ProceedingJoinPoint.class);
    MethodSignature mockedMethodSignature = Mockito.mock(MethodSignature.class);
    Method mockedMethod = Mockito.mock(Method.class);
    MultiCacheable mockedAnnotation = Mockito.mock(MultiCacheable.class);
    Parameter mockedParameter = Mockito.mock(Parameter.class);
    Parameter[] params = new Parameter[1];
    params[0] = mockedParameter;
    List<Long> ids = List.of(1L);
    Object[] args = new Object[1];
    args[0] = ids;
    Map<String, Object> cachedResult = new HashMap<>();
    cachedResult.put(Long.valueOf(1L).toString(), new Object());
    when(mockedJointPoint.getSignature()).thenReturn(mockedMethodSignature);
    when(mockedMethodSignature.getMethod()).thenReturn(mockedMethod);
    when(mockedMethod.getAnnotation(any())).thenReturn(mockedAnnotation);
    when(mockedAnnotation.keysParamName()).thenReturn("ids");
    when(mockedAnnotation.value()).thenReturn("test");
    when(mockedMethod.getParameters()).thenReturn(params);
    when(mockedParameter.getName()).thenReturn("ids");
    when(mockedJointPoint.getArgs()).thenReturn(args);
    when(conversionService.canConvert(Long.class, String.class)).thenReturn(true);
    when(conversionService.convert(1L, TypeDescriptor.valueOf(String.class))).thenReturn("testKey");
    when(cacheRepository.getObjectsFromCache("test", List.of("testKey"))).thenReturn(cachedResult);
    try {
      when(mockedJointPoint.proceed(any())).thenReturn(new ArrayList<>());
    } catch (Throwable e) {
      fail("test cache result fail. Reason: " + e.getMessage());
    }
    try {
      List<Object> fullResult = (List<Object>) multiCacheableAspect.cacheResult(mockedJointPoint);
      assertEquals(1, fullResult.size());
    } catch (Throwable e) {
      fail("test cache result fail. Reason: " + e.getMessage());
    }
  }

  @Test
  @DisplayName("test cacheResult with no db data addition")
  public void cacheResultTestFull() {
    ProceedingJoinPoint mockedJointPoint = Mockito.mock(ProceedingJoinPoint.class);
    MethodSignature mockedMethodSignature = Mockito.mock(MethodSignature.class);
    Method mockedMethod = Mockito.mock(Method.class);
    MultiCacheable mockedAnnotation = Mockito.mock(MultiCacheable.class);
    Parameter mockedParameter = Mockito.mock(Parameter.class);
    Parameter[] params = new Parameter[1];
    params[0] = mockedParameter;
    List<Long> ids = List.of(1L, 2L);
    Object[] args = new Object[1];
    args[0] = ids;
    Map<String, Object> cachedResult = new HashMap<>();
    cachedResult.put(Long.valueOf(1L).toString(), new User());
    List<Object> dbResult = List.of(new User());
    when(mockedJointPoint.getSignature()).thenReturn(mockedMethodSignature);
    when(mockedMethodSignature.getMethod()).thenReturn(mockedMethod);
    when(mockedMethod.getAnnotation(any())).thenReturn(mockedAnnotation);
    when(mockedAnnotation.keysParamName()).thenReturn("ids");
    when(mockedAnnotation.value()).thenReturn("test");
    when(mockedAnnotation.keyName()).thenReturn("id");
    when(mockedMethod.getParameters()).thenReturn(params);
    when(mockedParameter.getName()).thenReturn("ids");
    when(mockedJointPoint.getArgs()).thenReturn(args);
    when(conversionService.canConvert(Long.class, String.class)).thenReturn(true);
    when(conversionService.convert(1L, TypeDescriptor.valueOf(String.class))).thenReturn("1");
    when(conversionService.convert(2L, TypeDescriptor.valueOf(String.class))).thenReturn("2");
    when(cacheRepository.getObjectsFromCache("test", List.of("1", "2"))).thenReturn(cachedResult);
    try {
      when(mockedJointPoint.proceed(any())).thenReturn(dbResult);
    } catch (Throwable e) {
      fail("test cache result fail. Reason: " + e.getMessage());
    }
    try {
      List<Object> fullResult = (List<Object>) multiCacheableAspect.cacheResult(mockedJointPoint);
      assertEquals(2, fullResult.size());
    } catch (Throwable e) {
      fail("test cache result fail. Reason: " + e.getMessage());
    }
  }

  @Test
  @DisplayName("Test finding parameter in method args")
  public void findKeysParameterPlaceTest() {
    Method mockedMethod = Mockito.mock(Method.class);
    Parameter mockedParameter = Mockito.mock(Parameter.class);
    Parameter mockedFalseParameter = Mockito.mock(Parameter.class);
    Parameter[] params = new Parameter[3];
    params[1] = mockedParameter;
    params[0] = mockedFalseParameter;
    when(mockedMethod.getParameters()).thenReturn(params);
    when(mockedParameter.getName()).thenReturn("test");
    when(mockedFalseParameter.getName()).thenReturn("test1");
    int test = multiCacheableAspect.findKeysParameterPlace(mockedMethod, "test");
    assertEquals(1, test);
  }

  @Test
  @DisplayName("Test finding parameter in method args with no such parameter")
  public void findKeysParameterPlaceTestFailNoParam() {
    Method mockedMethod = Mockito.mock(Method.class);
    Parameter mockedFalseParameter = Mockito.mock(Parameter.class);
    when(mockedFalseParameter.getName()).thenReturn("test1");
    Parameter[] params = new Parameter[1];
    params[0] = mockedFalseParameter;
    when(mockedMethod.getParameters()).thenReturn(params);
    assertThrows(MultiCacheableInvalidArgsException.class,
        () -> multiCacheableAspect.findKeysParameterPlace(mockedMethod, "test"));
  }

  @Test
  @DisplayName("Test getting keys from parameter with wrong type")
  public void getKeysTestTypeFail() {
    String wrongObject = "";
    Exception exception = assertThrows(MultiCacheableInvalidArgsException.class,
        () -> multiCacheableAspect.getKeys(wrongObject));
    assertEquals("Key argument is not of type List", exception.getMessage());
  }

  @Test
  @DisplayName("Test getting unconvertible keys from parameter")
  public void getKeysTestFailUnconvertible() {
    List<Long> keys = List.of(1L, 2L, 3L);
    when(conversionService.canConvert(Long.class, String.class)).thenReturn(false);
    Exception exception = assertThrows(MultiCacheableInvalidArgsException.class,
        () -> multiCacheableAspect.getKeys(keys));
    assertEquals("Key of type java.lang.Long is not convertible", exception.getMessage());
  }

  @Test
  @DisplayName("Test getting keys from parameter")
  public void getKeysTest() {
    List<Long> keys = List.of(1L, 2L, 3L);
    when(conversionService.canConvert(Long.class, String.class)).thenReturn(true);
    List<String> keysString = multiCacheableAspect.getKeys(keys);
    assertEquals(3, keysString.size());
  }

  @Test
  @DisplayName("Test add object to cache key")
  public void addObjectsToCache() {
    List<Object> objects = new ArrayList<>();
    objects.add(new Object());
    when(conversionService.convert(any(Object.class), any(Class.class))).thenReturn("Key");
    when(conversionService.canConvert(any(Class.class), any(Class.class))).thenReturn(true);
    try {
      multiCacheableAspect.addObjectsToCache(objects, "class", "test");
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
      fail("test add object to cache fail. Reason: " + e.getMessage());
    }
    verify(cacheRepository).putObjectsInCache(anyString(), anyMap());
  }

  @Test
  @DisplayName("Test add object to cache with invalid key")
  public void addObjectsToCacheFail() {
    List<Object> objects = new ArrayList<>();
    objects.add(new Object());
    assertThrows(NoSuchMethodException.class,
        () -> multiCacheableAspect.addObjectsToCache(objects, "string", "test"));
  }
}
