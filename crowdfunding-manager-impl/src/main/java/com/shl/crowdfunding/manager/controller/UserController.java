package com.shl.crowdfunding.manager.controller;

import com.shl.crowdfunding.bean.Role;
import com.shl.crowdfunding.bean.User;
import com.shl.crowdfunding.manager.service.UserService;
import com.shl.crowdfunding.util.AjaxResult;
import com.shl.crowdfunding.util.Page;
import com.shl.crowdfunding.util.StringUtil;
import com.shl.crowdfunding.vo.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public String toIndex(){
        return "user/index";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "user/add";
    }

    @RequestMapping("/assignRole")
    public String assignRole(Integer id,Map map){
        List<Role> allRoleList = userService.queryAllRole();
        List<Integer> allUserRoleIds = userService.queryAllUserRoleIds(id);
        List<Role> leftRoleList = new ArrayList<>();
        List<Role> rightRoleList = new ArrayList<>();
        for (Role role : allRoleList) {
            if(allUserRoleIds.contains(role.getId())){
                rightRoleList.add(role);
            }else {
                leftRoleList.add(role);
            }
        }
        map.put("rightRoleList",rightRoleList);
        map.put("leftRoleList",leftRoleList);
        return "user/assignrole";
    }

    @ResponseBody
    @RequestMapping("/add")
    public Object add(User user){
        AjaxResult ajaxResult = new AjaxResult();
        try {
            int count = userService.insertUser(user);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            ajaxResult.setMessage("数据添加失败");
        }
        return ajaxResult;
    }
    /*同步请求*/
   /* @RequestMapping("/index")
    public String index(@RequestParam(value = "pageno",required = false,defaultValue = "1") Integer pageno, @RequestParam(value = "pagesize",required = false,defaultValue = "10") Integer pagesize, Map map){
        if(pageno<=0)
            pageno = 1;
        if(pagesize<=0)
            pagesize = 10;
        Page page = userService.queryUserPage(pageno,pagesize);
        map.put("page",page);
        return "user/index";
    }*/

   /*异步请求*/
/*    @ResponseBody
    @RequestMapping("/index")
    public Object index(@RequestParam(value = "pageno",required = false,defaultValue = "1") Integer pageno, @RequestParam(value = "pagesize",required = false,defaultValue = "10") Integer pagesize){
        if(pageno<=0)
            pageno = 1;
        if(pagesize<=0)
            pagesize = 10;
        AjaxResult ajaxResult = new AjaxResult();
        try {
            Page page = userService.queryUserPage(pageno,pagesize);
            ajaxResult.setSuccess(true);
            ajaxResult.setPage(page);
        } catch (Exception e) {
            ajaxResult.setMessage("查询数据失败");
        }
        return ajaxResult;
    }*/
    /*模糊查询*/
   @ResponseBody
   @RequestMapping("/doIndex")
   public Object index(@RequestParam(value = "pageno",required = false,defaultValue = "1") Integer pageno, @RequestParam(value = "pagesize",required = false,defaultValue = "10") Integer pagesize,String queryText){
       Map<String,Object> map = new HashMap<>();
       if(pageno<=0)
           pageno = 1;
       if(pagesize<=0)
           pagesize = 10;

       AjaxResult ajaxResult = new AjaxResult();
       try {
           map.put("pageno",pageno);
           map.put("pagesize",pagesize);
           if(StringUtil.isNotEmpty(queryText)){
               if(queryText.contains("%")){
                   queryText = queryText.replaceAll("%", "\\\\%");
               }
               map.put("queryText",queryText);
           }
           Page page = userService.queryUserPage(map);
           ajaxResult.setSuccess(true);
           ajaxResult.setPage(page);
       } catch (Exception e) {
           ajaxResult.setMessage("数据查询失败");
       }
       return ajaxResult;
   }

    //分配角色
    @ResponseBody
    @RequestMapping("/doAssignRole")
    public Object doAssignRole(Integer userid,Data data){
        AjaxResult ajaxResult = new AjaxResult();
        try {
            int count = userService.saveUserRoleRelationship(userid,data);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            ajaxResult.setMessage("角色分配失败");
        }
        return ajaxResult;
    }
    //取消分配角色
    @ResponseBody
    @RequestMapping("/doUnAssignRole")
    public Object doUnAssignRole(Integer userid,Data data){
        AjaxResult ajaxResult = new AjaxResult();
        try {
            int count = userService.deleteUserRoleRelationship(userid,data);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            ajaxResult.setMessage("取消角色分配失败");
        }
        return ajaxResult;
    }
    @RequestMapping("/toEdit")
    public String toEdit(Integer id,Map map){
       User user = userService.findUserByid(id);
       map.put("editUser",user);
       return "user/edit";
    }

    @ResponseBody
    @RequestMapping("/edit")
    public Object edit(User user){
        AjaxResult ajaxResult = new AjaxResult();
        try {
            int count = userService.editUser(user);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            ajaxResult.setMessage("数据查询失败");
        }
        return ajaxResult;
    }
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(Integer id){
        AjaxResult ajaxResult = new AjaxResult();
        try {
            int count = userService.deleteUser(id);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            ajaxResult.setMessage("查询删除失败");
        }
        return ajaxResult;
    }
    //使用拼接字符串的方式接受数据
   /* @ResponseBody
    @RequestMapping("/deleteBatch")
    public Object deleteBatch(Integer[] id){
        AjaxResult ajaxResult = new AjaxResult();
        try {
            int count = userService.deleteBatchUser(id);
            ajaxResult.setSuccess(count==id.length);
        } catch (Exception e) {
            ajaxResult.setMessage("批量删除失败");
        }
        return ajaxResult;
    }*/
    //使用vo集合对象方式接受数据
    @ResponseBody
    @RequestMapping("/deleteBatch")
    public Object deleteBatch(Data data){
        AjaxResult ajaxResult = new AjaxResult();
        try {
            int count = userService.deleteBatchUserByVo(data);
            ajaxResult.setSuccess(count==data.getDatas().size());
        } catch (Exception e) {
            ajaxResult.setMessage("批量删除失败");
        }
        return ajaxResult;
    }
}
