package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class CloudShutdownApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(CloudShutdownApplication.class);
        // 指定一个文件，写入pid号
        application.addListeners(new ApplicationPidFileWriter("app.pid"));
        application.run(args);
    }
}
