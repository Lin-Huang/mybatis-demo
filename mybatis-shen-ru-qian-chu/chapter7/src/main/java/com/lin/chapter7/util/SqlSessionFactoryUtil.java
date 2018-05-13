package com.lin.chapter7.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 每个MyBatis的应用都是以SqlSessionFactory的实例为中心
 * SqlSessionFactory的责任就是创建SqlSession，采用单例模式
 * 正确的做法是，每一个数据库只对应一个SqlSessionFactory,管理好数据据资源的分配，
 * 避免过多的Connection被消耗
 */
public class SqlSessionFactoryUtil {
    // SqlSessionFactory对象
    private static SqlSessionFactory sqlSessionFactory = null;
    // 类线程锁
    private static final Class CLASS_LOCK = SqlSessionFactoryUtil.class;

    /**
     * 私有化构造参数
     */
    private SqlSessionFactoryUtil(){

    }

    public static SqlSessionFactory initSqlSessionFactory(){
        // 编译后resource下的文件会在根目录下
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try{
            inputStream = Resources.getResourceAsStream(resource);
        }catch (IOException ex){
            Logger.getLogger(SqlSessionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

        synchronized (CLASS_LOCK){
            if (sqlSessionFactory == null) {
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            }
        }
        return sqlSessionFactory;
    }

    public static SqlSession openSqlSession(){
        if (sqlSessionFactory == null) {
            initSqlSessionFactory();
        }
        return sqlSessionFactory.openSession();
    }
}
