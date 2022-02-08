package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/{ids}")
    public List<User> selectByIds(@PathVariable String ids) {
        final String[] split = ids.split(",");
        final List<User> users = userService.selectByIds(split);
        return users;
    }

    /**
     * spring事务测试
     * @throws Exception
     */
    @PostMapping("/batch")
    public void testTransactional() throws Exception {
        userService.batch6();
    }
}
