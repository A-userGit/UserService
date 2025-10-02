package com.innowise.userservice.redis.impl;

import com.innowise.userservice.redis.RedisCacheRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.CacheException;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;

@Component
@Getter
@RequiredArgsConstructor
public class RedisCacheRepositoryImpl implements RedisCacheRepository {

  private final RedisCacheManager cacheManager;

  @Override
  public Map<String, Object> getObjectsFromCache(String cacheName, List<String> keys) {
    Map<String, Object> result = new HashMap<>();
    Cache cache = cacheManager.getCache(cacheName);
    if (cache == null) {
      throw new CacheException("Cache" + cacheName + " is missing");
    }
    for (String key : keys) {
      ValueWrapper valueWrapper = cache.get(key);
      if (valueWrapper != null) {
        result.put(key, valueWrapper.get());
      }
    }
    return result;
  }

  @Override
  public void putObjectsInCache(String cacheName, Map<String, Object> objects) {
    Cache cache = cacheManager.getCache(cacheName);
    if (cache == null) {
      throw new CacheException("Cache" + cacheName + " is missing");
    }
    for (String key : objects.keySet()) {
      cache.evict(key);
      cache.put(key, objects.get(key));
    }
  }
}
