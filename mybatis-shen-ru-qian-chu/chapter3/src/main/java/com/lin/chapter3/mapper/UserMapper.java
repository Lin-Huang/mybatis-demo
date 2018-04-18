package com.lin.chapter3.mapper;

import com.lin.chapter3.po.User;

public interface UserMapper {
    User getUser(Long id);
    int insertUser(User user);
}
