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
    <meta name="description" content="技术博客,后端,java">
    <link href="/favicon.ico" rel="SHORTCUT ICON">
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="/static/bootstrap3/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/bootstrap3/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="/static/css/main.css">
    <script src="/static/bootstrap3/js/jquery-1.11.2.min.js"></script>
    <script src="/static/bootstrap3/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        $(window).scroll(function () {
            if ($(window).scrollTop() == 0) {
                $(".cd-top").hide();
            } else {
                $(".cd-top").show();
            }
        });
        var duoshuoQuery = {short_name: "shawblog"};
        (function () {
            var ds = document.createElement('script');
            ds.type = 'text/javascript';
            ds.async = true;
            ds.src = "/static/embed.js";//(document.location.protocol == 'https:' ? 'https:' : 'http:') + '//static.duoshuo.com/embed.js';
            ds.charset = 'UTF-8';
            (document.getElementsByTagName('head')[0]
            || document.getElementsByTagName('body')[0]).appendChild(ds);
        })();
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
                <div class="panel-body">
                    <img class="img-responsive"
                         src="${pageContext.request.contextPath}/static/userImages/${blogger.imageName }"/>

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
                                    href="${pageContext.request.contextPath}/index.html?typeId=${blogTypeCount.id }">${blogTypeCount.typeName }</a><span
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
                                    href="${pageContext.request.contextPath}/index.html?releaseDateStr=${blogCount.releaseDateStr }">${blogCount.releaseDateStr }</a><span
                                    class="badge">${blogCount.blogCount }</span></span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <span class="glyphicon glyphicon-star"></span>
                    热评文章
                </div>
                <div class="panel-body">
                    <ul class="ds-top-threads" data-range="weekly" data-num-items="5"></ul>
                </div>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <span class="glyphicon glyphicon-user"></span>
                    最近访客
                </div>
                <div class="panel-body">
                    <div class="ds-recent-visitors"></div>
                </div>
            </div>


            <div class="panel panel-default">
                <div class="panel-heading">
                    <span class="glyphicon glyphicon-link"></span>
                    友链
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
<script>
    (function (i, s, o, g, r, a, m) {
        i['GoogleAnalyticsObject'] = r;
        i[r] = i[r] || function () {
                    (i[r].q = i[r].q || []).push(arguments)
                }, i[r].l = 1 * new Date();
        a = s.createElement(o),
                m = s.getElementsByTagName(o)[0];
        a.async = 1;
        a.src = g;
        m.parentNode.insertBefore(a, m)
    })(window, document, 'script', 'https://www.google-analytics.com/analytics.js', 'ga');
    ga('create', 'UA-87589134-1', 'auto');
    ga('send', 'pageview');
</script>
<script>
    (function () {
        var bp = document.createElement('script');
        var curProtocol = window.location.protocol.split(':')[0];
        if (curProtocol === 'https') {
            bp.src = 'https://zz.bdstatic.com/linksubmit/push.js';
        }
        else {
            bp.src = 'http://push.zhanzhang.baidu.com/push.js';
        }
        var s = document.getElementsByTagName("script")[0];
        s.parentNode.insertBefore(bp, s);
    })();

</script>
</html>
