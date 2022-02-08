package com.example.service;

import com.example.mapper.UserMapper;
import com.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserService userService;
    
    @Autowired
    UserMapper userMapper;

    public List<User> selectByIds(String[] ids) {
        final List<User> users = userMapper.selectByIds(ids);
        return users;
    }


    /**
     * 自事务失效的原因之一就是自身调用，就是调该类自己的方法，而没有经过 Spring 的代理类，默认只有在外部调用事务才会生效。
     * 这个的解决方案之一就是在的类中注入自己，用注入的对象再调用另外一个方法，但这个不太优雅
     * @throws Exception
     */
    public void batch() throws Exception {
        // 自己注入自己，通过代理类调用saveUser，事务生效了
        userService.saveUser();
    }

    public void batch2() throws Exception {
        // 发生了自身调用，就是调该类自己的方法，事务不会生效
        this.saveUser();
    }

    @Transactional(rollbackFor = Exception.class)
    public void batch3() throws Exception {
        // 两个方法都加上@Transactional，事务生效了
        this.saveUser();
    }

    @Transactional(rollbackFor = Exception.class)
    public void batch4() throws Exception {
        // 直接在调用方法加上@Transactional，事务生效了
        // 注意这里@Transactional的用法是整体执行完成(for循环执行完之后)才会一次提交事务，直接用在saveUser方法上是每循环一次进行一次事务提交
        this.saveUser2();
    }

    public void batch5() throws Exception {
        // 虽然通过代理类调用了，但是调用的方法没有加public，事务不生效
        // @Transactional默认只能用在public方法上
        userService.saveUser3();
    }

    @Transactional(rollbackFor = Exception.class)
    public void batch6() throws Exception {
        // 直接在调用方法加上@Transactional，事务生效了
        // 注意这里@Transactional的用法是整体执行完成(for循环执行完之后)才会一次提交事务，直接用在saveUser方法上是每循环一次进行一次事务提交
        // 跟batch4()的区别就是，这里自身调用了private的saveUser4()，所以无论调用的方法是什么访问控制符修饰，事务都是生效的
        this.saveUser4();
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void saveUser() {
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setUsername("save" + i);
            int n = userMapper.save(user);
            System.out.println("save: " + n);
            user.setUsername("update" + i);
            n = userMapper.update(user);
            System.out.println("update: " + n);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(1/0);
        }
    }

    public void saveUser2() throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setUsername("save" + i);
            int n = userMapper.save(user);
            System.out.println("save: " + n);
            user.setUsername("update" + i);
            n = userMapper.update(user);
            System.out.println("update: " + n);
            Thread.sleep(3000);
            System.out.println(1/0);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    void saveUser3() throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setUsername("save" + i);
            int n = userMapper.save(user);
            System.out.println("save: " + n);
            user.setUsername("update" + i);
            n = userMapper.update(user);
            System.out.println("update: " + n);
            Thread.sleep(3000);
            System.out.println(1/0);
        }
    }

    private void saveUser4() throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setUsername("save" + i);
            int n = userMapper.save(user);
            System.out.println("save: " + n);
            user.setUsername("update" + i);
            n = userMapper.update(user);
            System.out.println("update: " + n);
            Thread.sleep(3000);
            System.out.println(1/0);
        }
    }
}
