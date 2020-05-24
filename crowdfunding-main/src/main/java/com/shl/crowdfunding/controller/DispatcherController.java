package com.shl.crowdfunding.controller;

import com.shl.crowdfunding.bean.Member;
import com.shl.crowdfunding.bean.Permission;
import com.shl.crowdfunding.bean.User;
import com.shl.crowdfunding.manager.service.UserService;
import com.shl.crowdfunding.potal.service.MemberService;
import com.shl.crowdfunding.util.AjaxResult;
import com.shl.crowdfunding.util.Const;
import com.shl.crowdfunding.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class DispatcherController {
    @Autowired
    private UserService userService;

    @Autowired
    private MemberService memberService;

    @RequestMapping("/index")
    public String index(){
        return "index";
    }
    @RequestMapping("/login")
    public String login(HttpServletRequest request,HttpSession session){//判断是否需要自动登录
        //如果之前登录过，cookie中存放了用户信息，需要获取cookie中的信息，并进行数据库的验证

        boolean needLogin = true;
        String logintype = null ;
        Cookie[] cookies = request.getCookies();
        if(cookies != null){ //如果客户端禁用了Cookie，那么无法获取Cookie信息
            for (Cookie cookie : cookies) {
                if("logincode".equals(cookie.getName())){
                    String logincode = cookie.getValue();
                    System.out.println("获取到Cookie中的键值对"+cookie.getName()+"===== " + logincode);
                    //loginacct=admin&userpwd=21232f297a57a5a743894a0e4a801fc3&logintype=member
                    String[] split = logincode.split("&");
                    if(split.length == 3){
                        String loginacct = split[0].split("=")[1];
                        String userpwd = split[1].split("=")[1];
                        logintype = split[2].split("=")[1];
                        Map<String,Object> paramMap = new HashMap<>();
                        paramMap.put("loginacct", loginacct);
                        paramMap.put("userpswd", userpwd);
                        paramMap.put("type", logintype);

                        if("user".equals(logintype)){
                            User dbLogin = userService.querUserLogin(paramMap);
                            if(dbLogin!=null){
                                session.setAttribute(Const.LOGIN_USER, dbLogin);
                                needLogin = false ;
                            }
                            //加载当前登录用户的所拥有的许可权限.
                            //User user = (User)session.getAttribute(Const.LOGIN_USER);
                            Map<Integer, Permission> rootMap = new HashMap<>();
                            Set<String> myUris = new HashSet<>();
                            Permission rootPermission=null;
                            List<Permission> UserPermissions = userService.getUserPermissionsByUserId(dbLogin.getId());
                            for (Permission userPermission : UserPermissions) {
                                rootMap.put(userPermission.getId(),userPermission);
                                myUris.add("/"+userPermission.getUrl());
                            }
                            for (Permission permission : UserPermissions) {
                                if(permission.getPid()==null){
                                    rootPermission=permission;
                                }else {
                                    Permission parent = rootMap.get(permission.getPid());
                                    parent.getChildren().add(permission);
                                }
                            }
                            session.setAttribute("permissionRoot",rootPermission);
                            session.setAttribute(Const.MY_URIS,myUris);
                        }else if("member".equals(logintype)){

                            Member dbLogin = memberService.querMemberLogin(paramMap);
                            if(dbLogin!=null){
                                session.setAttribute(Const.LOGIN_MEMBER, dbLogin);
                                needLogin = false ;
                            }
                        }

                    }
                }
            }
        }

        if(needLogin){
            return "login";
        }else{
            if("user".equals(logintype)){
                return "redirect:/main.htm";
            }else if("member".equals(logintype)){
                return "redirect:/member.htm";
            }
        }
        return "login";
    }
    @RequestMapping("/member")
    public String member(){
        return "member/member";
    }

    @RequestMapping("/main")
    public String main(HttpSession session){
        return "main";
    }
    @ResponseBody
    @RequestMapping("/doLogin")
    public Object doLogin(String loginacct, String userpswd, String type, HttpSession session,
                          String rememberme, HttpServletResponse response){
        AjaxResult result = new AjaxResult();
        try {
            HashMap<String, Object> loginHashMap = new HashMap<>();
            loginHashMap.put("type",type);
            loginHashMap.put("userpswd", MD5Util.digest(userpswd));
            loginHashMap.put("loginacct",loginacct);
            if(type.equals("user")){
                User user = userService.querUserLogin(loginHashMap);
                session.setAttribute(Const.LOGIN_USER,user);
                if("1".equals(rememberme)){
                    String logincode = "\"loginacct="+user.getLoginacct()+"&userpwd="+user.getUserpswd()+"&logintype=user\"";
                    //loginacct=superadmin&userpwd=21232f297a57a5a743894a0e4a801fc3&logintype=user
                    //System.out.println("用户-存放到Cookie中的键值对：logincode::::::::::::"+logincode);

                    Cookie c = new Cookie("logincode",logincode);

                    c.setMaxAge(60*60*24*14); //2周时间Cookie过期     单位秒
                    c.setPath("/"); //表示任何请求路径都可以访问Cookie

                    response.addCookie(c);
                }

                //==============================用户权限控制
                Map<Integer, Permission> rootMap = new HashMap<>();
                Set<String> myUris = new HashSet<>();
                Permission rootPermission=null;
                List<Permission> UserPermissions = userService.getUserPermissionsByUserId(user.getId());
                for (Permission userPermission : UserPermissions) {
                    rootMap.put(userPermission.getId(),userPermission);
                    myUris.add("/"+userPermission.getUrl());
                }
                for (Permission permission : UserPermissions) {
                    if(permission.getPid()==null){
                        rootPermission=permission;
                    }else {
                        Permission parent = rootMap.get(permission.getPid());
                        parent.getChildren().add(permission);
                    }
                }
                session.setAttribute("permissionRoot",rootPermission);
                session.setAttribute(Const.MY_URIS,myUris);
            }else if(type.equals("member")){
                Member member = memberService.querMemberLogin(loginHashMap);
                session.setAttribute(Const.LOGIN_MEMBER,member);

                if("1".equals(rememberme)){
                    String logincode = "\"loginacct="+member.getLoginacct()+"&userpwd="+member.getUserpswd()+"&logintype=member\"";
                    //loginacct=superadmin&userpwd=21232f297a57a5a743894a0e4a801fc3&logintype=user
                    //System.out.println("用户-存放到Cookie中的键值对：logincode::::::::::::"+logincode);

                    Cookie c = new Cookie("logincode",logincode);

                    c.setMaxAge(60*60*24*14); //2周时间Cookie过期     单位秒
                    c.setPath("/"); //表示任何请求路径都可以访问Cookie

                    response.addCookie(c);
                }
            }else {
                throw new Exception("登陆用户类型错误");
            }
            result.setData(type);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage(e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }
 /*   @RequestMapping("/doLogin")
    public String doLogin(String loginacct, String userpswd, String username, HttpSession session){
        HashMap<String, Object> loginHashMap = new HashMap<>();
        loginHashMap.put("username",username);
        loginHashMap.put("userpswd",userpswd);
        loginHashMap.put("loginacct",loginacct);
        User user = userService.querUserLogin(loginHashMap);
        session.setAttribute(Const.LOGIN_USER,user);
        return "redirect:/main.htm";
    }*/


    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:index.jsp";
    }
}
