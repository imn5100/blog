<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>外链文件管理</title>
    <link rel="stylesheet" type="text/css" href="/static/jquery-easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="/static/jquery-easyui/themes/icon.css">
    <script type="text/javascript" src="/static/jquery-easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/jquery-easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/static/jquery-easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="/static/ajaxfileupload.js"></script>
    <script type="text/javascript">
        function deleteUser() {
            var selectedRows = $("#dg").datagrid("getSelections");
            if (selectedRows.length != 1) {
                $.messager.alert("系统提示", "请选择1条要删除的数据！");
                return;
            }
            var appKey = selectedRows[0].appKey
            $.messager.confirm("系统提示", "您确定要删除这这条数据吗？", function (r) {
                if (r) {
                    $.post("/admin/remote/deleteUser.do", {'appkey': appKey}, function (result) {
                        if (result.code == 200) {
                            $.messager.alert("系统提示", "删除用户成功！");
                            $("#dg").datagrid("reload");
                        } else {
                            $.messager.alert("系统提示", result.msg);
                        }
                    }, "json").error(function () {
                        $.messager.alert("系统提示", "系统异常或登录超时，请刷新重试！");
                    });
                }
            });
        }
        function addUser() {
            var name = $("#addName").val();
            var salt = $("#addSalt").val();
            var permissions = $("#addPermissions").val();
            var intr = $("#addIntr").val();
            if (name == null || name == "" || salt == null || salt == "" || permissions == null || permissions == "") {
                $.messager.alert("系统提示", "请填写必要内容！");
                return;
            }
            if (!isdigit(permissions)) {
                $.messager.alert("系统提示", "权限必须是数字类型！");
                return;
            }
            params = {
                "name": name,
                "salt": salt,
                "permissions": permissions,
                "intr": intr
            }
            $.post("/admin/remote/addUser.do", params, function (result) {
                if (result.code == 200) {
                    $.messager.alert("系统提示", "添加成功！");
                    closeDialog1()
                    $("#dg").datagrid("reload");
                } else {
                    $.messager.alert("系统提示", "添加失败！" + result.msg);
                }
            }, "json").error(function () {
                $.messager.alert("系统提示", "系统异常或登录超时，请刷新重试！");
            });
            return false;
        }
        function openMsgModifyDialog() {
            var selectedRows = $("#dg").datagrid("getSelections");
            if (selectedRows.length != 1) {
                $.messager.alert("系统提示", "请选择一条要编辑的数据！");
                return;
            }
            var row = selectedRows[0];
            $("#dlg_u").dialog("open").dialog("setTitle", "编辑远程消息任务信息");
            $("#fm_u").form("load", row);
        }
        function updateUser() {
            var appkey = $("#modifyAppkey").val();
            var intr = $("#modifyIntr").val();
            var name = $("#modifyName").val();
            var permissions = $("#modifyPermissions").val();
            if (appkey == null || appkey == "" || intr == null || intr == "" || name == null || name == "" || permissions == null || permissions == "") {
                $.messager.alert("系统提示", "请填写必要内容！");
                return;
            }
            if (!isdigit(permissions)) {
                $.messager.alert("系统提示", "权限必须是数字类型！");
                return;
            }
            params = {
                "appKey": appkey,
                "intr": intr,
                "name": name,
                "permissions": permissions,
            }
            $.post("/admin/remote/updateUser.do", params, function (result) {
                if (result.code == 200) {
                    $.messager.alert("系统提示", "更新成功！");
                    closeDialog2()
                    $("#dg").datagrid("reload");
                } else {
                    $.messager.alert("系统提示", "更新失败！" + result.msg);
                }
            }, "json").error(function () {
                $.messager.alert("系统提示", "系统异常或登录超时，请刷新重试！");
            });
            return false;
        }
        function searchUser() {
            $("#dg").datagrid('load', {
                "appkey": $("#appkey").val(),
                "name": $("#name").val(),
            });
        }
        function formatTime(val, row) {
            if (val == null) {
                return ""
            }
            return new Date(val).toLocaleString();
        }
        function openAddUserDialog() {
            $("#dlg1").dialog("open").dialog("setTitle", "添加远程任务用户");
        }
        function openModifyUserDialog() {
            var selectedRows = $("#dg").datagrid("getSelections");
            if (selectedRows.length != 1) {
                $.messager.alert("系统提示", "请选择一条要编辑的数据！");
                return;
            }
            var row = selectedRows[0];
            $("#dlg2").dialog("open").dialog("setTitle", "修改");
            $("#fm_m").form("load", row);
        }

        function closeDialog1() {
            $("#dlg1").dialog("close");
            $("#addName").val("")
            $("#addSalt").val("")
            $("#addPermissions").val("")
            $("#addIntr").val("")
        }
        function closeDialog2() {
            $("#dlg2").dialog("close");
        }
        function isdigit(s) {
            var r, re;
            re = /\d*/i; //\d表示数字,*表示匹配多个数字
            r = s.match(re);
            return (r == s) ? 1 : 0;
        }
    </script>
