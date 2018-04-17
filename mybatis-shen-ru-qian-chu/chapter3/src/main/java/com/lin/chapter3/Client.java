package com.lin.chapter3;

import com.lin.chapter3.mapper.RoleMapper;
import com.lin.chapter3.po.Role;
import com.lin.chapter3.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;

public class Client {
    public static void main(String[] args) {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            RoleMapper mapper = sqlSession.getMapper(RoleMapper.class);
            Role ro = mapper.findRole("test");
            System.err.println(ro.getRoleName());
            System.err.println(ro.getId());
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
