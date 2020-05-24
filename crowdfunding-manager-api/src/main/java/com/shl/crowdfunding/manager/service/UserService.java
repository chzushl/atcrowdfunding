package com.shl.crowdfunding.manager.service;

import com.shl.crowdfunding.bean.Permission;
import com.shl.crowdfunding.bean.Role;
import com.shl.crowdfunding.bean.User;
import com.shl.crowdfunding.util.Page;
import com.shl.crowdfunding.vo.Data;

import java.util.List;
import java.util.Map;

public interface UserService {
    User querUserLogin(Map<String, Object> loginHashMap);

    //Page queryUserPage(Integer pageno, Integer pagesize);

    Page queryUserPage(Map<String, Object> map);

    int insertUser(User user);

    User findUserByid(Integer id);

    int editUser(User user);

    int deleteUser(Integer id);

    int deleteBatchUser(Integer[] ids);

    int deleteBatchUserByVo(Data data);

    List<Role> queryAllRole();

    List<Integer> queryAllUserRoleIds(Integer id);

    int saveUserRoleRelationship(Integer userid, Data data);

    int deleteUserRoleRelationship(Integer userid, Data data);

    List<Permission> getUserPermissionsByUserId(Integer id);
}
