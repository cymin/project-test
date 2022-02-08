package com.example.mapper;

import com.example.model.User;

import java.util.List;

public interface UserMapper {
    List<User> selectByIds(String[] ids);
    
    int save(User user);
    
    int update(User user);
}
