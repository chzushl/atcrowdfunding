package com.shl.crowdfunding.manager.service;

import com.shl.crowdfunding.bean.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> getChildrenPermissionByPid(Integer id);

    Permission getRootPermission();

    List<Permission> getAllallPermissions();

    void insertPermission(Permission permission);

    Permission getPermissionByPid(Integer id);

    int editPermission(Permission permission);

    int deletePermissionById(Integer id);

    List<Integer> getPermissionByRoleid(Integer roleid);
}
