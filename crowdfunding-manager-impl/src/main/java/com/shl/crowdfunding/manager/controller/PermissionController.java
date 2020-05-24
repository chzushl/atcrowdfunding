package com.shl.crowdfunding.manager.controller;

import com.shl.crowdfunding.bean.Permission;
import com.shl.crowdfunding.manager.service.PermissionService;
import com.shl.crowdfunding.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @RequestMapping("/index")
    public String index() {
        return "permission/index";
    }

    @RequestMapping("/toAdd")
    public String toAdd() {
        return "permission/add";
    }

    @RequestMapping("/toEdit")
    public String toEdit(Integer id,Map map) {
       Permission permission = permissionService.getPermissionByPid(id);
       map.put("permisssion",permission);
        return "permission/edit";
    }
    @ResponseBody
    @RequestMapping("/doDelete")
    public Object doDelete(Integer id) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            int count = permissionService.deletePermissionById(id);
            ajaxResult.setSuccess(1==count);
        } catch (Exception e) {
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("许可书加载失败");
            e.printStackTrace();
        }
        return ajaxResult;
    }

    @ResponseBody
    @RequestMapping("/doEdit")
    public Object doEdit(Permission permission) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            int count = permissionService.editPermission(permission);
            ajaxResult.setSuccess(1==count);
        } catch (Exception e) {
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("许可书加载失败");
            e.printStackTrace();
        }
        return ajaxResult;
    }

    @ResponseBody
    @RequestMapping("/doAdd")
    public Object doAdd(Permission permission) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            permissionService.insertPermission(permission);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("许可书加载失败");
            e.printStackTrace();
        }
        return ajaxResult;
    }

    @ResponseBody
    @RequestMapping("/loadData")
    public Object loadData() {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            ArrayList<Permission> root = new ArrayList<>();

            List<Permission> allPermissions = permissionService.getAllallPermissions();
            Map<Integer, Permission> map = new HashMap<>();
            for (Permission innerPermission : allPermissions) {
                map.put(innerPermission.getId(), innerPermission);
            }

            for (Permission permission : allPermissions) {
                //Permission children = permission;
                if (permission.getPid() == null) {
                    root.add(permission);
                } else {
                    Permission parent = map.get(permission.getPid());
                    parent.getChildren().add(permission);
                }
            }

            ajaxResult.setData(root);

            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("许可书加载失败");
            e.printStackTrace();
        }
        return ajaxResult;
    }
  /*  @ResponseBody
    @RequestMapping("/loadData")
    public Object loadData(){
        AjaxResult ajaxResult = new AjaxResult();
        try {
            ArrayList<Permission> root = new ArrayList<>();
            Permission permission = permissionService.getRootPermission();

            queryChildPermission(permission);

            root.add(permission);
            ajaxResult.setData(root);

            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("许可书加载失败");
            e.printStackTrace();
        }
        return ajaxResult;
    }*/

    /*private void queryChildPermission (Permission permission){
        List<Permission> children = permissionService.getChildrenPermissionByPid(permission.getId());
        permission.setChildren(children);
        for (Permission innerChild : children) {
            queryChildPermission(innerChild);
        }
    }*/
}
