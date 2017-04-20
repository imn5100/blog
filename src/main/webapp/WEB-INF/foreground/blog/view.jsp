<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<meta name="keywords" content="${blog.keyWord}"/>
<link rel="stylesheet" href="/static/css/embed.default.css">
<script type="text/javascript" src="/static/ueditor/ueditor.parse.js"></script>
<script type="text/javascript">
    uParse('.blog_content',
        {
            'highlightJsUrl': '/static/ueditor/third-party/SyntaxHighlighter/shCore.js',
            'highlightCssUrl': '/static/ueditor/third-party/SyntaxHighlighter/shCoreDefault.css'
        })
</script>
<style>
    .blog_info {
        text-align: center;
        padding: 10px 0;
        color: #666;
        font-size: medium;
    }

    .blog_info li {
        text-align: -webkit-match-parent;
    }

    .blog_content {
        margin-top: 20px;
        padding-bottom: 30px;
    }

    .blog_lastAndNextPage {
        padding: 10px;
    }

    hr {
        border: 0;
        border-top: 1px solid #eee;
        margin: 20px 0;
        display: block;
        -webkit-margin-before: 0.5em;
        -webkit-margin-after: 0.5em;
        -webkit-margin-start: auto;
        -webkit-margin-end: auto;
    }
</style>
<div class="panel panel panel-default">
    <div class="panel-heading">
        博客信息
    </div>
    <div class="panel-body">
        <h3 align="center"><strong>${blog.title }</strong></h3>

        <div>
        </div>
        <div class="blog_info">
            <ul>
                <span class="glyphicon glyphicon-calendar"></span>&nbsp;
                <fmt:formatDate value="${blog.releaseDate}" type="date" pattern="yyyy-MM-dd HH:mm"/>
                <span class="glyphicon glyphicon-hand-up"></span>
                (${blog.clickHit})
                <span class="glyphicon glyphicon-tags"></span>&nbsp;
                <c:forEach items="${blog.keywordList}" var="keyword">
                    <a href="/blog/search.html?keyword=${keyword}"
                       target="_blank">${keyword}</a>&nbsp;&nbsp;
                </c:forEach>
                <span class="glyphicon glyphicon-list-alt"></span>&nbsp;
                <a href="/index.html?typeId=${blog.blogType.id}"
                   target="_blank">${blog.blogType.typeName}</a>
            </ul>
        </div>
        <div class="blog_content">
            ${blog.content}
        </div>
        <hr>
        <div class="blog_lastAndNextPage">
            <c:choose>
                <c:when test="${lastBlog!=null && lastBlog.id!=null}">
                    <p>上一篇：<a href="/blog/${lastBlog.encodeId}.html">${lastBlog.title}</a></p>
                </c:when>
                <c:otherwise>
                    <p>上一篇：没有了 XD</p>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${nextBlog!=null && nextBlog.id!=null}">
                    <p>下一篇：<a href="/blog/${nextBlog.encodeId}.html">${nextBlog.title}</a></p>
                </c:when>
                <c:otherwise>
                    <p>下一篇：没有了 XD</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<div id="cloud-tie-wrapper" class="cloud-tie-wrapper"></div>
<script>
    var cloudTieConfig = {
        url: document.location.href,
        sourceId: "",
        productKey: "5639cd71ae684a908044fc1325d4dcd3",
        target: "cloud-tie-wrapper"
    };
</script>
<script src="https://img1.cache.netease.com/f2e/tie/yun/sdk/loader.js"></script>
