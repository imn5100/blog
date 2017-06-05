<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="技术博客,后端,java,python">
    <link href="/favicon.ico" rel="SHORTCUT ICON">
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/css/font-awesome.css">                <!-- Font Awesome -->
    <link rel="stylesheet" href="/static/css/magnific-popup.css">
    <link rel="stylesheet" href="/static/css/main-style.css">
    <link rel="stylesheet" href="/static/css/list.css">
</head>
<style>
    .badge {
        display: block;
        float: right;
        text-align: center;
        margin-right: 15px;
        margin-top: 3px;
        padding: 0 10px;
        font-size: 12px;
        color: #fff;
        background-color: #bbb;
        border-radius: 50%;
        transition: 0.5s ease-in-out;
    }

    .card-default > .card-body {
        background-color: #f8f8fd;
        box-shadow: 0 1px 2px rgba(0, 0, 0, 0.4), 0 0 30px rgba(10, 10, 0, 0.1) inset;
    }

    .cd-top {
        display: none;
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
</style>
<body id="top" class="home gray-bg">
<div class="container-fluid">
    <div class="row">

        <div class="tm-navbar-container">

            <nav class="navbar navbar-full navbar-fixed-top">

                <button class="navbar-toggler hidden-md-up" type="button" data-toggle="collapse"
                        data-target="#tmNavbar">
                    &#9776;
                </button>

                <div class="collapse navbar-toggleable-sm" id="tmNavbar">

                    <ul class="nav navbar-nav">
                        <li class="nav-item">
                            <a class="nav-link" href="#top">首页</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#tm-section-2">博客</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#">歌单</a>
                        </li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" id="dropdown04"
                               data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">实验室</a>
                            <div class="dropdown-menu" aria-labelledby="dropdown04">
                                <a class="dropdown-item" href="#">Rainy Mood</a>
                                <a class="dropdown-item" href="#">远程任务执行器</a>
                            </div>
                        </li>
                    </ul>

                </div>

            </nav>

        </div>

    </div>

    <div class="row">
        <div class="tm-intro">

            <section id="tm-section-1">
                <div class="tm-container text-xs-center tm-section-1-inner">
                    <img src="/static/img/tm-logo.png" alt="Logo" class="tm-logo">
                    <h1 class="tm-site-name">Shaw</h1>
                    <a href="#tm-section-2" class="tm-intro-link">Begin</a>
                    <p class="tm-intro-text">
                    <form class="form-inline my-2 my-lg-0" action="/blog/search.html" onsubmit="return checkData()">
                        <input class="form-control mr-sm-2" id="keyword" name="keyword"  type="text" placeholder="Search">
                    </form>
                    </p>

                </div>
            </section>

        </div>
    </div>
</div>
<div class="col-md-9" id="contents">
    <div class="row gray-bg">
        <div id="tm-section-2" class="tm-section">
            <div class="tm-container tm-container-wide">
                <c:forEach var="blog" items="${blogList}" varStatus="status">
                    <div class="tm-news-item">
                        <c:if test="${blog.imagesList.size() > 0}">
                            <c:forEach var="image" items="${blog.imagesList}">
                                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-6 col-xl-6 flex-order-2 tm-news-item-img-container">
                                    <img src="${image}" alt="Image" class="img-fluid tm-news-item-img"
                                         style="max-width:50%">
                                </div>
                            </c:forEach>
                        </c:if>
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12">
                            <h2 class="tm-news-title dark-gray-text">${blog.title}</h2>
                            <span class="info">
                                        <span class="icon-calendar"></span>${blog.releaseDateStr}&nbsp;
                                        <span class="fa fa-tags"></span>&nbsp;
                                        <c:forEach items="${blog.keywordList}" var="keyword">
                                            <a href="/blog/search.html?keyword=${keyword}">${keyword}</a>
                                        </c:forEach>
                                        <span class="fa fa-list-alt"></span>
                                        <a href="/index.html?typeId=${blog.blogType.id}">${blog.blogType.typeName}</a>
                                    </span>
                            <br>
                            <p class="tm-news-text">${blog.summary }...</p>
                            <a href="/blog/${blog.encodeId}.html"
                               class="btn tm-light-blue-bordered-btn tm-news-link">Read</a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
<div class="col-md-3" id="side" style="padding-top: 30px;">
    <div class="card card-default">
        <div class="card-heading">
            <span class="fa fa-tower"></span>
            ${blogger.nickName}
        </div>
        <div class="card-block" align="center">
            <img class="img-fluid"
                 src="${blogger.imageName}"/>
            <div align="center">${blogger.sign }</div>
        </div>
    </div>
    <div class="card card-default">
        <div class="card-heading">
            <span class="fa fa-list"></span>
            Categories
        </div>
        <div class="card-block">
            <ul>
                <c:forEach var="blogTypeCount" items="${blogTypeCountList}">
                    <li><span><a
                            href="/index.html?typeId=${blogTypeCount.id }">${blogTypeCount.typeName }</a><span
                            class="badge">${blogTypeCount.blogCount }</span></span>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
    <div class="card card-default">
        <div class="card-heading">
            <span class="fa fa-calendar"></span>
            Calendar
        </div>
        <div class="card-block">
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
    <div class="card card-default">
        <div class="card-heading">
            <span class="fa fa-link"></span>
            Links
        </div>
        <div class="card-block">
            <ul>
                <c:forEach var="link" items="${linkList }">
                    <li><span><a href="${link.linkUrl }" target="_blank">${link.linkName }</a></span></li>
                </c:forEach>
            </ul>
        </div>
    </div>
    <div class="card card-default">
        <div class="card-heading" align="center">
            <span><a id="close_side">关闭侧边栏</a></span>
        </div>
    </div>
</div>

<div class="col-md-12">
    <div class="row gray-bg">
        <footer class="tm-footer">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12">
                <p class="text-xs-center tm-footer-text">Power By <a href="https://github.com/imn5100"
                                                                     target="_blank">Imn5100</a></p>
                <p class="text-xs-center tm-footer-text">浙ICP备16038105号</p>
                <p class="text-xs-center tm-footer-text">浙公网安备33010602007235号</p>
            </div>
        </footer>
    </div>
</div>

<script src="/static/js/jquery-1.11.3.min.js"></script>
<script src="/static/js/tether.min.js"></script>
<script src="/static/js/bootstrap.min.js"></script>
<script src="/static/js/jquery.singlePageNav.min.js"></script>
<script src="/static/js/jquery.magnific-popup.min.js"></script>
<script>
    $(document).ready(function () {
        var mobileTopOffset = 54;
        var desktopTopOffset = 80;
        var topOffset = desktopTopOffset;

        if ($(window).width() <= 767) {
            topOffset = mobileTopOffset;
        }
        $('#tmNavbar').singlePageNav({
            'currentClass': "active",
            offset: topOffset,
            'filter': ':not(.external)'
        });

        $(window).resize(function () {
            if ($(window).width() <= 767) {
                topOffset = mobileTopOffset;
            }
            else {
                topOffset = desktopTopOffset;
            }

            $('#tmNavbar').singlePageNav({
                'currentClass': "active",
                offset: topOffset,
                'filter': ':not(.external)'
            });
        });

        $('#tmNavbar a').click(function () {
            $('#tmNavbar').collapse('hide');
        });

        var target = $("#tm-section-2").offset().top - topOffset;

        if ($(window).scrollTop() >= target) {
            $(".tm-navbar-container").addClass("bg-inverse");
        }
        else {
            $(".tm-navbar-container").removeClass("bg-inverse");
        }

        $(window).scroll(function () {

            if ($(this).scrollTop() >= target) {
                $(".tm-navbar-container").addClass("bg-inverse");
            }
            else {
                $(".tm-navbar-container").removeClass("bg-inverse");
            }
        });
        $('a[href*="#"]:not([href="#"])').click(function () {
            if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '')
                && location.hostname == this.hostname) {

                var target = $(this.hash);
                target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');

                if (target.length) {

                    $('html, body').animate({
                        scrollTop: target.offset().top - topOffset
                    }, 1000);
                    return false;
                }
            }
        });

        $('.tm-img-grid').magnificPopup({
            delegate: 'a', // child items selector, by clicking on it popup will open
            type: 'image',
            gallery: {enabled: true}
        });
    });

</script>
<script type="text/javascript">
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
    function checkData() {
        var keyword = document.getElementById("keyword").value.trim();
        if (keyword == null || keyword == "") {
            return false;
        } else {
            return true;
        }
    }
</script>
</body>
</html>