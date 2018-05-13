package com.lin.chapter7.databaseid;

import org.apache.ibatis.mapping.DatabaseIdProvider;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 自定义数据厂商标识
 * dataBaseIdProvider数据库厂商标识
 * Mybatis可能会运行在不同的数据中，它为此提供一个数据库标识，并提供自定义，
 * 它的作用在于指定SQL到对应的数据库厂商提供的数据库中运行
 */
public class MyDataBaseIdProvider implements DatabaseIdProvider {
    private Properties properties = null;

    @Override
    public void setProperties(Properties p) {
        // 这里是我们在XML的databaseIdProvider标签里面配置的信息
        this.properties = p;
    }

    @Override
    public String getDatabaseId(DataSource dataSource) throws SQLException {
        // 获取数据库的名称
        String dbName = dataSource.getConnection().getMetaData().getDatabaseProductName();
        return (String) this.properties.get(dbName);
    }
}
/*
    <databaseIdProvider type="com.lin.chapter3.databaseid.MyDataBaseIdProvider">
        <property name="SQL Server" value="sqlserver" />
        <property name="MySQL" value="mysql" />
        <property name="DB2" value="db2" />
        <property name="Oracle" value="oracle" />
    </databaseIdProvider>
 */
