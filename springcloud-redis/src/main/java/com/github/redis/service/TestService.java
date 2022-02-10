package com.github.redis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService {
    private static final Logger logger = LoggerFactory.getLogger(TestService.class);

    @Autowired
    RedisService redisService;

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

}
