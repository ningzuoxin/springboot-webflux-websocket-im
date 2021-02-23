package com.ning.repo;

import com.ning.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Set;

@Repository
public class SubscribeRepo {

    private static final String TEMP_KEY = "IM_TEMP_";

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public SubscribeRepo(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public synchronized void createBusinessSubscribe(String id, String ttl) {
        redisTemplate.opsForSet().add(generateRedisKey(id), id);
        redisTemplate.expire(id, Duration.ofMinutes(Long.parseLong(ttl)));
    }

    public synchronized void destroyBusinessSubscribe(String id) {
        redisTemplate.delete(generateRedisKey(id));
    }

    public void subscribe(String key, String deviceId) {
        deviceId = Utils.URLDecoderParam(deviceId);
        redisTemplate.opsForSet().add(generateRedisKey(key), deviceId);
    }

    public void unsubscribe(String key, String deviceId) {
        deviceId = Utils.URLDecoderParam(deviceId);
        redisTemplate.opsForSet().remove(generateRedisKey(key), deviceId);
    }

    public Set<String> findDevicesByBusinessId(String id) {
        Boolean hasHey = redisTemplate.hasKey(generateRedisKey(id));
        if (hasHey != null && hasHey) {
            return redisTemplate.opsForSet().members(generateRedisKey(id));
        }
        return null;
    }

    public static String generateRedisKey(String key) {
        return TEMP_KEY + key;
    }
}
