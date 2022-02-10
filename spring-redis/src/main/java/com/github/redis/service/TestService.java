package com.github.redis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestService {
    private static final Logger logger = LoggerFactory.getLogger(TestService.class);

    @Autowired
    RedisService redisService;
    
    @Autowired
    RedisTemplate redisTemplate;

    @Transactional
    public String testTransactional(final String key, final String value) {
        // 事务正常提交
        redisService.setStringKey(key, value);
        redisService.setStringKey(key + 2, value);
        return value;
    }

    @Transactional(rollbackFor = Exception.class)
    public String testTransactional2(final String key, final String value) {
        redisService.setStringKey(key, value);
        // 出异常了，事务不会被提交
        System.out.println(1 / 0);
        redisService.setStringKey(key + 2, value);
        return value;
    }

    @Transactional(rollbackFor = Exception.class)
    public String testTransactional3(final String key, final String value) {
        redisService.setStringKey(key, value);
        // 请求期间中断程序，事务不会被提交
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        redisService.setStringKey(key + 2, value);
        return value;
    }

    // 不使用spring注解式事务，自己控制redis事务
    public List<Object>  testTransactional4(final String key, final String value) {
        List<Object> txResults = (List<Object>) redisTemplate.execute(new SessionCallback<List<Object>>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForSet().add(key, value);
                operations.opsForSet().add(key + 2, value);
                // 出异常了，事务不会提交
                System.out.println(1/0);
                return operations.exec();
            }
        });
        return txResults;
    }
}
