package com.lin.chapter4.mapper;

import com.lin.chapter4.param.RoleParam;
import com.lin.chapter4.po.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mapper是一个接口，而没有任何实现类，它的作用是发送SQL，然后返回结果
 * 相对而言，它可以进一步屏蔽SqlSession这个对象，使得它具有更强的业务可读性
 * Mapper的运行，是运用Java语言的动态代理去实现的
 * Mybatis会为这个接口生成代理类对象，代理对象会根据“接口全路径+方法名”去匹配，
 * 找到对应的XML文件（或注解）去完成它所需要的任务，返回我们需要的结果。
 */
public interface RoleMapper {
    Role getRole(Long id);

    /**
     *使用@Param注解传递多个参数，当参数个数n<=5时，它是最佳的传参方式
     */
    List<Role> findRoleByAnnotation(@Param("roleName") String roleName,
                                    @Param("note") String note);

    /**
     *当参数个数多于5个时，建议使用JavaBean方式
     */
    List<Role> findRoleByParams(RoleParam param);
}
