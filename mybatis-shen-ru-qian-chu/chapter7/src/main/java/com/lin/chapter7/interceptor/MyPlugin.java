package com.lin.chapter7.interceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.Properties;

/**
 * 其中@Intercepts说明它是一个拦截器。@Signature是注册拦截器签名的地方，只有签名满足条件才能拦截，
 * type可以是四大对象中的一个，这里是StatementHandler。
 * method代表要拦截四大对象的某一接口方法，而args则表示该方法的参数
 */
@Intercepts({@Signature(
        type = StatementHandler.class, //确定要拦截的对象
        method = "prepare",  // 确定要拦截的方法
        args = {Connection.class, Integer.class})}) // 拦截方法的参数
public class MyPlugin implements Interceptor{
    Properties props = null;

    /**
     * 代替拦截对象方法的内容
     * @param invocation 责任链对象
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.err.println("Before...");
        // 如果当前代理的是一个非代理对象，那么它就回调用真实拦截对象的方法，
        // 如果不是，它会回调下个插件代理对象的invoke方法
        Object obj = invocation.proceed();
        System.err.println("After...");
        return obj;
    }

    /**
     * 生成对象的代理，这里常用MyBatis提供的Plugin类的wrap方法
     * @param target 被代理的对象
     */
    @Override
    public Object plugin(Object target) {
        // 使用MyBatis提供的Plugin类生成代理对象
        System.err.println("调用生成代理对象...");
        return Plugin.wrap(target, this);
    }


    /**
     * 获取插件配置的属性，我们在MyBatis的配置里面去配置
     * @param properties props是MyBatis配置的参数
     */
    @Override
    public void setProperties(Properties properties) {
        System.err.println(properties.get("dbType"));
        this.props = properties;
    }
}
