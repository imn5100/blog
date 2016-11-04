<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>ShawBlog管理界面</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/bootstrap3/js/jquery.md5.js"></script>
    <script type="text/javascript">

        var url;

        function openTab(text, url, iconCls) {
            if ($("#tabs").tabs("exists", text)) {
                $("#tabs").tabs("select", text);
            } else {
                var content = "<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='${pageContext.request.contextPath}/admin/" + url + "'></iframe>";
                $("#tabs").tabs("add", {
                    title: text,
                    closable: true,
                    iconCls: iconCls,
                    content: content
                });
            }
        }


        function openPasswordModifyDialog() {
            $("#dlg").dialog("open").dialog("setTitle", "修改密码");
            url = "${pageContext.request.contextPath}/admin/blogger/modifyPassword.do?id=${currentUser.id}";
        }
        function openWebLogDialog() {
            jQuery.post("/admin/system/getWebLogHtmlList.do", function (data) {
                var obj = jQuery.parseJSON(data);
                if (obj.success == true) {
                    var html = "";
                    obj.data.forEach(function (element, index) {
                        html += "<tr>"
                        html += '<td> <a href="/admin/system/getWebLogHtml.html?filename=' + element + '" target="_blank">' + element + '</a></td>'
                        html += "<tr>"
                    });
                    $("#webloglist").html(html);
                }
            });
            $("#weblog").dialog("open").dialog("setTitle", "web日志分析");
        }

        function modifyPassword() {
            $("#fm").form("submit", {
                url: url,
                onSubmit: function () {
                    var newPassword = $("#newPassword").val();
                    var newPassword2 = $("#newPassword2").val();
                    if (!$(this).form("validate")) {
                        return false;
                    }
                    if (newPassword != newPassword2) {
                        $.messager.alert("系统提示", "确认密码输入错误！");
                        return false;
                    }
                    $("#newPassword").get(0).value = $.md5(newPassword);
                    $("#newPassword2").get(0).value = $.md5(newPassword2);
                    return true;
                },
                success: function (result) {
                    var result = eval('(' + result + ')');
                    if (result.success) {
                        $.messager.alert("系统提示", "密码修改成功，下一次登录生效！");
                        resetValue();
                        $("#dlg").dialog("close");
                    } else {
                        $.messager.alert("系统提示", "密码修改失败！");
                        return;
                    }
                }
            });
        }

        function closePasswordModifyDialog() {
            resetValue();
            $("#dlg").dialog("close");
        }

        function resetValue() {
            $("#oldPassword").val("");
            $("#newPassword").val("");
            $("#newPassword2").val("");
        }

        function logout() {
            $.messager.confirm("系统提示", "您确定要退出系统吗？", function (r) {
                if (r) {
                    window.location.href = '${pageContext.request.contextPath}/admin/blogger/logout.do';
                }
            });
        }

        function refreshSystem() {
            $.post("${pageContext.request.contextPath}/admin/system/refreshSystem.do", {}, function (result) {
                if (result.success) {
                    $.messager.alert("系统提示", "已成功刷新系统缓存！");
                } else {
                    $.messager.alert("系统提示", "刷新系统缓存失败！");
                }
            }, "json");
        }
        function refreshIndex() {
            $.post("${pageContext.request.contextPath}/admin/system/refreshIndex.do", {}, function (result) {
                if (result.success) {
                    $.messager.alert("系统提示", "重建索引成功！");
                } else {
                    $.messager.alert("系统提示", "重建索引失败");
                }
            }, "json");
        }
        function resetSummary() {
            $.post("${pageContext.request.contextPath}/admin/system/resetSummary.do?length=280", {}, function (result) {
                if (result.success) {
                    $.messager.alert("系统提示", "重置概述成功！");
                } else {
                    $.messager.alert("系统提示", "重置概述失败");
                }
            }, "json");
        }

    </script>
</head>
<body class="easyui-layout">
<div region="north">
</div>
<div region="center">
    <div class="easyui-tabs" fit="true" border="false" id="tabs">
        <div title="控制台" data-options="iconCls:'icon-home'">
            <div align="center" style="padding-top: 100px"><a href="https://ecs.console.aliyun.com/#/home" target="_blank"><font
                    color="#808080" size="5">阿里云控制台</font></a></div>
        </div>
    </div>
