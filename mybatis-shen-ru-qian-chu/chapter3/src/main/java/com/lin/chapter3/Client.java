package com.lin.chapter3;

import com.lin.chapter3.enums.Sex;
import com.lin.chapter3.enums.State;
import com.lin.chapter3.mapper.RoleMapper;
import com.lin.chapter3.mapper.UserMapper;
import com.lin.chapter3.po.Role;
import com.lin.chapter3.po.User;
import com.lin.chapter3.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.Date;

public class Client {
    public static void main(String[] args) {
        testEnumOrdinalTypeHandler();
    }

    public static void testEnumOrdinalTypeHandler() {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = new User();
            user.setUserName("zhangsan");
            user.setCnname("张三");
            user.setMobile("188888888888");
            user.setSex(Sex.MALE);
            user.setEmail("zhangsan@163.com");
            user.setNote("test EnumOrdinalTypeHandler");
            user.setBirthday(new Date());
            user.setState(State.FAILURE);
            userMapper.insertUser(user);
            User user1 = userMapper.getUser(1L);
            System.err.println(user1.getSex());
            sqlSession.commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            if (sqlSession != null)
                sqlSession.rollback();
        } finally {
            // 每次创建的SqlSession都必须及时关闭它
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    public static void testMyTypeHandler() {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            RoleMapper mapper = sqlSession.getMapper(RoleMapper.class);
            Role ro = mapper.findRole("test");
            System.err.println(ro.getRoleName());
            System.err.println(ro.getId());
            sqlSession.commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            if (sqlSession != null)
                sqlSession.rollback();
        } finally {
            // 每次创建的SqlSession都必须及时关闭它
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}
