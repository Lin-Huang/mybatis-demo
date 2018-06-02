package com.lin.dao;

import com.lin.entity.RoleBean;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDao {
    int insertRole(RoleBean role);
    int updateRole(RoleBean role);
    int deleteRole(Integer id);
    RoleBean getRole(Integer id);
    List<RoleBean> findRoles(String roleName, RowBounds rowBounds);
}
