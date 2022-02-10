package com.github.redis.controller;

import com.github.redis.service.RedisService;
import com.github.redis.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:
 */
@RestController
public class TestController {

    @Autowired
    private TestService testService;
    
    @Autowired
    private RedisService redisService;

    // 事务正常提交
    @RequestMapping("/test")
    public String testTransactional(String key, String value) {
        return testService.testTransactional(key, value);
    }

    // 出异常了，事务不会被提交
    @RequestMapping("/test2")
    public String testTransactional2(String key, String value) {
        return testService.testTransactional2(key, value);
    }

    // 请求期间中断程序，事务不会被提交
    @RequestMapping("/test3")
    public String testTransactional3(String key, String value) {
        return testService.testTransactional3(key, value);
    }
    
    // 不使用spring注解式事务，自己控制redis事务
    @RequestMapping("/test4")
    public List<Object> testTransactional4(String key, String value) {
        return testService.testTransactional4(key, value);
    }

    @RequestMapping("/get")
    public String getVal(String key){
        return redisService.getStringKey(key);
    }
}
