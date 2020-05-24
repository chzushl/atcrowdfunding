package com.shl.crowdfunding.manager.mapper;

import com.shl.crowdfunding.bean.Permission;
import com.shl.crowdfunding.bean.Role;
import com.shl.crowdfunding.bean.User;
import com.shl.crowdfunding.vo.Data;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    User selectByPrimaryKey(Integer id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

    User querUserLogin(Map<String, Object> loginHashMap);

    //Integer queryTotalSize();

    //List<User> queryDates(@Param("startpage") Integer startpage, @Param("pagesize") Integer pagesize);

    Integer queryTotalSize(Map<String, Object> map);

    List<User> queryDates(Map<String, Object> map);

    int deleteBatchUserByVo(Data data);

    List<Role> queryAllRole();

    List<Integer> queryAllUserRoleIds(Integer id);

    int saveUserRoleRelationship(@Param("userid") Integer userid, @Param("data") Data data);

    int deleteUserRoleRelationship(@Param("userid") Integer userid, @Param("data") Data data);

    List<Permission> getUserPermissionsByUserId(Integer id);
}