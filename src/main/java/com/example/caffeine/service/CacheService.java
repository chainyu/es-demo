package com.example.caffeine.service;

import java.util.Map;

public interface CacheService {

    Map<String, Object> findData(String key);
}
