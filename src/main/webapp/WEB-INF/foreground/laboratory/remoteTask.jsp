<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="技术博客,后端,java">
    <link href="/favicon.ico" rel="SHORTCUT ICON">
    <title>远程任务发送器</title>
    <link rel="stylesheet" href="/static/bootstrap3/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/bootstrap3/css/bootstrap-theme.min.css">
    <script src="/static/bootstrap3/js/jquery.js"></script>
    <script src="/static/bootstrap3/js/bootstrap.min.js"></script>
    <style type="text/css">
        body {
            padding-top: 100px;
            padding-bottom: 20px;
            background-image: url(https://ooo.0o0.ooo/2016/06/20/5768c606cf9cb.jpg);
        }
    </style>
</head>
<body>
<div class="container None">
    <jsp:include page="/WEB-INF/foreground/common/head.jsp"/>
    <div>
        <div class="col-md-12">
            <div class="panel panel panel-default">
                <c:if test="${loginSuccess == null or loginSuccess == false}">
                    <div class="panel-heading">
                        身份验证
                    </div>
                    <form style="padding: 10px 10px 10px 10px;" id="auth">
                        <div class="form-group">
                            <label for="AppKey">AppKey</label>
                            <input type="text" class="form-control" id="AppKey" maxlength="32">
                        </div>
                        <div class="form-group">
                            <label for="AppSecret">AppSecret</label>
                            <input type="password" class="form-control" id="AppSecret" maxlength="32">
                        </div>
                        <button>进入</button>
                    </form>
                </c:if>
                <c:if test="${loginSuccess == true}">
                <div class="panel-heading">
                    远程任务发送
                </div>
                <div class="bs-docs-section" style="padding:10px 10px 10px;">
                    <form class="form-horizontal" id="sendTask">
                        <div class="form-group">
                            <label for="title" class="col-sm-2 control-label">主题</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="title" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="taskType" class="col-sm-2 control-label">任务类型</label>
                            <div class="col-sm-10">
                                <select id="taskType" class="form-control">
                                    <c:forEach var="item" items="${task_user.remoteTaskPermissionList}">
                                        <option value="${item.value}">${item.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">客户端状态</label>
                            <div class="col-sm-10">
                                <c:if test="${socket_connect == true}">
                                    已连接
                                </c:if>
                                <c:if test="${socket_connect != true}">
                                    未连接
                                </c:if>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="contents" class="col-sm-2 control-label">内容</label>
                            <div class="col-sm-10">
                                <textarea class="form-control" rows="10" id="contents"> </textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-8">
                                <button type="submit" class="btn btn-default">发送~~＼(☆o☆)／</button>
                            </div>
                            <div class="col-sm-2">
                                <a id="u_info" style="float:right">显示用户信息</a>
                            </div>
                        </div>
                    </form>
                </div>
                <hr>
                <div id="user_info" style="padding:10px 10px 10px;display: None">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">用户信息：</label>
                        </div>
                        <div class="form-group">
                            <label for="AK" class="col-sm-2 control-label">AK</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="AK" readonly="readonly"
                                       value="${task_user.appkey}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="as" class="col-sm-2 control-label">AS</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="as" readonly="readonly"
                                       value="${task_user.appsecret}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="tuac" class="col-sm-2 control-label">活跃时间</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="tuac" readonly="readonly"
                                       value="${task_user.showActiveTime}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="tun" class="col-sm-2 control-label">名字</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="tun" readonly="readonly"
                                       value="${task_user.name}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="tui" class="col-sm-2 control-label">介绍</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="tui" readonly="readonly"
                                       value="${task_user.intr}">
                            </div>
                        </div>
                    </form>
                    <table class="table">
                        <tr>
                            <td>
                            </td>
                            <td>
                                <button id="logout" style="float:right">退出</button>
                            </td>
                        </tr>
                    </table>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <div align="center" style="padding-top: 100px">
			<span style="">Powered by <a href="https://github.com/imn5100" target="_blank">
                <img alt="Brand" src="/static/images/git.png"> Imn5100</a></span><br>
            </div>
        </div>
    </div>
</div>
</body>
<script type="application/javascript">
    $("#auth").submit(function () {
        var ak = $("#AppKey").val();
        var as = $("#AppSecret").val();

        if (ak == null || ak.length != 32 || as == null || as.length != 32) {
            alert("AppKey 或 AppSecret 无效")
            return false;
        }
        var data = {
            "ak": ak,
            "as": as
        }
        $.post("/remoteTask/main.html", data, function () {
            location.reload()
        })
        return false;
    })
    $("#sendTask").submit(function () {
        var title = $("#title").val();
        var taskType = $("#taskType").val();
        var contents = $("#contents").val();

        if (title == null || title == "" || taskType == null || contents == null || contents == "") {
            alert("请填写完整信息")
            return false;
        }
        var data = {
            "title": title,
            "type": taskType,
            "contents": contents,
        }
        $.post("/remoteTask/addTask.html", data, function (data) {
            alert(data.msg);
            if (data.code == 200 || data.code == 605) {
                location.reload()
            }
        }, "json")
        return false;

    })
    $("#logout").click(function () {
        $.post("/remoteTask/logout.html", function () {
            location.reload()
        })
        return false;
    })
    $("#u_info").click(function () {
        $("#u_info").hide();
        $("#user_info").show();
    })
</script>
</html>
