package com.lin.chapter7.mapper;

import com.lin.chapter7.po.User;

public interface UserMapper {
    User getUser(Long id);
    int insertUser(User user);
}
