package com.shl.crowdfunding.manager.service.impl;

import com.shl.crowdfunding.bean.Permission;
import com.shl.crowdfunding.manager.mapper.PermissionMapper;
import com.shl.crowdfunding.manager.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> getChildrenPermissionByPid(Integer id) {
        return permissionMapper.getChildrenPermissionByPid(id);
    }

    @Override
    public Permission getRootPermission() {
        return permissionMapper.getRootPermission();
    }

    @Override
    public List<Permission> getAllallPermissions() {
        return permissionMapper.getAllallPermissions();
    }

    @Override
    public void insertPermission(Permission permission) {
        permissionMapper.insert(permission);
    }

    @Override
    public Permission getPermissionByPid(Integer id) {
        return permissionMapper.selectByPrimaryKey(id);
    }

    @Override
    public int editPermission(Permission permission) {
        return permissionMapper.updateByPrimaryKey(permission);
    }

    @Override
    public int deletePermissionById(Integer id) {
        return permissionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Integer> getPermissionByRoleid(Integer roleid) {
        return permissionMapper.getPermissionByRoleid(roleid);
    }
}
