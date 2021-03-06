<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>修改个人信息页面</title>
    <link rel="stylesheet" type="text/css" href="/static/jquery-easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="/static/jquery-easyui/themes/icon.css">
    <script type="text/javascript" src="/static/jquery-easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/jquery-easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/static/jquery-easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" charset="utf-8" src="/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="/static/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="/static/ueditor/lang/zh-cn/zh-cn.js"></script>

    <script type="text/javascript" src="/static/ajaxfileupload.js"></script>
    <script type="text/javascript">
        function isFloat(num) {
            var r = "^\\d+(\\.\\d+)?$"
            return r.match(num);
        }
        function updateUserInfo() {
            var id = $("#id").val();
            var userName = $("#id").val();
            var nickName = $("#nickName").val();
            var sign = $("#sign").val();
            var imageUrl = $("#imageUrl").val();
            var backgroundUrl = $("#backgroundUrl").val();
            var aspectRatio = $("#aspectRatio").val();
            var proFile = UE.getEditor('proFile').getContent();
            if (aspectRatio != null || aspectRatio != "") {
                //非浮点数
                if ((!isFloat(aspectRatio))) {
                    //非整数
                    if (isNaN(aspectRatio)) {
                        $.messager.alert("系统提示", "背景高度设置错误");
                        return
                    }
                }
            }
            var params = {
                "id": id,
                "userName": userName,
                "nickName": nickName,
                "sign": sign,
                "proFile": proFile,
                "imageUrl": imageUrl,
                "backgroundUrl": backgroundUrl,
                "aspectRatio": aspectRatio
            }
            $.ajaxFileUpload({
                type: 'POST',
                contentType: 'application/json',
                url: '/admin/blogger/save.do',
                secureuri: false,
                processData: false,
                cache: false,
                fileElementId: 'imageFile',                             // file标签的id
                async: true,
                data: params,
                dataType: 'json',  // 返回数据的类型
                success: function (data) {
                    if (data.success == true) {
                        $("#imageUrl").val(data.imageUrl);
                        $.messager.alert("系统提示", "修改成功");
                    }
                    else {
                        $.messager.alert("系统提示", "修改失败");
                    }
                }
            });
            return false;
        }
    </script>
</head>
<body style="margin: 10px">
<div id="p" class="easyui-panel" title="修改个人信息" style="padding: 10px">
    <form id="form1" action="" method="post" enctype="multipart/form-data">
        <table cellspacing="20px">
            <tr>
                <td width="80px">登录名：</td>
                <td>
                    <input type="hidden" id="id" name="id" value="${currentUser.id }"/>
                    <input type="text" id="userName" name="userName" style="width: 200px;"
                           value="${currentUser.userName }" readonly="readonly"/>
                </td>
            </tr>
            <tr>
                <td>昵称：</td>
                <td><input type="text" id="nickName" name="nickName" style="width: 200px;"/></td>
            </tr>
            <tr>
                <td>签名：</td>
                <td><input type="text" id="sign" name="sign" value="${currentUser.sign }" style="width: 400px;"/></td>
            </tr>
            <tr>
                <td>头像：</td>
                <td><input type="file" id="imageFile" name="imageFile"/></td>
            </tr>
            <tr>
                <td>头像URL</td>
                <td><input type="text" id="imageUrl" name="imageUrl" style="width: 400px;"/></td>
            </tr>
            <tr>
                <td>背景URL</td>
                <td><input type="text" id="backgroundUrl" name="backgroundUrl" style="width: 400px;"/>背景宽高比<input
                        type="text" id="aspectRatio" name="aspectRatio" style="width: 50px;"/></td>
            </tr>
            <tr>
                <td valign="top">简介：</td>
                <td>
                    <div id="proFile" type="text/plain" style="width:100%;height:500px;"></div>
                    <input type="hidden" id="pF" name="proFile"/>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <a href="javascript:updateUserInfo()" class="easyui-linkbutton"
                       data-options="iconCls:'icon-submit'">提交</a>
                </td>
            </tr>
        </table>
    </form>
</div>

<script type="text/javascript">

    //实例化编辑器
    //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
    var ue = UE.getEditor('proFile');

    ue.addListener("ready", function () {
        //通过ajax请求数据
        UE.ajax.request("/admin/blogger/find.do",
            {
                method: "post",
                async: false,
                data: {},
                onsuccess: function (result) {
                    result = eval("(" + result.responseText + ")");
                    $("#nickName").val(result.nickName);
                    $("#sign").val(result.sign);
                    $("#nickName").val(result.nickName);
                    $("#imageUrl").val(result.imageName);
                    $("#backgroundUrl").val(result.backgroundImage);
                    $("#aspectRatio").val(result.aspectRatio);
                    UE.getEditor('proFile').setContent(result.proFile);
                }
            }
        );
    });

</script>
</body>
</html>