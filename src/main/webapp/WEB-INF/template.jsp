<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="/static/css/font-awesome.css">                <!-- Font Awesome -->
    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/css/template-style.css">
    <script src="/static/js/jquery-1.11.3.min.js"></script>
</head>
<body id="top" class="page-2">

<div class="tm-navbar-container tm-navbar-container-dark">
    <nav class="navbar navbar-full navbar-fixed-top bg-inverse">
        <button class="navbar-toggler hidden-md-up" type="button" data-toggle="collapse" data-target="#tmNavbar">
            &#9776;
        </button>
        <div class="collapse navbar-toggleable-sm" id="tmNavbar">
            <ul class="nav navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" href="/">首页</a>
                </li>
                <li class="nav-item jumpHook">
                    <a class="nav-link" href="/#tm-section-2">博客</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/about.html">关于</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/blogger/about.html">歌单</a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" id="navbarDropdownMenuLink" data-toggle="dropdown"
                       aria-haspopup="true" aria-expanded="false">
                        实验室
                    </a>
                    <div class="dropdown-menu bg-inverse " style=""
                         aria-labelledby="navbarDropdownMenuLink">
                        <a class="dropdown-item bg-inverse" target="_blank" href="/rainyRoom.html">Rainy Mood</a>
                        <a class="dropdown-item bg-inverse" target="_blank" href="/remoteTask/main.html">远程任务执行器</a>
                    </div>
                </li>
            </ul>

        </div>
    </nav>
</div>
<div class="container-fluid">
    <jsp:include page="${mainPage}"></jsp:include>
</div>
<jsp:include page="foreground/common/foot.jsp"></jsp:include>
<script>
    $(document).ready(function () {

        var mobileTopOffset = 54;
        var tabletTopOffset = 74;
        var desktopTopOffset = 79;
        var topOffset = desktopTopOffset;

        if ($(window).width() <= 767) {
            topOffset = mobileTopOffset;
        }
        else if ($(window).width() <= 991) {
            topOffset = tabletTopOffset;
        }

        $('#tmNavbar').singlePageNav({
            'currentClass': "active",
            offset: topOffset,
            'filter': ':not(.external)'
        });

        if ($(window).width() < 570) {
            $('.tm-full-width-large-table').addClass('table-responsive');
        }
        else {
            $('.tm-full-width-large-table').removeClass('table-responsive');
        }

        $(window).resize(function () {

            if ($(window).width() < 768) {
                topOffset = mobileTopOffset;
            }
            else if ($(window).width() <= 991) {
                topOffset = tabletTopOffset;
            }
            else {
                topOffset = desktopTopOffset;
            }

            $('#tmNavbar').singlePageNav({
                'currentClass': "active",
                offset: topOffset,
                'filter': ':not(.external)'
            });

            if ($(window).width() < 570) {
                $('.tm-full-width-large-table').addClass('table-responsive');
            }
            else {
                $('.tm-full-width-large-table').removeClass('table-responsive');
            }
        });

    });

</script>
</body>
</html>