</div>
<div region="west" style="width: 200px" title="导航菜单" split="true">
    <div class="easyui-accordion" data-options="fit:true,border:false">
        <div title="常用操作" data-options="selected:true,iconCls:'icon-item'" style="padding: 10px">
            <a href="javascript:openTab('写博客','writeBlog.jsp','icon-writeblog')" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-writeblog'" style="width: 150px">写博客</a>
            <a href="http://shawblog.duoshuo.com/admin/" target="_blank" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-review'" style="width: 150px">评论审核</a>
            <a href="javascript:openWebLogDialog()" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-review'" style="width: 150px;">web日志分析报表</a>
        </div>
        <div title="博客管理" data-options="iconCls:'icon-bkgl'" style="padding:10px;">
            <a href="javascript:openTab('写博客','writeBlog.jsp','icon-writeblog')" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-writeblog'" style="width: 150px;">写博客</a>
            <a href="javascript:openTab('博客信息管理','blogManage.jsp','icon-bkgl')" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-bkgl'" style="width: 150px;">博客信息管理</a>
        </div>
        <div title="博客类别管理" data-options="iconCls:'icon-bklb'" style="padding:10px">
            <a href="javascript:openTab('博客类别信息管理','blogTypeManage.jsp','icon-bklb')" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-bklb'" style="width: 150px;">博客类别信息管理</a>
        </div>
        <div title="个人信息管理" data-options="iconCls:'icon-grxx'" style="padding:10px">
            <a href="javascript:openTab('修改个人信息','modifyInfo.jsp','icon-grxxxg')" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-grxxxg'" style="width: 150px;">修改个人信息</a>
            <a href="javascript:openPasswordModifyDialog()" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-modifyPassword'" style="width: 150px;">修改密码</a>
        </div>
        <div title="系统管理" data-options="iconCls:'icon-system'" style="padding:10px">
            <a href="javascript:openTab('友情链接管理','linkManage.jsp','icon-link')" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-link'" style="width: 150px">友情链接管理</a>
            <a href="javascript:openWebLogDialog()" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-review'" style="width: 150px;">web日志分析报表</a>
            <a href="javascript:refreshSystem()" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-refresh'" style="width: 150px;">刷新系统缓存</a>
            <a href="javascript:refreshIndex()" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-grxxxg'" style="width: 150px;">重建索引</a>
            <a href="javascript:resetSummary()" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-grxxxg'" style="width: 150px;">重置博客概述</a>
            <a href="javascript:logout()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-exit'"
               style="width: 150px;">安全退出</a>
        </div>
    </div>
</div>
<div region="south" align="center">
    Rua~~~~~!
</div>

<div id="dlg" class="easyui-dialog" style="width:400%;height:200%;padding: 10px 20px"
     closed="true" buttons="#dlg-buttons">

    <form id="fm" method="post">
        <table cellspacing="8px">
            <tr>
                <td>用户名：</td>
                <td><input type="text" id="userName" name="userName" readonly="readonly"
                           value="${currentUser.userName }" style="width: 200px"/></td>
            </tr>
            <tr>
                <td>新密码：</td>
                <td><input type="password" id="newPassword" name="newPassword" class="easyui-validatebox"
                           required="true" style="width: 200px"/></td>
            </tr>
            <tr>
                <td>确认新密码：</td>
                <td><input type="password" id="newPassword2" name="newPassword2" class="easyui-validatebox"
                           required="true" style="width: 200px"/></td>
            </tr>
        </table>
    </form>
</div>
<div id="weblog" class="easyui-dialog" style="width:200%;height:300%;padding: 10px 20px"
     closed="true">
    日志分析生成的报表列表：<br>
    <table cellspacing="8px" id="webloglist">
    </table>
    <br>* 每日7:30生成
</div>

<div id="dlg-buttons">
    <a href="javascript:modifyPassword()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
    <a href="javascript:closePasswordModifyDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>

</body>
</html>