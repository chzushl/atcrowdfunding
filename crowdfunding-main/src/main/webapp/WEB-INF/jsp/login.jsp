<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/3/20
  Time: 17:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keys" content="">
    <meta name="author" content="">
    <link rel="stylesheet" href="${APP_PATH}/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${APP_PATH}/css/font-awesome.min.css">
    <link rel="stylesheet" href="${APP_PATH}/css/login.css">
    <style>

    </style>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <div><a class="navbar-brand" href="index.html" style="font-size:32px;">尚筹网-创意产品众筹平台</a></div>
        </div>
    </div>
</nav>

<div class="container">

    <form id="loginForm" action="${APP_PATH}/doLogin.do" method="post" class="form-signin" role="form">
        ${exception.message}
        <h2 class="form-signin-heading"><i class="glyphicon glyphicon-log-in"></i> 用户登录</h2>
        <div class="form-group has-success has-feedback">
            <input type="text" name="loginacct" class="form-control" id="floginacct" placeholder="请输入登录账号" autofocus>
            <span class="glyphicon glyphicon-user form-control-feedback"></span>
        </div>
        <div class="form-group has-success has-feedback">
            <input type="password" name="userpswd" class="form-control" id="fuserpswd" placeholder="请输入登录密码" style="margin-top:10px;">
            <span class="glyphicon glyphicon-lock form-control-feedback"></span>
        </div>
        <div class="form-group has-success has-feedback">
            <select class="form-control" name="type" id="ftype">
                <option value="member" selected>会员</option>
                <option value="user" >管理</option>
            </select>
        </div>
        <div class="checkbox">
            <label>
                <input id="rememberMe" type="checkbox" value="1"> 记住我
            </label>
            <br>
            <label>
                忘记密码
            </label>
            <label style="float:right">
                <a href="reg.html">我要注册</a>
            </label>
        </div>
        <a class="btn btn-lg btn-success btn-block" onclick="dologin2()">登录</a>
    </form>
</div>
<script src="${APP_PATH}/jquery/jquery-2.1.1.min.js"></script>
<script src="${APP_PATH}/bootstrap/js/bootstrap.min.js"></script>
<script src="${APP_PATH}/jquery/layer/layer.js"></script>
<script>
    function dologin2() {
       /* $("#loginForm").submit();*/
        var loginacct = $("#floginacct");
        var userpswd = $("#fuserpswd");
        var type = $("#ftype");

        if($.trim(loginacct.val())==""){
            layer.msg("用户名不能为空,请重新输入", {time:1000, icon:5, shift:6}, function(){
                loginacct.val("");
                loginacct.focus();
            });
            return false;
        }

        if($.trim(userpswd.val())==""){
            layer.msg("用户密码不能为空,请重新输入",{time:1000, icon:5, shift:6},function () {
                userpswd.val("");
                userpswd.focus();
            })
            return false;
        }
        var flag = $("#rememberMe")[0].checked;
        var loadingIndex=-1;
        $.ajax({
            type : "post",
            data : {
                loginacct:loginacct.val(),
                userpswd:userpswd.val(),
                type:type.val(),
                rememberme:flag?"1":"0"
            },
            url : "${APP_PATH}/doLogin.do",
            beforeSend : function(){
                loadingIndex = layer.msg('处理中', {icon: 16});
            },
            success : function(result){
                var obj = eval("("+result+")");
                if(obj.success){
                    layer.close(loadingIndex);
                   /* alert("登录成功");*/
                    if("user"==obj.data){
                        window.location.href="${APP_PATH}/main.htm";
                    }else if("member"==obj.data){
                        window.location.href="${APP_PATH}/member.htm";
                    }else {
                        layer.msg("传入的用户类型不合法",{time:1000, icon:5, shift:6});
                    }
                }else{
                    layer.msg(obj.message,{time:1000, icon:5, shift:6});
                }
            }
        });
    }
</script>
</body>
</html>
