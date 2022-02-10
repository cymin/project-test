package com.github.redis.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * spring redis 工具类
 *
 * @author shtdsoft
 **/
@SuppressWarnings(value = {"unchecked", "rawtypes"})
@Component
public class RedisService {
    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    public RedisTemplate redisTemplate;

    @Autowired
    public StringRedisTemplate stringRedisTemplate;

    public String getStringKey(final String key) {
        final String value = stringRedisTemplate.opsForValue().get(key);
        return value;
    }

    public void setStringKey(final String key, final String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param keys  缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final T value, final String... keys) {
        String key = StringUtils.join(keys, ":");
        setCacheObject(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Long timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public <T> void setCacheObject(final T value, final Long timeout, final TimeUnit timeUnit, final String... keys) {
        String key = StringUtils.join(keys, ":");
        setCacheObject(key, value, timeout, timeUnit);
    }

    public <T> boolean setZset(final String key, final T value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    public <T> Set<T> getZsetByScore(final String key, final double min, final double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    public void removeFromZset(final String key, final Object... values) {
        redisTemplate.opsForZSet().remove(key, values);
    }

    public Long getZsetSize(final String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    public boolean expire(final long timeout, final String... keys) {
        String key = StringUtils.join(keys, ":");
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    public Long getExpire(final String... keys) {
        String key = StringUtils.join(keys, ":");
        return redisTemplate.getExpire(key);
    }

    /**
     * 设置有效时间
     *
     * @param keys    Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final long timeout, final TimeUnit unit, final String... keys) {
        String key = StringUtils.join(keys, ":");
        return expire(key, timeout, unit);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param keys 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String... keys) {
        String key = StringUtils.join(keys, ":");
        return getCacheObject(key);
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除单个对象
     *
     * @param keys
     */
    public boolean deleteObject(final String... keys) {
        String key = StringUtils.join(keys, ":");
        return deleteObject(key);
    }

    public boolean deleteAllObject(final String... keys) {
        String key = StringUtils.join(keys, ":");
        return deleteObject(key + "*");
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public long deleteObject(final Collection collection) {
        return redisTemplate.delete(collection);
    }


    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Set<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }


    public Long incrBy(Long incr, String... keys) {
        String key = StringUtils.join(keys, ":");
        return redisTemplate.opsForValue().increment(key, incr);
    }


    public Long decrBy(Long decr, String... keys) {
        String key = StringUtils.join(keys, ":");
        return redisTemplate.opsForValue().decrement(key, decr);
    }

    /**
     * 添加hash
     *
     * @param key
     * @param hashKey
     * @param value
     */
    public <T> void setHashObject(final String key, final String hashKey, final T value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }


    /**
     * 添加hash
     *
     * @param keys
     * @param hashKey
     * @param value
     */
    public <T> void setHashObject(final T value, final String hashKey, final String... keys) {
        String key = StringUtils.join(keys, ":");
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public <T> void setAllHashObject(final String key, final Map<String, T> hashMap) {
        redisTemplate.opsForHash().putAll(key, hashMap);
    }

    /**
     * 获取hash
     *
     * @param key
     * @param hashKey
     * @param <T>
     * @return
     */
    public <T> T getHashObject(final String key, final String hashKey) {
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(key, hashKey);
    }

    public <T> T getHashObject(final String hashKey, final String... keys) {
        String key = StringUtils.join(keys, ":");
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(key, hashKey);
    }

    public <T> Map<String, T> getHashObjectMap(final String key) {
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    public <T> List<T> mutiGetHashObject(final String key, Collection<String> hashKeys) {
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        return hashOperations.multiGet(key, hashKeys);
    }

    public String get(String prefix, String key) {
        final String value = stringRedisTemplate.opsForValue().get(prefix + key);
        return value;
    }

    public String get(String key) {
        final String value = stringRedisTemplate.opsForValue().get(key);
        return value;
    }

    /**
     * 批量获取值
     *
     * @param keys
     * @return
     */
    public <T> List<T> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 获取多个hash
     *
     * @param key
     * @param hashKeys
     * @param <T>
     * @return
     */
    public <T> List<T> mutiGetHashObject(final String key, final String... hashKeys) {
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        return hashOperations.multiGet(key, Arrays.asList(hashKeys));
    }

    /**
     * 获取整个hash
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> Map<String, T> getHashObjects(final String key) {
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    /**
     * 获取整个hash
     *
     * @param keys
     * @param <T>
     * @return
     */
    public <T> Map<String, T> getHashObjects(final String... keys) {
        String key = StringUtils.join(keys, ":");
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    /**
     * 删除hash
     *
     * @param key
     * @param hashKeys
     */
    public <T> void deleteHashObjects(final String key, final String... hashKeys) {
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        hashOperations.delete(key, hashKeys);
    }

    /**
     * 删除hash
     */
    public <T> void deleteHashObject(final String hashKey, final String... keys) {
        String key = StringUtils.join(keys, ":");
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        hashOperations.delete(key, hashKey);
    }

    public <T> boolean setIfAbset(final T value, final long timeout, final TimeUnit timeUnit, final String... keys) {
        String key = StringUtils.join(keys, ":");
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
    }

    /**
     * 批量添加
     */
    public void batchSet(Map<String, String> map) {
        stringRedisTemplate.opsForValue().multiSet(map);
    }

    /**
     * 批量添加并设置失效时间
     */
    public void batchSet(Map<String, String> map, final long timeout) {
        RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
        stringRedisTemplate.executePipelined(new RedisCallback<String>() {

            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                map.forEach((key, value) -> {
                    connection.set(Objects.requireNonNull(serializer.serialize(key)), Objects.requireNonNull(serializer.serialize(value)), Expiration.seconds(timeout), RedisStringCommands.SetOption.UPSERT);
                });
                return null;
            }
        }, serializer);
    }

    /**
     * 批量获取
     */
    public <T> List<T> batchGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    public boolean setNx(String key, long timeout) {
        return redisTemplate.opsForValue().setIfAbsent(key, UUID.randomUUID().toString(), timeout, TimeUnit.SECONDS);
    }
}

