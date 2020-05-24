package com.shl.crowdfunding.manager.mapper;

import com.shl.crowdfunding.bean.Permission;
import java.util.List;

public interface PermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Permission record);

    Permission selectByPrimaryKey(Integer id);

    List<Permission> selectAll();

    int updateByPrimaryKey(Permission record);

    List<Permission> getChildrenPermissionByPid(Integer id);

    Permission getRootPermission();

    List<Permission> getAllallPermissions();

    List<Integer> getPermissionByRoleid(Integer roleid);
}