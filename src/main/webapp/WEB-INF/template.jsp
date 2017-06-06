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
                    <a class="nav-link external" href="/">首页</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/">博客</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/">歌单</a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" id="dropdown04"
                       data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">实验室</a>
                    <div class="dropdown-menu bg-inverse" aria-labelledby="dropdown04">
                        <a class="dropdown-item bg-inverse" href="#">Rainy Mood</a>
                        <a class="dropdown-item bg-inverse" href="#">远程任务执行器</a>
                    </div>
                </li>
            </ul>

        </div>
    </nav>

</div>

<div class="container-fluid">
    <jsp:include page="${mainPage}"></jsp:include>
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12">
            <p class="text-xs-center tm-footer-text">Copyright &copy; 2016 Company Name</p>
        </div>
    </div>
</div>
<script src="/static/js/jquery-1.11.3.min.js"></script>
<script src="/static/js/tether.min.js"></script>
<script src="/static/js/bootstrap.min.js"></script>
<script src="/static/js/jquery.singlePageNav.min.js"></script>
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


        $('#tmNavbar a').click(function () {
            $('#tmNavbar').collapse('hide');
        });


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