package com.lin.service;

import com.lin.entity.RoleBean;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface RoleService {
    int insertRole(RoleBean role);
    int updateRole(RoleBean role);
    int deleteRole(Integer id);
    RoleBean getRole(Integer id);
    List<RoleBean> findRoles(String roleName, int start, int limit);
}
