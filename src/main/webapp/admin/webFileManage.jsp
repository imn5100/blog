<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>上传web文件管理</title>
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
                    $.post("/admin/webFile/delete.do", {ids: ids}, function (result) {
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

        function uploadWebFile() {
            var filename = $("#filename").val();
            var file = $("#file").val();
            if (file == null || file == "") {
                $.messager.alert("系统提示", "请添加需要上传的文件！");
                return;
            }
            var params = {
                "filename": filename,
            }
            $.ajaxFileUpload({
                type: 'POST',
                contentType: 'application/json',
                url: '/admin/webFile/upload.do',
                secureuri: false,
                processData: false,
                cache: false,
                fileElementId: 'file',                             // file标签的id
                async: true,
                data: params,
                dataType: 'json',  // 返回数据的类型
                success: function (data) {
                    if (data.success == true) {
                        var html = "URL:" + '<a href="' + data.url + '" target="_blank" >' + data.url + '</a></br>';
                        html += '<img  style="max-width: 100%;height: 350px;" src="' + data.url + '"/>'
                        $("#url").html(html);
                        $.messager.alert("系统提示", "上传成功");
                    }
                    else {
                        $.messager.alert("系统提示", "上传失败");
                    }
                }
            });
            return false;
        }
        function uploadWebFileSMMS() {
            var file = $("#fileForSMMS").val();
            if (file == null || file == "") {
                $.messager.alert("系统提示", "请添加需要上传的文件！");
                return;
            }
            $.ajaxFileUpload({
                type: 'POST',
                contentType: 'application/json',
                url: '/admin/webFile/SMMSUpload.do',
                secureuri: false,
                processData: false,
                cache: false,
                fileElementId: 'fileForSMMS', // file标签的id
                async: true,
                data: "",
                dataType: 'json',  // 返回数据的类型
                success: function (data) {
                    if (data.success == true) {
                        var html = "URL:" + '<a href="' + data.uploadFile.url + '" target="_blank" >' + data.uploadFile.url + '</a></br>';
                        html += '<img  style="max-width: 100%;height: 350px;" src="' + data.uploadFile.url + '"/>'
                        $("#showData").html(html);
                        $.messager.alert("系统提示", "上传图片成功");
                    }
                    else {
                        $.messager.alert("系统提示", "上传图片失败");
                    }
                }
            });
            return false;
        }

        function openUploadWebFileDialog() {
            $("#dlg").dialog("open").dialog("setTitle", "上传文件(大文件请使用工具上传)");
            url = "/admin/webFile/save.do";
        }
        function openUploadWebFileDialogSMMS() {
            $("#dlg2").dialog("open").dialog("setTitle", "上传图片(SMMS)");
        }
        function resetValue() {
            $("#fileName").val("");
            $("#file").val("");
        }
        function closeUploadFileDialog() {
            $("#dlg").dialog("close");
            resetValue();
        }
        function closeUploadImageDialog() {
            $("#dlg2").dialog("close");
            $("#fileForSMMS").val("")
        }
        function searchFile() {
            $("#dg").datagrid('load', {
                "prefix": $("#prefix").val()
            });
        }
        function formatUrl(val, row) {
            return '<a target="_blank" href="' + val + '">' + val + '</a>'
        }
    </script>
</head>
<body style="margin: 1px">
<table id="dg" title="web文件管理" class="easyui-datagrid"
       fitColumns="true" pagination="true" rownumbers="true"
       url="/admin/webFile/list.do" fit="true" toolbar="#tb">
    <thead>
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th field="key" align="center">文件key</th>
        <th field="hash" align="center">hash</th>
        <th field="fsize" align="center">文件大小</th>
        <th field="putTime" align="center">存储时间</th>
        <th field="mimeType" align="center">文件类型</th>
        <th field="url" align="center" formatter="formatUrl">访问链接</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <div>
        <a href="javascript:openUploadWebFileDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">上传文件(QINIU)</a>
        <a href="javascript:openUploadWebFileDialogSMMS()" class="easyui-linkbutton" iconCls="icon-add" plain="true">上传图片(SMMS)</a>
        <a href="javascript:deleteFile()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>

        <div>
            &nbsp;文件名：&nbsp;<input type="text" id="prefix" size="20" onkeydown="if(event.keyCode==13) searchFile()"/>
            <a href="javascript:searchFile()" class="easyui-linkbutton" iconCls="icon-search" plain="true">查询</a>
        </div>
    </div>
</div>
<div id="dlg" class="easyui-dialog" style="width:500%;height:500%;padding: 5px 5px"
     closed="true" buttons="#dlg-buttons">
    <form id="fm" method="post">
        <table cellspacing="8px">
            <tr>
                <td>文件名：</td>
                <td><input type="text" id="filename" class="easyui-validatebox" placeholder="默认取上传文件名"/>
                </td>
            </tr>
            <tr>
                <td>文件：</td>
                <td><input type="file" id="file" name="file"/></td>
            </tr>
        </table>
        <div id="url">

        </div>
    </form>
</div>
<div id="dlg2" class="easyui-dialog" style="width:500%;height:500%;padding: 5px 5px"
     closed="true" buttons="#dlg2-buttons">
    <form id="fm2" method="post">
        <table cellspacing="8px">
            <tr>
                <td>文件：</td>
                <td><input type="file" id="fileForSMMS" name="file"/></td>
            </tr>
        </table>
        <div id="showData">
        </div>
    </form>
</div>
<div id="dlg-buttons">
    <a href="javascript:uploadWebFile()" class="easyui-linkbutton" iconCls="icon-ok">上传</a>
    <a href="javascript:closeUploadFileDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
<div id="dlg2-buttons">
    <a href="javascript:uploadWebFileSMMS()" class="easyui-linkbutton" iconCls="icon-ok">上传</a>
    <a href="javascript:closeUploadImageDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
</body>
</html>