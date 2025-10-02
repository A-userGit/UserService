package com.innowise.userservice.redis;

import java.util.List;
import java.util.Map;

public interface RedisCacheRepository {

  Map<String, Object> getObjectsFromCache(String cacheName, List<String> keys);

  void putObjectsInCache(String cacheName, Map<String, Object> objects);

}
