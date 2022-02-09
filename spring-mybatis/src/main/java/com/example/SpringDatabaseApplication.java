package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.example.mapper")
@EnableAspectJAutoProxy(exposeProxy = true) // "自调用”的实现方式
@EnableTransactionManagement //开启本地事务
public class SpringDatabaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringDatabaseApplication.class, args);
    }

}
