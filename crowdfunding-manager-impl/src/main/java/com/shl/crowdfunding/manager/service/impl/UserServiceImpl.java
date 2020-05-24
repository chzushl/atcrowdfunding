package com.shl.crowdfunding.manager.service.impl;

import com.shl.crowdfunding.bean.Permission;
import com.shl.crowdfunding.bean.Role;
import com.shl.crowdfunding.bean.User;
import com.shl.crowdfunding.exception.LoginFailException;
import com.shl.crowdfunding.manager.mapper.UserMapper;
import com.shl.crowdfunding.manager.service.UserService;
import com.shl.crowdfunding.util.MD5Util;
import com.shl.crowdfunding.util.Page;
import com.shl.crowdfunding.vo.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

   /* @Override
    public Page queryUserPage(Integer pageno, Integer pagesize) {
        Page page = new Page();
        page.setPageno(pageno);
        page.setPagesize(pagesize);

        Integer totalSize = userMapper.queryTotalSize();
        page.setTotalsize(totalSize);
        Integer startpage = page.getStartPage();
        List<User> datas = userMapper.queryDates(startpage,pagesize);
        page.setDatas(datas);
        return page;
    }*/

    @Override
    public User querUserLogin(Map<String, Object> loginHashMap) {
        User user = userMapper.querUserLogin(loginHashMap);
        if (user == null) {
            throw new LoginFailException("用户帐号密码错误");
        }
        return user;
    }

    @Override
    public Page queryUserPage(Map<String, Object> map) {
        Page page = new Page((Integer)map.get("pageno"),(Integer)map.get("pagesize"));

        Integer startpage = page.getStartPage();
        map.put("startpage",startpage);


        Integer totalSize = userMapper.queryTotalSize(map);
        page.setTotalsize(totalSize);

        List<User> datas = userMapper.queryDates(map);
        page.setDatas(datas);
        return page;
    }

    @Override
    public int insertUser(User user) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
        user.setCreatetime(format);
        user.setUserpswd(MD5Util.digest("123"));
        int count = userMapper.insert(user);
        return count;
    }

    @Override
    public User findUserByid(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }

    @Override
    public int editUser(User user) {
        int i = userMapper.updateByPrimaryKey(user);
        return i;
}

    @Override
    public int deleteUser(Integer id) {
        int i = userMapper.deleteByPrimaryKey(id);
        return i;
    }

    @Override
    public int deleteBatchUser(Integer[] ids) {
        int count = 0;
        for (Integer id : ids) {
            int i = userMapper.deleteByPrimaryKey(id);
            count+=i;
        }
        return count;
    }

    @Override
    public int deleteBatchUserByVo(Data data) {
        return userMapper.deleteBatchUserByVo(data);
    }

    @Override
    public List<Role> queryAllRole() {
        return userMapper.queryAllRole();
    }

    @Override
    public List<Integer> queryAllUserRoleIds(Integer id) {
        return userMapper.queryAllUserRoleIds(id);
    }

    @Override
    public int saveUserRoleRelationship(Integer userid, Data data) {
        return userMapper.saveUserRoleRelationship(userid,data);
    }

    @Override
    public int deleteUserRoleRelationship(Integer userid, Data data) {
        return userMapper.deleteUserRoleRelationship(userid,data);
    }

    @Override
    public List<Permission> getUserPermissionsByUserId(Integer id) {
        return userMapper.getUserPermissionsByUserId(id);
    }
}