</head>
<body style="margin: 1px">
<table id="dg" title="远程任务用户管理" class="easyui-datagrid"
       fitColumns="true" pagination="true" rownumbers="true"
       url="/admin/remote/listUser.do" fit="true" toolbar="#tb">
    <thead>
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th field="appKey" align="center">AppKey</th>
        <th field="appSecret" align="center">AppSecret</th>
        <th field="permissions" align="center">执行权限</th>
        <th field="name" align="center">名字</th>
        <th field="intr" align="center">介绍</th>
        <th field="activeTime" align="center" formatter="formatTime">活跃时间</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <div>
        <a href="javascript:openAddUserDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加用户</a>
        <a href="javascript:openModifyUserDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">编辑用户</a>
        <a href="javascript:deleteUser()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除用户</a>
        <div>
            &nbsp;appkey：&nbsp;<input type="text" id="appkey" size="20"
                                      onkeydown="if(event.keyCode==13) searchUser()"/>
            &nbsp;名字：&nbsp;<input type="text" id="name" size="20"
                                  onkeydown="if(event.keyCode==13) searchUser()"/>
            <a href="javascript:searchUser()" class="easyui-linkbutton" iconCls="icon-search" plain="true">查询</a>
        </div>
    </div>
</div>
<div id="dlg1" class="easyui-dialog" style="width:350%;height:300%;padding: 5px 5px"
     closed="true" buttons="#dlg1-buttons">
    <form id="fm" method="post">
        <table cellspacing="8px">
            <tr>
                <td>名字：</td>
                <td><input type="text" id="addName" class="easyui-validatebox" placeholder="用户名"/>
                </td>
            </tr>
            <tr>
                <td>加密Salt：</td>
                <td><input id="addSalt" class="easyui-validatebox" placeholder="用作appkey加密">
                </td>
            </tr>
            <tr>
                <td>执行权限：</td>
                <td><input id="addPermissions" class="easyui-validatebox" placeholder="以二进制位为标识符"></td>
            </tr>
            <tr>
                <td>简介：</td>
                <td><input id="addIntr" class="easyui-validatebox"></td>
            </tr>
        </table>

    </form>
</div>
<div id="dlg2" class="easyui-dialog" style="width:350%;height:300%;padding: 5px 5px"
     closed="true" buttons="#dlg2-buttons">
    <form id="fm_m" method="post">
        <table cellspacing="8px">
            <tr>
                <td>AppKey：</td>
                <td><input type="text" id="modifyAppkey" name="appKey" class="easyui-validatebox" required="true"
                           readonly="readonly"/></td>
            </tr>
            <tr>
                <td>AppSecret：</td>
                <td><input type="text" id="modifyAppsercet" name="appSecret" class="easyui-validatebox" required="true"
                           readonly="readonly"/>
                </td>
            </tr>
            <tr>
                <td>执行权限：</td>
                <td><input type="text" id="modifyPermissions" name="permissions" class="easyui-validatebox"
                           required="true"/>
                </td>
            </tr>
            <tr>
                <td>名字：</td>
                <td><input type="text" id="modifyName" name="name" class="easyui-validatebox" required="true"/>
                </td>
            </tr>
            <tr>
                <td>简介：</td>
                <td><input type="text" id="modifyIntr" name="intr" class="easyui-validatebox" required="true"/>
                </td>
            </tr>
        </table>
    </form>
</div>

<div id="dlg1-buttons">
    <a href="javascript:addUser()" class="easyui-linkbutton" iconCls="icon-ok">提交</a>
    <a href="javascript:closeDialog1()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
<div id="dlg2-buttons">
    <a href="javascript:updateUser()" class="easyui-linkbutton" iconCls="icon-ok">更改</a>
    <a href="javascript:closeDialog2()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
</body>
</html>