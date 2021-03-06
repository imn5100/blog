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
    <link rel="stylesheet" href="/static/css/font-awesome.css">                <!-- Font Awesome -->
    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/css/template-style.css">
</head>
<body>
<div class="tm-navbar-container tm-navbar-container-dark">
    <nav class="navbar navbar-full navbar-fixed-top bg-inverse">
        <button class="navbar-toggler hidden-md-up" type="button" data-toggle="collapse" data-target="#tmNavbar">
            &#9776;
        </button>
        <div class="collapse navbar-toggleable-sm" id="tmNavbar">
            <ul class="nav navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" href="/">首页</a>
                </li>
                <li class="nav-item jumpHook">
                    <a class="nav-link" href="/#tm-section-2">博客</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/about.html">关于</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/blogger/about.html">歌单</a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" id="navbarDropdownMenuLink" data-toggle="dropdown"
                       aria-haspopup="true" aria-expanded="false">
                        实验室
                    </a>
                    <div class="dropdown-menu bg-inverse " style=""
                         aria-labelledby="navbarDropdownMenuLink">
                        <a class="dropdown-item bg-inverse"  href="/rainyRoom.html">Rainy Mood</a>
                        <a class="dropdown-item bg-inverse" href="/remoteTask/main.html">远程任务执行器</a>
                    </div>
                </li>
            </ul>

        </div>
    </nav>
</div>
<div class="container None" style="padding-top:100px;">
    <div>
        <div class="col-md-12">
            <div class="card card card-default">
                <c:if test="${loginSuccess == null or loginSuccess == false}">
                    <div class="card-heading">
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
                <div class="card-heading">
                    远程任务发送
                </div>
                <div class="card-body" style="padding:10px 10px 10px 10px;">
                    <div class="bs-docs-section">
                        <form class="form-horizontal" id="sendTask">
                            <div class="form-group">
                                <label for="title">主题</label>
                                <input type="text" class="form-control" id="title" placeholder="">
                            </div>
                            <div class="form-group">
                                <label for="taskType">任务类型</label>
                                <select id="taskType" class="form-control">
                                    <c:forEach var="item" items="${task_user.remoteTaskPermissionList}">
                                        <option value="${item.value}">${item.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>客户端状态:</label>
                                <c:if test="${socket_connect == true}">
                                    已连接
                                </c:if>
                                <c:if test="${socket_connect != true}">
                                    未连接
                                </c:if>
                            </div>
                            <div class="form-group">
                                <label for="contents">内容</label>
                                <textarea class="form-control" rows="10" id="contents"> </textarea>
                            </div>
                            <div class="form-group">
                                <button type="submit" class="btn btn-default">发送ヽ(╯▽╰)ﾉ</button>
                                <a id="u_info" style="float:right">显示用户信息</a>
                            </div>
                        </form>
                    </div>
                    <div class="alert alert-success" role="alert" id="infoAlert"
                         style="padding:10px 10px 10px 10px;display: None"></div>
                    <hr>
                    <div id="user_info" style="padding:10px 10px 10px;display: None">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label>用户信息：</label>
                            </div>
                            <div class="form-group">
                                <label for="AK" class="col-sm-2 control-label">AK</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" id="AK" readonly="readonly"
                                           value="${task_user.appKey}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="as" class="col-sm-2 control-label">AS</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" id="as" readonly="readonly"
                                           value="${task_user.appSecret}">
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
                                    <input type="text" class="form-control" id="tun" required="true"
                                           value="${task_user.name}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="tui" class="col-sm-2 control-label">介绍</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" id="tui" required="true"
                                           value="${task_user.intr}">
                                </div>
                            </div>
                        </form>
                        <table class="table">
                            <tr>
                                <td>
                                </td>
                                <td>
                                    <button id="update" class="btn btn-primary">保存</button>
                                    <button id="logout" class="btn btn-danger" style="float:right">退出</button>
                                </td>
                            </tr>
                        </table>
                    </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/foreground/common/foot.jsp"></jsp:include>
</body>
<script>
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
            divAlert(2, "请填写完整信息")
            return false;
        }
        var data = {
            "title": title,
            "type": taskType,
            "contents": contents,
        }
        $.post("/remoteTask/addTask.html", data, function (data) {
            if (data.code == 605) {
                alert(data.msg);
                location.reload()
            } else {
                if (data.code == 200) {
                    $("#title").val("");
                    $("#contents").val("");
                    divAlert(1, data.msg)
                } else {
                    divAlert(2, data.msg)
                }
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
    $("#update").click(function () {
        var name = $("#tun").val();
        var intr = $("#tui").val();
        if (name == "" || intr == "") {
            divAlert(2, "请填写完整信息")
            return;
        }
        $.post("/remoteTask/update.do", {"name": name, "intr": intr}, function (data) {
            divAlertForData(data)
        }, "json")
    })
    function divAlert(type, msg) {
        var alertDiv = $("#infoAlert")
        if (type == 1) {
            alertDiv.attr("class", "alert alert-success")
        } else if (type == 2) {
            alertDiv.attr("class", "alert alert-warning")
        } else if (type == 3) {
            alertDiv.attr("class", "alert alert-danger")
        }
        alertDiv.html(msg)
        alertDiv.show();
    }
    function divAlertForData(data) {
        if (data.code == 200) {
            //成功
            divAlert(1, data.msg)
        } else if (data.code == 605) {
            //登录超时
            alert(data.msg);
            location.reload()
        } else if (data.code == 600) {
            //执行出错
            divAlert(3, data.msg)
        } else {
            //业务错误
            divAlert(2, data.msg)
        }
    }

</script>
</html>
