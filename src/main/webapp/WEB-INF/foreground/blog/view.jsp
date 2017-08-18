<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<meta name="keywords" content="${blog.keyWord}"/>
<script type="text/javascript" src="/static/ueditor/ueditor.parse.js"></script>
<script type="text/javascript">
    uParse('.blog_content',
        {
            'highlightJsUrl': '/static/ueditor/third-party/SyntaxHighlighter/shCore.js',
            'highlightCssUrl': '/static/ueditor/third-party/SyntaxHighlighter/shCoreDefault.css'
        })
    $(document).ready(function () {
        $("#oauthLogin").attr("href", "/user/fromGithub.html?redirect=" + window.location);
    });
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

    .img-responsive {
        display: block;
        max-width: 100%;
        height: auto
    }
</style>
<div style="padding-top: 30px;">
    <div class="card-heading">
    </div>
    <div class="card-body">
        <h3 align="center"><strong>${blog.title }</strong></h3>
        <div>
        </div>
        <div class="blog_info">
            <ul>
                <span class="icon-calendar"></span>&nbsp;
                <fmt:formatDate value="${blog.releaseDate}" type="date" pattern="yyyy-MM-dd HH:mm"/>
                <span class="glyphicon glyphicon-hand-up"></span>
                (${blog.clickHit})
                <span class="fa fa-tags"></span>&nbsp;
                <c:forEach items="${blog.keywordList}" var="keyword">
                    <a href="/blog/search.html?keyword=${keyword}"
                       target="_blank">${keyword}</a>&nbsp;&nbsp;
                </c:forEach>
                <span class="fa fa-list-alt"></span>&nbsp;
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
<div>
    <c:choose>
        <c:when test="${OAUTH_USER!=null}">
            <hr>
            <div class="col-md-2">
                <img class="img-responsive"
                     src="${OAUTH_USER.avatarUrl}"/>
                <p align="center">
                    <a href="https://github.com/${OAUTH_USER.account}/" target="_blank">
                       ${OAUTH_USER.account}(${OAUTH_USER.name})</a>
                </p>
            </div>
            <div class="col-md-10">
                <div class="form-group">
                    <textarea class="form-control" rows="5"> </textarea>
                </div>
                <div align="right">
                    <button> 评论</button>
                </div>
            </div>
            <hr>
        </c:when>
        <c:otherwise>
            <a href="/user/fromGithub.html?" id="oauthLogin">Login From Github</a>
        </c:otherwise>
    </c:choose>
</div>