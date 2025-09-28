package com.innowise.userservice.redis;

import java.util.List;
import java.util.Map;

public interface RedisCacheRepository {

  public Map<String, Object> getObjectsFromCache(String cacheName, List<String> keys);

  public void putObjectsInCache(String cacheName, Map<String, Object> objects);

}
