package com.lin.chapter7;

import com.lin.chapter7.mapper.RoleMapper;
import com.lin.chapter7.mapper.UserMapper;
import com.lin.chapter7.po.Role;
import com.lin.chapter7.po.User;
import com.lin.chapter7.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;

public class Client {
    public static void main(String[] args) {
        testEnumOrdinalTypeHandler();
    }

    public static void testEnumOrdinalTypeHandler() {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            /*User user = new User();
            user.setUserName("zhangsan");
            user.setCnname("张三");
            user.setMobile("188888888888");
            user.setSex(Sex.MALE);
            user.setEmail("zhangsan@163.com");
            user.setNote("test EnumOrdinalTypeHandler");
            user.setBirthday(new Date());
            user.setState(State.FAILURE);
            userMapper.insertUser(user);*/
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
