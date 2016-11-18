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
        function deleteFile() {
            var selectedRows = $("#dg").datagrid("getSelections");
            if (selectedRows.length == 0) {
                $.messager.alert("系统提示", "请选择要删除的数据！");
                return;
            }
            var strIds = [];
            for (var i = 0; i < selectedRows.length; i++) {
                strIds.push(selectedRows[i].key);
            }
            var ids = strIds.join(",");
            $.messager.confirm("系统提示", "您确定要删除这<font color=red>" + selectedRows.length + "</font>条数据吗？", function (r) {
                if (r) {
                    $.post("${pageContext.request.contextPath}/admin/webFile/delete.do", {ids: ids}, function (result) {
                        if (result.success) {
                            $.messager.alert("系统提示", "删除文件成功！");
                            $("#dg").datagrid("reload");
                        } else {
                            if (result.msg) {
                                $.messager.alert("系统提示", "删除文件失败," + msg);
                            } else {
                                $.messager.alert("系统提示", "删除文件失败！");
                            }
                        }
                    }, "json");
                }
            });
        }
        function toValid() {

        }
        function notValid() {

        }
        function searchFile() {
            var type = ""
            if ($("#type").val() != 0) {
                type = $("#type").val();
            }
            $("#dg").datagrid('load', {
                "filename": $("#filename").val(),
                "mimetype": $("#mimetype").val(),
                "type": type
            });
        }
        function formatUrl(val, row) {
            return '<a target="_blank" href="' + val + '">' + val + '</a>'
        }
        function formatTime(val, row) {
            return new Date(val).toLocaleString();
        }
        function isValid(val, row) {
            return val == 1 ? "有效" : "无效";
        }
        function formatType(val, row) {
            if (val == 1) {
                return "七牛";
            } else if (val == 2) {
                return "SMMS";
            } else {
                return "其他"
            }
        }
        function formatSize(val, row) {
            return new String(Number(val / 1024).toFixed(2)) + "kb";
        }
        var detailMap = new Map()
        function formatDetail(val, row) {
            if (val == undefined || val == null) {
                return "";
            } else {
                detailMap.set(row.id, val);
                return '<a href="javascript:seeDetail(' + row.id + ')">查看</a>';
            }
        }
        function seeDetail(val) {
            $("#detail").html(detailMap.get(val));
            $("#dlg").dialog("open").dialog("setTitle", "文件其他详细信息");
        }
    </script>
</head>
<body style="margin: 1px">
<table id="dg" title="外链文件管理" class="easyui-datagrid"
       fitColumns="true" pagination="true" rownumbers="true"
       url="/admin/webFile/listWebFile.do" fit="true" toolbar="#tb">
    <thead>
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th field="id" align="center">编号</th>
        <th field="filename" align="center">文件名</th>
        <th field="mimetype" align="center">文件类型</th>
        <th field="storename" align="center">外部名</th>
        <th field="type" align="center" formatter="formatType">来源</th>
        <th field="size" align="center" formatter="formatSize">大小</th>
        <th field="uploadTime" align="center" formatter="formatTime">存储时间</th>
        <th field="url" align="center" formatter="formatUrl">访问链接</th>
        <th field="path" align="center">路径</th>
        <th field="hash" align="center">hash</th>
        <th field="isValid" align="center" formatter="isValid">是否有效</th>
        <th field="detail" align="center" formatter="formatDetail">其他信息</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <div>
        <a href="javascript:toValid()" class="easyui-linkbutton" iconCls="icon-edit"
           plain="true">标记有效</a>
        <a href="javascript:notValid()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">标记无效</a>
        <a href="javascript:deleteFile()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>

        <div>
            &nbsp;文件名：&nbsp;<input type="text" id="filename" size="20" onkeydown="if(event.keyCode==13) searchFile()"/>
            &nbsp;文件类型：&nbsp;<input type="text" id="mimetype" size="20">
            <select id="type">
                <option value="0" selected="selected">请选择文件存储源</option>
                <option value="1">七牛</option>
                <option value="2">SMMS</option>
            </select>
            <a href="javascript:searchFile()" class="easyui-linkbutton" iconCls="icon-search" plain="true">查询</a>
        </div>
    </div>
</div>
<div id="dlg" class="easyui-dialog" style="width:600px;height:120px;padding: 10px 20px" closed="true">
    <div id="detail">
    </div>
</div>
</body>
</html>