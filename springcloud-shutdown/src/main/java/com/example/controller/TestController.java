package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    
    @GetMapping("/test")
    public String testConnect() throws InterruptedException {
        Thread.sleep(60_000);
        return "success";
    }
}
