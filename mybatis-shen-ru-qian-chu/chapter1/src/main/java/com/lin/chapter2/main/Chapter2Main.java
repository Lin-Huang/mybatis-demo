package com.lin.chapter2.main;

import com.lin.chapter2.mapper.RoleMapper;
import com.lin.chapter2.po.Role;
import com.lin.chapter2.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;

public class Chapter2Main {
    public static void main(String[] args) {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            // 尽量在一个SqlSession事务的方法中使用，然后废弃掉
            RoleMapper mapper = sqlSession.getMapper(RoleMapper.class);
            Role role = new Role();
            role.setRoleName("testName");
            role.setNote("testNode");
            mapper.insertRole(role);
            mapper.deleteRole(1L);
            sqlSession.commit();
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
            sqlSession.rollback();
        }finally {
            // 每次创建的SqlSession都必须及时关闭它
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}
