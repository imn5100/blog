<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/list.css">
<div class="col-md-9">
    <div class="panel panel panel-default">
        <div class="panel-heading">
            <span class="glyphicon glyphicon-book"></span> 博客列表
        </div>
        <div class="panel-body">
            <div class="bs-docs-section">
                <c:forEach var="blog" items="${blogList}">
                    <div class="bs-callout bs-callout-info" id="callout-helper-bg-specificity">
                        <h4><a class="title"
                               href="${pageContext.request.contextPath}/blog/articles/${blog.id}.html">${blog.title }</a>
                        </h4>
                        &nbsp;&nbsp;
                        <small style="color: grey">${blog.summary }...</small>
                        <br>
                        <br>
                        <c:forEach var="image" items="${blog.imagesList}">
                            <img src="${image}" class="img-responsive" style="max-width: 50%"/>
                            &nbsp;&nbsp;
                        </c:forEach>
                    <span class="info">
                        <span class="glyphicon glyphicon-calendar"></span>${blog.releaseDateStr}&nbsp;
                        <span class="glyphicon glyphicon-tags"></span>&nbsp;
                        <c:forEach items="${blog.keywordList}" var="keyword">
                            <a href="${pageContext.request.contextPath}/blog/q.html?q=${keyword}" style="color: #9d9d9d;">${keyword}</a>
                        </c:forEach>
                        <span class="glyphicon glyphicon-list-alt"></span>
                        <a href="${pageContext.request.contextPath}/index.html?typeId=${blog.blogType.id}" style="color: #9d9d9d;">${blog.blogType.typeName}</a>
                    </span>
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
</div>