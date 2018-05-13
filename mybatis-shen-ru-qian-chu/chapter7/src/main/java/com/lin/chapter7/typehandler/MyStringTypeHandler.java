package com.lin.chapter7.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// 定义的是JavaType类型，可以指定哪些Java类型被拦截
@MappedTypes({String.class})
// 定义的是JdbcType类型，它需要满足枚举类JdbcType所列的类型
@MappedJdbcTypes(JdbcType.VARCHAR)
public class MyStringTypeHandler implements TypeHandler<String> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, String s, JdbcType jdbcType) throws SQLException {
        System.err.println("使用我的TypeHandler");
        preparedStatement.setString(i, s);
    }

    @Override
    public String getResult(ResultSet resultSet, String s) throws SQLException {
        System.err.println("使用我的TypeHandler，ResultSet列名获取字符串");
        return resultSet.getString(s);
    }

    @Override
    public String getResult(ResultSet resultSet, int i) throws SQLException {
        System.err.println("使用我的TypeHandler，ResultSet下标获取字符串");
        return resultSet.getString(i);
    }

    @Override
    public String getResult(CallableStatement callableStatement, int i) throws SQLException {
        System.err.println("使用我的TypeHandler，callableStatement下标获取字符串");
        return callableStatement.getString(i);
    }
}
