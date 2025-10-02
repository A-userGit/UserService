package com.innowise.redis.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.innowise.userservice.redis.impl.RedisCacheRepositoryImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.cache.CacheException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.data.redis.cache.RedisCacheManager;

@DisplayName("Tests for redis cache repository")
@ExtendWith(MockitoExtension.class)
public class RedisCacheRepositoryImplUnitTest {

  @Mock
  private RedisCacheManager cacheManager;

  @InjectMocks
  RedisCacheRepositoryImpl redisCacheRepository;

  @Test
  @DisplayName("Test getting value from non existent cache")
  public void getObjectsFromNonExistentCache() {
    when(cacheManager.getCache(anyString())).thenReturn(null);
    assertThrows(CacheException.class,
        () -> redisCacheRepository.putObjectsInCache("test", new HashMap<>()));
  }

  @Test
  @DisplayName("Test getting value from cache")
  public void getObjectsFromCacheTest() {
    List<String> keys = List.of("test1", "test2", "test3");
    Object value = new Object();
    Cache mockedCache = Mockito.mock(Cache.class);
    ValueWrapper mockedWrapper = Mockito.mock(ValueWrapper.class);
    when(cacheManager.getCache(anyString())).thenReturn(mockedCache);
    when(mockedCache.get(any())).thenReturn(mockedWrapper);
    when(mockedWrapper.get()).thenReturn(value);
    Map<String, Object> testResults = redisCacheRepository.getObjectsFromCache("Test", keys);
    assertEquals(3, testResults.size());
    assertTrue(testResults.keySet().stream().anyMatch(key -> key.equals("test3")));
  }

  @Test
  @DisplayName("Test putting value in cache")
  public void putObjectsInCacheTest() {
    Map<String, Object> objects = new HashMap<>();
    objects.put("test1", new Object());
    objects.put("test2", new Object());
    objects.put("test3", new Object());
    Cache mockedCache = Mockito.mock(Cache.class);
    when(cacheManager.getCache(anyString())).thenReturn(mockedCache);
    redisCacheRepository.putObjectsInCache("Test", objects);
    verify(mockedCache, times(3)).evict(anyString());
    verify(mockedCache, times(3)).put(any(), any());
  }
}
