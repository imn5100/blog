<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="技术博客,后端,java,python">
    <link href="/favicon.ico" rel="SHORTCUT ICON">
    <title>${pageTitle}</title>
    <link rel="search" type="application/opensearchdescription+xml" href="/search.xml" title="shawblog">
    <link rel="stylesheet" href="/static/bootstrap3/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/bootstrap3/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="/static/css/main.css">
    <script src="/static/bootstrap3/js/jquery.js"></script>
    <script src="/static/bootstrap3/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        //节流函数 避免监听事件频繁调用函数
        //method 要执行的函数
        //delay 延迟
        //duration  在time时间内必须执行一次
        function throttle(method, delay, duration) {
            var timer = null, begin = new Date();
            return function () {
                var context = this, args = arguments, current = new Date();
                clearTimeout(timer);
                if (current - begin >= duration) {
                    method.apply(context, args);
                    begin = current;
                } else {
                    timer = setTimeout(function () {
                        method.apply(context, args);
                    }, delay);
                }
            }
        }
        function showTop() {
            if ($(window).scrollTop() < 100) {
                $(".cd-top").hide();
            } else {
                $(".cd-top").show();
            }
        }
        $(window).scroll(throttle(showTop, 500, 1000));
        $(function () {
            $("#close_side").click(function () {
                $("#contents").attr("class", "col-md-12")
                $("#side").hide();
                $("#up").show();
            });
            $(".cd-top").eq(0).click(function () {
                $("html,body").animate({scrollTop: 0}, 500);
                return false;
            });
        })
    </script>
    <style type="text/css">
        body {
        <c:if test="${blogger.backgroundImage !=null && blogger.backgroundImage !=''}"> background-image: url("${blogger.backgroundImage}");
        </c:if>
        }
    </style>
</head>
<body>
<div class="container None">
    <jsp:include page="/WEB-INF/foreground/common/head.jsp"/>
    <div>
        <div class="col-md-9" id="contents">
            <jsp:include page="${mainPage }"></jsp:include>
        </div>
        <div class="col-md-3" id="side">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <span class="glyphicon glyphicon-tower"></span>
                    ${blogger.nickName}
                </div>
                <div class="panel-body" align="center">
                    <img class="img-responsive"
                         src="${blogger.imageName}"/>

                    <div align="center">${blogger.sign }</div>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <span class="glyphicon glyphicon-list"></span>
                    Categories
                </div>
                <div class="panel-body">
                    <ul>
                        <c:forEach var="blogTypeCount" items="${blogTypeCountList }">
                            <li><span><a
                                    href="/index.html?typeId=${blogTypeCount.id }">${blogTypeCount.typeName }</a><span
                                    class="badge">${blogTypeCount.blogCount }</span></span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <span class="glyphicon glyphicon-calendar"></span>
                    Calendar
                </div>
                <div class="panel-body">
                    <ul>
                        <c:forEach var="blogCount" items="${blogCountList }">
                            <li><span><a
                                    href="/index.html?releaseDateStr=${blogCount.releaseDateStr }">${blogCount.releaseDateStr }</a><span
                                    class="badge">${blogCount.blogCount }</span></span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <span class="glyphicon glyphicon-link"></span>
                    Links
                </div>
                <div class="panel-body">
                    <ul>
                        <c:forEach var="link" items="${linkList }">
                            <li><span><a href="${link.linkUrl }" target="_blank">${link.linkName }</a></span></li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading" align="center">
                    <span><a id="close_side">关闭侧边栏</a></span>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="/WEB-INF/foreground/common/foot.jsp"/>
</div>
<a class="cd-top"></a>
</body>
</html>
