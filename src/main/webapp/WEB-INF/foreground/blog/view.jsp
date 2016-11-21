<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<meta name="keywords" content="${blog.keyWord}"/>
<script type="text/javascript"
        src="/static/ueditor/third-party/SyntaxHighlighter/shCore.js"></script>
<link rel="stylesheet"
      href="/static/ueditor/third-party/SyntaxHighlighter/shCoreDefault.css">
<script type="text/javascript">
    SyntaxHighlighter.all();
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
            ${pageCode }
        </div>
    </div>
</div>
<div class="ds-thread" data-thread-key="${blog.id}" data-title="${blog.title}"
     data-url="${rootSite}/blog/${blog.id}.html"></div>
<div class="ds-share" data-thread-key="${blog.id}" data-title="${blog.title}"
     data-images="${blogger.imageName}" data-content=""
     data-url="${rootSite}/blog/${blog.id}.html">
    <div class="ds-share-aside-right">
        <div class="ds-share-aside-inner">
        </div>
        <div class="ds-share-aside-toggle">分享到</div>
    </div>
</div>
