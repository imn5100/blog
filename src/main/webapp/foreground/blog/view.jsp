<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/static/ueditor/third-party/SyntaxHighlighter/shCore.js"></script>
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/static/ueditor/third-party/SyntaxHighlighter/shCoreDefault.css">
<script type="text/javascript">
    SyntaxHighlighter.all();
</script>
<style>
    .blog_info {
        text-align: center;
        padding: 10px 0;
        color: #666;
    }

    .blog_info li {
        text-align: -webkit-match-parent;
    }

    .blog_content {
        margin-top: 20px;
        padding-bottom: 30px;
    }

    .blog_keyWord {
        margin-top: 20px;
        padding-bottom: 30px;
        padding-left: 15px;
    }

    .blog_keyWord a {
        color: deepskyblue;
    }

    .blog_lastAndNextPage {
        border-top: 1px dotted black;
        padding: 10px;
    }
</style>
<div class="col-md-12">
    <div class="panel panel panel-primary">
        <div class="panel-heading">
            博客信息
        </div>
        <div class="panel-body">
            <h3 align="center"><strong>${blog.title }</strong></h3>

            <div style="padding-left: 330px;padding-bottom: 20px;padding-top: 10px">
            </div>
            <div class="blog_info">
                <ul>
                    发布时间：<fmt:formatDate value="${blog.releaseDate }" type="date" pattern="yyyy-MM-dd HH:mm"/>
                    博客类别：${blog.blogType.typeName}
                    浏览(${blog.clickHit})
                    评论:<span class="ds-thread-count" data-thread-key="${blog.id}"></span>
                </ul>
            </div>
            <div class="blog_content">
                ${blog.content}
            </div>
            <div class="blog_keyWord">
                关键字：
                <c:choose>
                    <c:when test="${keyWords==null}">
                        &nbsp;&nbsp;无
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="keyWord" items="${keyWords }">
                            &nbsp;&nbsp;<a href="${pageContext.request.contextPath}/blog/q.html?q=${keyWord}" target="_blank">${keyWord}</a>&nbsp;&nbsp;
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="blog_lastAndNextPage">
                ${pageCode }
            </div>
        </div>
    </div>
    <div class="ds-thread" data-thread-key="${blog.id}" data-title="${blog.title}"
         data-url="${rootSite}/blog/articles/${blog.id}.html"></div>

    <div class="ds-share" data-thread-key="${blog.id}" data-title="${blog.title}"
         data-images="${rootSite}/static/userImages/${blogger.imageName}" data-content=""
         data-url="${rootSite}/blog/articles/${blog.id}.html">
        <div class="ds-share-aside-right">
            <div class="ds-share-aside-inner">
            </div>
            <div class="ds-share-aside-toggle">分享到</div>
        </div>
    </div>
</div>
