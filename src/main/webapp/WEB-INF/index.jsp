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
    <link href="/favicon.ico" rel="SHORTCUT ICON">
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/css/font.css">
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

    <c:if test="${blogger.backgroundImage !=null && blogger.backgroundImage !=''}">
    .tm-intro {
        background: url(${blogger.backgroundImage}) no-repeat;
        background-attachment: fixed;
        background-position: left top;
        background-size: 100% 750px;
        color: white;
        display: -webkit-flex;
        display: -ms-flexbox;
        display: flex;
        -webkit-flex-direction: column;
        -ms-flex-direction: column;
        flex-direction: column;
        -webkit-align-items: center;
        -ms-flex-align: center;
        align-items: center;
        -webkit-justify-content: center;
        -ms-flex-pack: center;
        justify-content: center;
        height: 750px;
    }

    </c:if>
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
                            <a class="nav-link" href="/">首页</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#tm-section">博客</a>
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
                            <div class="dropdown-menu bg-inverse" aria-labelledby="navbarDropdownMenuLink">
                                <a class="dropdown-item bg-inverse" target="_blank" href="/rainyRoom.html">Rainy
                                    Mood</a>
                                <a class="dropdown-item bg-inverse" target="_blank"
                                   href="/remoteTask/main.html">远程任务执行器</a>
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
                    <h1 class="tm-site-name">El Psy Congroo</h1>
                    <a href="#tm-section" class="tm-intro-link">Begin</a>
                    <p class="tm-intro-text">
                    <form class="form-inline my-2 my-lg-0" action="/blog/search.html" onsubmit="return checkData()">
                        <input class="form-control mr-sm-2" id="keyword" name="keyword" type="text"
                               placeholder="Search">
                    </form>
                    </p>

                </div>
            </section>
        </div>
        <div style="padding-top: 60px" id="tm-section-2"></div>
    </div>
</div>
<div class="col-md-9" id="contents">
    <div class="row gray-bg">
        <div class="tm-section" id="tm-section">
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
                            <h2 class="tm-news-title dark-gray-text"><a
                                    href="/blog/${blog.encodeId}.html">★${blog.title}</a>
                            </h2>
                            <span class="info">
                                        <span class="fa fa-calendar"></span>${blog.releaseDateStr}&nbsp;
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
    <div>
        <nav align="center">
            <ul class="pagination jumpHook" id="pagination">
                ${pageCode }
            </ul>
        </nav>
    </div>
</div>
<div class="col-md-3" id="side" style="padding-top: 30px;">
    <div class="card card-default">
        <div class="card-heading">
            <span class="fa fa-leaf"></span>
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
<jsp:include page="foreground/common/foot.jsp"></jsp:include>
<script src="/static/js/jquery.magnific-popup.min.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        var aspectRatio = 1.5;
        <c:if test="${blogger.aspectRatio!=null&&blogger.aspectRatio>0}">
        aspectRatio = ${blogger.aspectRatio};
        </c:if>
        var mobileTopOffset = 54;
        var desktopTopOffset = 80;
        var topOffset = desktopTopOffset;
        if (aspectRatio != null) {
            var height = $(window).width() / aspectRatio;
            if (!(height > 750)) {
                height = 750
            }
            $(".tm-intro").attr("style", "background-size: 100% " + height + "px;");
        }

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
            if (aspectRatio != null) {
                var height = $(window).width() / aspectRatio;
                if (!(height > 750)) {
                    height = 750
                }
                $(".tm-intro").attr("style", "background-size: 100% " + height + "px;");
            }
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
    $(function () {
        $("#close_side").click(function () {
            $("#contents").attr("class", "col-md-12")
            $("#side").hide();
            $("#up").show();
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