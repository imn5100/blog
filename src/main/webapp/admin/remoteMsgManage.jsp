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
        var url;
        function deleteMsg() {
            var selectedRows = $("#dg").datagrid("getSelections");
            if (selectedRows.length == 0) {
                $.messager.alert("系统提示", "请选择要删除的数据！");
                return;
            }
            var strIds = [];
            for (var i = 0; i < selectedRows.length; i++) {
                strIds.push(selectedRows[i].id);
            }
            var ids = strIds.join(",");
            $.messager.confirm("系统提示", "您确定要删除这<font color=red>" + selectedRows.length + "</font>条数据吗？", function (r) {
                if (r) {
                    $.post("/admin/remote/batchDelete.do", {ids: ids}, function (result) {
                        if (result.success) {
                            $.messager.alert("系统提示", "删除任务成功！");
                            $("#dg").datagrid("reload");
                        } else {
                            $.messager.alert("系统提示", "删除任务失败！");
                        }
                    }, "json").error(function () {
                        $.messager.alert("系统提示", "系统异常或登录超时，请刷新重试！");
                    });
                }
            });
        }
        function sendMsg() {
            var topic = $("#topic").val();
            var contents = $("#contents").val();
            var other = $("#other").val();
            if (topic == null || topic == "" || contents == null || contents == "") {
                $.messager.alert("系统提示", "请填写必要内容！");
                return;
            }
            params = {
                "topic": topic,
                "contents": contents,
                "other": other
            }
            $.post("/admin/remote/pushMsg.do", params, function (result, textstatus, xhr) {
                if (result == "200") {
                    $.messager.alert("系统提示", "添加成功！");
                    closeDialog2()
                    $("#dg").datagrid("reload");
                } else {
                    $.messager.alert("系统提示", "添加失败！");
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
        function updateMsg() {
            var id = $("#id").val();
            var topic = $("#topic_u").val();
            var contents = $("#contents_u").val();
            var other = $("#other_u").val();
            var status = $("#status_u").val();
            if (id == null || id == "" || status == null || status == "" || topic == null || topic == "" || contents == null || contents == "") {
                $.messager.alert("系统提示", "请填写必要内容！");
                return;
            }
            params = {
                "id": id,
                "topic": topic,
                "contents": contents,
                "other": other,
                "status": status
            }
            $.post("/admin/remote/updateMsg.do", params, function (result, textstatus, xhr) {
                if (result == "200") {
                    $.messager.alert("系统提示", "更新成功！");
                    closeDialog_u()
                    $("#dg").datagrid("reload");
                } else {
                    $.messager.alert("系统提示", "更新失败！");
                }
            }, "json").error(function () {
                $.messager.alert("系统提示", "系统异常或登录超时，请刷新重试！");
            });
            return false;
        }
        function addWhiteList() {
            var ip = $("#ip").val();
            if (ip == null || ip == "") {
                $.messager.alert("系统提示", "请填写必要内容！");
                return;
            }
            params = {
                "ip": ip
            }
            $.post("/admin/remote/addWhiteList.do", params, function (result, textstatus, xhr) {
                if (result == "200") {
                    $.messager.alert("系统提示", "添加成功！");
                    closeDialog3()
                } else {
                    $.messager.alert("系统提示", "添加失败！");
                }
            }, "json").error(function () {
                $.messager.alert("系统提示", "系统异常或登录超时，请刷新重试！");
            });
            return false;
        }
        function searchMsg() {
            var status = ""
            if ($("#status").val() != 0) {
                status = $("#status").val();
            }
            $("#dg").datagrid('load', {
                "topic": $("#searchTopic").val(),
                "status": status
            });
        }
        function removeIp(ip) {
            $.post("/admin/remote/removeWhiteList.do?ip=" + ip, function (result, textstatus, xhr) {
                if (result == "200") {
                    $.messager.alert("系统提示", "删除成功！");
                    closeDialog4()
                } else {
                    $.messager.alert("系统提示", "删除失败！");
                }
            }, "json").error(function () {
                $.messager.alert("系统提示", "系统异常或登录超时，请刷新重试！");
            });
        }
        function formatTime(val, row) {
            if (val == null) {
                return ""
            }
            return new Date(val).toLocaleString();
        }
        function formatStatus(val, row) {
            if (val == 1) {
                return "提交";
            } else if (val == 2) {
                return "开始";
            } else if (val == 3) {
                return "完成"
            } else if (val == 4) {
                return "失败"
            } else {
                return "未知状态"
            }
        }
        var detailMap = new Map()
        function formatDetail(val, row) {
            if (val == undefined || val == null) {
                return "";
            } else {
                if (val.length < 100) {
                    return val;
                }
                detailMap.set(row.id, val);
                return '<a href="javascript:seeDetail(' + row.id + ')">查看</a>';
            }
        }
        function seeDetail(val) {
            $("#detail").html("<textarea style='margin: 0px; height: 300px; width: 400px;' readonly='readonly' >" + detailMap.get(val) + "</textarea>");
            $("#dlg").dialog("open").dialog("setTitle", "详情");
        }
        function openSendMsgDialog() {
            $("#dlg2").dialog("open").dialog("setTitle", "添加远程消息任务");
        }
        function openDialog3() {
            $("#dlg3").dialog("open").dialog("setTitle", "添加白名单");
        }
        function openDialog4() {
            $.post("/admin/remote/whiteList.do", function (result, textstatus, xhr) {
                if (result.code == 200) {
                    ip_list = result.data
                    str = ""
                    ip_list.forEach(function (element, index) {
                        str += "<tr><td>" + String(index + 1) + "</td><td>"
                        str += String(element)
                        str += "</td><td>"
                        str += "<a href=javascript:removeIp('" + String(element) + "')>删除</a></tr>"
                    })
                    $("#ipList").html(str)
                }
            }, "json").error(function () {
                $.messager.alert("系统提示", "系统异常或登录超时，请刷新重试！");
            });
            $("#dlg4").dialog("open").dialog("setTitle", "管理白名单");
        }
        function closeDialog4() {
            $("#dlg4").dialog("close");
            $("#ipList").html("")
        }
        function closeDialog2() {
            $("#dlg2").dialog("close");
            $("#topic").val("")
            $("#contents").val("")
            $("#other").val("")
        }
        function closeDialog3() {
            $("#dlg3").dialog("close");
            $("#ip").val("")
        }
        function closeDialog_u() {
            $("#dlg_u").dialog("close");
        }
    </script>
</head>
<body style="margin: 1px">
<table id="dg" title="远程消息任务管理" class="easyui-datagrid"
       fitColumns="true" pagination="true" rownumbers="true"
       url="/admin/remote/listMsg.do" fit="true" toolbar="#tb">
    <thead>
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th field="id" align="center">编号</th>
        <th field="topic" align="center">主题</th>
        <th field="appKey" align="center">appkey</th>
        <th field="status" formatter="formatStatus" align="center">状态</th>
        <th field="contents" formatter="formatDetail" align="center">执行内容</th>
        <th field="createTime" align="center" formatter="formatTime">创建时间</th>
        <th field="opTime" align="center" formatter="formatTime">操作时间</th>
        <th field="other" align="center" formatter="formatDetail">其他信息</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <div>
        <a href="javascript:openSendMsgDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">提交消息</a>
        <a href="javascript:openMsgModifyDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">编辑消息</a>
        <a href="javascript:deleteMsg()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除消息</a>
        <a href="javascript:openDialog3()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加白名单</a>
        <a href="javascript:openDialog4()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">管理白名单</a>

        <div>
            &nbsp;任务主题：&nbsp;<input type="text" id="searchTopic" size="20"
                                    onkeydown="if(event.keyCode==13) searchMsg()"/>
            <select id="status">
                <option value="0" selected="selected">请选择消息状态</option>
                <option value="1">提交</option>
                <option value="2">开始</option>
                <option value="3">完成</option>
                <option value="4">失败</option>
            </select>
            <a href="javascript:searchMsg()" class="easyui-linkbutton" iconCls="icon-search" plain="true">查询</a>
        </div>
    </div>
</div>
<div id="dlg2" class="easyui-dialog" style="width:500%;height:500%;padding: 5px 5px"
     closed="true" buttons="#dlg2-buttons">
    <form id="fm" method="post">
        <table cellspacing="8px">
            <tr>
                <td>主题：</td>
                <td><input type="text" id="topic" class="easyui-validatebox" placeholder=""/>
                </td>
            </tr>
            <tr>
                <td>内容：</td>
                <td><textarea id="contents"
                              style="margin: 0px; height: 200px; width: 400px;" class="easyui-validatebox"></textarea>
                </td>
            </tr>
            <tr>
                <td>附加内容：</td>
                <td><textarea id="other" style="margin: 0px; height: 100px; width: 400px;"></textarea></td>
            </tr>
        </table>

    </form>
</div>
<div id="dlg_u" class="easyui-dialog" style="width:500%;height:550%;padding: 5px 5px"
     closed="true" buttons="#dlg_u-buttons">
    <form id="fm_u" method="post">
        <table cellspacing="8px">
            <tr>
                <td>ID：</td>
                <td><input type="text" id="id" name="id" class="easyui-validatebox" required="true"
                           readonly="readonly"/></td>
            </tr>
            <tr>
                <td>主题：</td>
                <td><input type="text" id="topic_u" name="topic" class="easyui-validatebox" required="true"/>
                </td>
            </tr>
            <tr>
                <td>内容：</td>
                <td><textarea id="contents_u" name="contents" class="easyui-validatebox" required="true"
                              style="margin: 0px; height: 200px; width: 400px;"></textarea></td>
            </tr>
            <tr>
                <td>附加内容：</td>
                <td><textarea id="other_u" name="other" style="margin: 0px; height: 100px; width: 400px;"></textarea>
                </td>
            </tr>
            <tr>
                <td>
                    消息状态
                </td>
                <td>
                    <select id="status_u" name="status">
                        <option value="1">提交</option>
                        <option value="2">开始</option>
                        <option value="3">完成</option>
                        <option value="4">失败</option>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg3" class="easyui-dialog" style="width:300%;height:150%;padding: 5px 5px"
     closed="true" buttons="#dlg3-buttons">
    <table cellspacing="8px">
        <tr>
            <td>IP：</td>
            <td><input type="text" id="ip" class="easyui-validatebox" placeholder=""/>
            </td>
        </tr>
    </table>
</div>
<div id="dlg4" class="easyui-dialog" style="width:250%;height:250%;padding: 5px 5px" closed="true">
    <table border="1">
        <thead>
        <tr>
            <td>编号</td>
            <td>IP</td>
            <td>操作</td>
        </tr>
        </thead>
        <tbody id="ipList">
        </tbody>
    </table>
</div>
<div id="dlg" class="easyui-dialog" style="width:500px;height:500px;padding: 10px 20px" closed="true">
    <div id="detail">
    </div>
</div>
<div id="dlg2-buttons">
    <a href="javascript:sendMsg()" class="easyui-linkbutton" iconCls="icon-ok">提交</a>
    <a href="javascript:closeDialog2()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
<div id="dlg3-buttons">
    <a href="javascript:addWhiteList()" class="easyui-linkbutton" iconCls="icon-ok">提交</a>
    <a href="javascript:closeDialog3()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
<div id="dlg_u-buttons">
    <a href="javascript:updateMsg()" class="easyui-linkbutton" iconCls="icon-ok">更改</a>
    <a href="javascript:closeDialog_u()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
</body>
</html>