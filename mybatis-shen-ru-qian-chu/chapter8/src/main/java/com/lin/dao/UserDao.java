package com.lin.dao;

import com.lin.entity.UserBean;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    UserBean getUser(Integer id);
    int insertUser(UserBean user);
    int deleteUser(Integer id);
    int updateUser(UserBean user);
    List<UserBean> findUsers(String userName, RowBounds rowBounds);
}
