package com.lin.chapter7.interceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.Properties;

@Intercepts({@Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = {Connection.class, Integer.class}
)})
public class QueryLimitPlugin implements Interceptor {
    private int limit;
    private String dbType;
    private static final String LMT_TABLE_NAME = "limit_table_name_xxx";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaStmtHandler = SystemMetaObject.forObject(statementHandler);
        // 分离代理对象，从而取出经过多次代理的被代理类，MyBatis使用的是JDK代理
        while (metaStmtHandler.hasGetter("h")) {
            Object object = metaStmtHandler.getValue("h");
            metaStmtHandler = SystemMetaObject.forObject(object);
        }

        // 分离最后一个代理对象的目标类
        while (metaStmtHandler.hasGetter("target")) {
            Object object = metaStmtHandler.getValue("target");
            metaStmtHandler = SystemMetaObject.forObject(object);
        }

        // 取出即将要执行的SQL
        String sql = (String) metaStmtHandler.getValue("delegate.boundSql.sql");
        String limitSql;

        // 判断参数是不是MySQL数据库且SQL有没有被插件重写过
        if ("mysql".equalsIgnoreCase(this.dbType) && !sql.contains(LMT_TABLE_NAME)) {
            sql = sql.trim();
            limitSql = "select * from (" + sql + ") " + LMT_TABLE_NAME + " limit " + limit;
            metaStmtHandler.setValue("delegate.boundSql.sql", limitSql);
        }
        // 调用原来对象的方法，进入责任链的下一层级
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        String strLimt = properties.getProperty("limit", "50");
        this.limit = Integer.parseInt(strLimt);
        this.dbType = (String) properties.getProperty("dbtype", "mysql");
    }
}
