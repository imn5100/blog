<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="${pageContext.request.contextPath}/favicon.ico" rel="SHORTCUT ICON">
    <title>${pageTitle}</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/bootstrap3/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/bootstrap3/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/blog.css">
    <script src="${pageContext.request.contextPath}/static/bootstrap3/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/bootstrap3/js/jquery-1.11.2.min.js"></script>
    <script>
        //去除百度统计代码
        //	var _hmt = _hmt || [];
        //(function() {
        // var hm = document.createElement("script");
        // hm.src = "//hm.baidu.com/hm.js?aa5c701f4f646931bf78b6f40b234ef5";
        //var s = document.getElementsByTagName("script")[0];
        //s.parentNode.insertBefore(hm, s);
        //})();
    </script>

    <style type="text/css">
        body {
            padding-top: 50px;
            padding-bottom: 20px;
            background-image: url("${pageContext.request.contextPath}/static/images/star02.png");
            /*font-family:  Helvetica, Arial,sans-serif;*/
        }
    </style>
</head>
<body>
<div class="container">
    <jsp:include page="/foreground/common/head.jsp"/>

    <jsp:include page="/foreground/common/menu.jsp"/>

    <div class="row">
        <div class="col-md-9">
            <jsp:include page="${mainPage }"></jsp:include>
        </div>


        <div class="col-md-3">
            <div class="data_list">
                <div class="data_list_title">
                    <img src="${pageContext.request.contextPath}/static/images/user_icon.png"/>
                    ${blogger.nickName }
                </div>
                <div class="user_image">
                    <img src="${pageContext.request.contextPath}/static/userImages/${blogger.imageName }"/>
                </div>
                <div class="userSign">${blogger.sign }</div>
            </div>

            <div class="data_list">
                <div class="data_list_title">
                    <img src="${pageContext.request.contextPath}/static/images/byType_icon.png"/>
                    按日志类别
                </div>
                <div class="datas">
                    <ul>
                        <c:forEach var="blogTypeCount" items="${blogTypeCountList }">
                            <li><span><a
                                    href="${pageContext.request.contextPath}/index.html?typeId=${blogTypeCount.id }">${blogTypeCount.typeName }(${blogTypeCount.blogCount })</a></span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

            <div class="data_list">
                <div class="data_list_title">
                    <img src="${pageContext.request.contextPath}/static/images/byDate_icon.png"/>
                    按日志日期
                </div>
                <div class="datas">
                    <ul>
                        <c:forEach var="blogCount" items="${blogCountList }">
                            <li><span><a
                                    href="${pageContext.request.contextPath}/index.html?releaseDateStr=${blogCount.releaseDateStr }">${blogCount.releaseDateStr }(${blogCount.blogCount })</a></span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <div class="data_list">
                <div class="data_list_title">
                    <img src="${pageContext.request.contextPath}/static/images/hot.png"/>
                    热评文章
                </div>
                <!-- 多说热评文章 start -->
                <div class="ds-top-threads" data-range="daily" data-num-items="5"></div>
                <!-- 多说热评文章 end -->
                <!-- 多说公共JS代码 start (一个网页只需插入一次) -->
                <script type="text/javascript">
                    var duoshuoQuery = {short_name: "shawblog"};
                    (function () {
                        var ds = document.createElement('script');
                        ds.type = 'text/javascript';
                        ds.async = true;
                        ds.src = (document.location.protocol == 'https:' ? 'https:' : 'http:') + '//static.duoshuo.com/embed.unstable.js';
                        ds.charset = 'UTF-8';
                        (document.getElementsByTagName('head')[0]
                        || document.getElementsByTagName('body')[0]).appendChild(ds);
                    })();
                </script>
                <!-- 多说公共JS代码 end -->
            </div>

            <div class="data_list">
                <div class="data_list_title">
                    <img src="${pageContext.request.contextPath}/static/images/link_icon.png"/>
                    友链
                </div>
                <div class="datas">
                    <ul>
                        <c:forEach var="link" items="${linkList }">
                            <li><span><a href="${link.linkUrl }" target="_blank">${link.linkName }</a></span></li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>

    </div>

    <jsp:include page="/foreground/common/foot.jsp"/>
</div>
</body>
</html>