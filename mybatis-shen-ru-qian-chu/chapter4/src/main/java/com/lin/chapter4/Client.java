package com.lin.chapter4;

import com.alibaba.fastjson.JSON;
import com.lin.chapter4.mapper.RoleMapper;
import com.lin.chapter4.mapper.StudentMapper;
import com.lin.chapter4.param.RoleParam;
import com.lin.chapter4.po.MaleStudentBean;
import com.lin.chapter4.po.Role;
import com.lin.chapter4.po.StudentBean;
import com.lin.chapter4.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class Client {
    public static void main(String[] args) {
        //testAutoMapper();
        //testAutoId();
        //testAssociation();
        //testCollection();
        //testDiscriminator();
        testCache();
    }

    private static void testCache() {
        SqlSession sqlSession = null;
        SqlSession sqlSession2 = null;
        try {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            StudentBean student = mapper.getStudent(1L);
            System.err.println("使用同一个sqlSession再执行一次");
            StudentBean student1 = mapper.getStudent(1L);
            sqlSession.commit();
            System.err.println("现在创建一个新的sqlSession再执行一次");
            sqlSession2 = SqlSessionFactoryUtil.openSqlSession();
            StudentMapper mapper1 = sqlSession2.getMapper(StudentMapper.class);
            StudentBean student2 = mapper1.getStudent(1L);
            sqlSession2.commit();
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

    private static void testDiscriminator() {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            MaleStudentBean maleStudent = (MaleStudentBean) mapper.getStudent(1L);
            System.err.println(maleStudent.getSex());
            //System.err.println(maleStudent.getStudentHealthMaleBeanList());
            //System.err.println(JSON.toJSONString(maleStudent, true));
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

    private static void testAutoMapper() {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            RoleMapper mapper = sqlSession.getMapper(RoleMapper.class);
            Role role = mapper.getRole(2L);
            System.err.println(JSON.toJSON(role));

            List<Role> roleList = mapper.findRoleByAnnotation("test", "test");
            System.err.println(JSON.toJSON(roleList));

            RoleParam roleParam = new RoleParam("test", "test");
            List<Role> roleByParams = mapper.findRoleByParams(roleParam);
            System.err.println(JSON.toJSON(roleByParams));
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

    private static void testAutoId() {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            RoleMapper mapper = sqlSession.getMapper(RoleMapper.class);
            Role role = new Role();
            role.setNote("托尔斯泰");
            role.setRoleName("孤独鳏寡");
            mapper.insertRole(role);
            System.err.println(JSON.toJSON(role));
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

    // 测试一对一级联
    private static void testAssociation() {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            StudentBean student = mapper.getStudent(1L);
            System.err.println(JSON.toJSON(student));
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

    private static void testCollection() {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            StudentBean student = mapper.getStudent(1L);
            System.err.println(JSON.toJSON(student));
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
