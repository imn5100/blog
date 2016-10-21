<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/list.css">
<div class="panel panel panel-primary">
    <div class="panel-heading">
        博客列表
    </div>
    <div class="panel-body">
        <div class="bs-docs-section">
            <c:forEach var="blog" items="${blogList}">
                <div class="bs-callout bs-callout-info" id="callout-helper-bg-specificity">
                    <h4><a class="title"
                           href="${pageContext.request.contextPath}/blog/articles/${blog.id}.html">${blog.title }</a>
                    </h4>
                    &nbsp;&nbsp;<code>${blog.summary }...</code>
                    <br>
                    <br>
                    <div class="listimg">
                        <c:forEach var="image" items="${blog.imagesList }">
                            <a href="${pageContext.request.contextPath}/blog/articles/${blog.id}.html">${image }</a>
                            &nbsp;&nbsp;
                        </c:forEach>
                    </div>
                    <span class="info">${blog.releaseDateStr}&nbsp;阅读(${blog.clickHit}) &nbsp;
                        <span class="ds-thread-count" data-thread-key="${blog.id}" data-count-type="comments"></span>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<div>
    <nav align="center">
        <ul class="pagination pagination-sm">
            ${pageCode }
        </ul>
    </nav>
</div>