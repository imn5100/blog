<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>修改博客页面</title>
    <link rel="stylesheet" type="text/css" href="/static/jquery-easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="/static/jquery-easyui/themes/icon.css">
    <script type="text/javascript" src="/static/jquery-easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/jquery-easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/static/jquery-easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" charset="utf-8" src="/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="/static/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="/static/ueditor/lang/zh-cn/zh-cn.js"></script>
    <script type="text/javascript">
        function submitData() {
            var title = $("#title").val();
            var blogTypeId = $("#blogTypeId").combobox("getValue");
//            给图片添加适配屏幕样式
            var content = UE.getEditor('editor').getContent().replace(/<img src=/g, "<img class='img-responsive' src=");
            var keyWord = $("#keyWord").val();

            if (title == null || title == '') {
                alert("请输入标题！");
            } else if (blogTypeId == null || blogTypeId == '') {
                alert("请选择博客类别！");
            } else if (content == null || content == '') {
                alert("请输入内容！");
            } else {
                $.post("/admin/blog/save.do", {
                    'id': '${param.id}',
                    'title': title,
                    'blogType.id': blogTypeId,
                    'content': content,
                    'contentNoTag': UE.getEditor('editor').getContentTxt(),
                    'summary': UE.getEditor('editor').getContentTxt().substr(0, 200),
                    'keyWord': keyWord
                }, function (result) {
                    if (result.success) {
                        alert("博客修改成功！");
                    } else {
                        alert("博客修改失败！");
                    }
                }, "json");
            }
        }
        $(function () {
            $(".cd-top").eq(0).click(function () {
                $("html,body").animate({scrollTop: 0}, 500);
                return false;
            });
            $(".cd-bottom").eq(0).click(function () {
                $("html,body").animate({scrollTop: 10000}, 500);
                return false;
            });
        })
    </script>
    <style type="text/css">
        .cd-top {
            height: 40px;
            width: 40px;
            position: fixed;
            bottom: 10px;
            right: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, .05);
            background: url(/static/images/cd-top-arrow.svg) center 50% no-repeat rgba(52, 73, 94, .8);
            opacity: 1;
            -webkit-transition: opacity .3s 0s, visibility 0s .3s;
            -moz-transition: opacity .3s 0s, visibility 0s .3s;
            transition: opacity .3s 0s, visibility 0s .3s;
        }

        .cd-bottom {
            height: 40px;
            width: 40px;
            position: fixed;
            top: 10px;
            right: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, .05);
            background: url(/static/images/cd-bottom-arrow.svg) center 50% no-repeat rgba(52, 73, 94, .8);
            opacity: 1;
            -webkit-transition: opacity .3s 0s, visibility 0s .3s;
            -moz-transition: opacity .3s 0s, visibility 0s .3s;
            transition: opacity .3s 0s, visibility 0s .3s;
        }
    </style>
</head>
<body style="margin: 10px">
<div id="p" class="easyui-panel" title="修改博客" style="padding: 10px">
    <table cellspacing="20px">
        <tr>
            <td width="80px">博客标题：</td>
            <td><input type="text" id="title" name="title" style="width: 400px;"/></td>
        </tr>
        <tr>
            <td>所属类别：</td>
            <td>
                <select class="easyui-combobox" style="width: 154px" id="blogTypeId" name="blogType.id" editable="false"
                        panelHeight="auto">
                    <option value="">请选择博客类别...</option>
                    <c:forEach var="blogType" items="${blogTypeCountList }">
                        <option value="${blogType.id }">${blogType.typeName }</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td>关键字：</td>
            <td><input type="text" id="keyWord" name="keyWord" style="width: 400px;"/>&nbsp;(多个关键字中间用空格隔开)</td>
        </tr>
        <tr>
            <td valign="top">博客内容：</td>
            <td>
                <div id="editor" type="text/plain" style="width:690px;height:750px;"></div>
            </td>
        </tr>
        <tr>
            <td></td>
            <td>
                <a href="javascript:submitData()" class="easyui-linkbutton"
                   data-options="iconCls:'icon-submit'">修改博客</a>
            </td>
        </tr>
    </table>
</div>
<a class="cd-top"></a>
<a class="cd-bottom"></a>
<script type="text/javascript">
    //实例化编辑器
    //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
    var ue = UE.getEditor('editor');
    ue.addListener("ready", function () {
        //通过ajax请求数据
        UE.ajax.request("/admin/blog/findById.do",
            {
                method: "post",
                async: false,
                data: {"id": "${param.id}"},
                onsuccess: function (result) {
                    result = eval("(" + result.responseText + ")");
                    $("#title").val(result.title);
                    $("#keyWord").val(result.keyWord);
                    $("#blogTypeId").combobox("setValue", result.blogType.id);
                    UE.getEditor('editor').setContent(result.content);
                }
            }
        );
    });
</script>
</body>
</html>

