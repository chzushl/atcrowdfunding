<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/4/13
  Time: 20:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <link rel="stylesheet" href="${APP_PATH}/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${APP_PATH}/css/font-awesome.min.css">
    <link rel="stylesheet" href="${APP_PATH}/css/main.css">
    <link rel="stylesheet" href="${APP_PATH}/css/pagination.css">
    <style>
        .tree li {
            list-style-type: none;
            cursor:pointer;
        }
        table tbody tr:nth-child(odd){background:#F4F4F4;}
        table tbody td:nth-child(even){color:#C00;}
    </style>
</head>

<body>

<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <div><a class="navbar-brand" style="font-size:32px;" href="#">众筹平台 - 流程管理</a></div>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <li style="padding-top:8px;">
                    <jsp:include page="/WEB-INF/jsp/common/userinfo.jsp"></jsp:include>
                </li>
                <li style="margin-left:10px;padding-top:8px;">
                    <button type="button" class="btn btn-default btn-danger">
                        <span class="glyphicon glyphicon-question-sign"></span> 帮助
                    </button>
                </li>
            </ul>
            <form class="navbar-form navbar-right">
                <input type="text" class="form-control" placeholder="Search...">
            </form>
        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
            <div class="tree">
                <jsp:include page="/WEB-INF/jsp/common/menu.jsp"></jsp:include>
            </div>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input class="form-control has-success" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button type="button" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
                    </form>

                    <button id="uploadProDefBtn" type="button" class="btn btn-primary" style="float:right;"><i class="glyphicon glyphicon-upload"></i> 上传流程定义文件</button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <form id="processForm"  method="post" enctype="multipart/form-data">
                            <input id="processFile" style="display: none" type="file"  name="processFile">
                        </form>
                        <table class="table  table-bordered">
                            <thead>
                            <tr >
                                <th width="30">#</th>
                                <th>流程定义名称</th>
                                <th>流程定义版本</th>
                                <th>流程定义Key</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody>

                            </tbody>
                            <tfoot>
                            <tr >
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"><!-- 这里显示分页 --></div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${APP_PATH}/jquery/jquery-2.1.1.min.js"></script>
<script src="${APP_PATH}/bootstrap/js/bootstrap.min.js"></script>
<script src="${APP_PATH}/script/docs.min.js"></script>
<script src="${APP_PATH }/jquery/pagination/jquery.pagination.js"></script>
<script type="text/javascript" src="${APP_PATH }/script/menu.js"></script>
<script type="text/javascript" src="${APP_PATH }/jquery/layer/layer.js"></script>
<script src="${APP_PATH }/jquery/jquery-form/jquery.form.js"></script>

<script type="text/javascript">
    $(function () {
        $(".list-group-item").click(function(){
            if ( $(this).find("ul") ) {
                $(this).toggleClass("tree-closed");
                if ( $(this).hasClass("tree-closed") ) {
                    $("ul", this).hide("fast");
                } else {
                    $("ul", this).show("fast");
                }
            }
        });
        showMenu();
        queryPageUser(0);
    });
    $("#uploadProDefBtn").click(function () {
        $("#processFile").click();
    });

    $("#processFile").change(function () {
        var options = {
            url:"${APP_PATH}/process/deploy.do",
            beforeSubmit : function(){
                loadingIndex = layer.msg('文件正在部署中', {icon: 6});
                return true ; //必须返回true,否则,请求终止.
            },
            success : function(obj){
                var result = eval("("+obj+")");
                layer.close(loadingIndex);
                if(result.success){
                    layer.msg("文件部署成功", {time:1000, icon:6});
                    queryPageUser(0);
                }else{
                    layer.msg(result.message, {time:1000, icon:5, shift:6});
                }
            }
        };
        $("#processForm").ajaxSubmit(options); //异步提交
        $("#processForm")[0].reset();
        return ;
    });

    var jsonObj = {
        "pageno" : 1,
        "pagesize" : 10
    };

    var loadingIndex = -1 ;
    function queryPageUser(pageIndex){
        jsonObj.pageno = pageIndex+1;
        $.ajax({
            type : "POST",
            data : jsonObj,
            url : "${APP_PATH}/process/doIndex.do",
            beforeSend : function(){
                loadingIndex = layer.load(2, {time: 10*1000});
                return true ;
            },
            success : function(ajxaResult){
                var result = eval("("+ajxaResult+")");
                layer.close(loadingIndex);
                if(result.success){
                    var page = result.page ;
                    var data = page.datas ;

                    var content = '';

                    $.each(data,function(i,n){
                        content+='<tr>';
                        content+='<td>'+(i+1)+'</td>';
                        content+='<td>'+n.name+'</td>';
                        content+='<td>'+n.version+'</td>';
                        content+='<td>'+n.key+'</td>';
                        content+='<td>';
                        content+='    <button type="button" onclick="window.location.href=\'${APP_PATH}/process/showimg.htm?id='+n.id+'\'" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-eye-open"></i></button>';
                        content+='    <button type="button" onclick="deleteProcess(\''+n.id+'\',\''+n.name+'\')" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></button>';
                        content+='</td>';
                        content+='</tr>';
                    });


                    $("tbody").html(content);

                    $("#Pagination").pagination(page.totalsize, {
                        num_edge_entries: 1, //边缘页数
                        num_display_entries: 4, //主体页数
                        callback: queryPageUser, //查询当前页的数据.
                        items_per_page:page.pagesize, //每页显示1项
                        current_page:(page.pageno-1), //显示当前页索引,索引从0开始
                        prev_text:"上一页",
                        next_text:"下一页"
                    });

                }else{
                    layer.msg(result.message, {time:1000, icon:5, shift:6});
                }
            },
            error : function(){
                layer.msg("加载数据失败!", {time:1000, icon:5, shift:6});
            }
        });
    }
    function deleteProcess(id,name) {
        layer.confirm("是否删除["+name+"]",  {icon: 3, title:'提示'}, function(cindex){
            layer.close(cindex);
            $.ajax({
                type:"POST",
                url:"${APP_PATH}/process/doDelete.do",
                data:{
                    id:id
                },
                beforeSend:function () {
                    loadingIndex = layer.load(2, {time: 10*1000});
                    return true;
                },
                success:function (result) {
                    layer.close(loadingIndex);
                    var obj = eval("("+result+")");
                    if(obj.success){
                        queryPageUser(0);
                    }else{
                        layer.msg(obj.message,{time:1000, icon:5, shift:6});
                    }
                },
                error:function () {
                    layer.msg(obj.message,{time:1000, icon:5, shift:6});
                }
            });
        }, function(cindex){
            layer.close(cindex);
        });
    };
</script>
</body>
</html>

