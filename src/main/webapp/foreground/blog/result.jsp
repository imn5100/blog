<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/list.css">
<div class="panel panel panel-primary">
    <div class="panel-heading">
        搜索&nbsp;${q} &nbsp;的结果 &nbsp;(总共搜索到&nbsp;${resultTotal}&nbsp;条记录)
    </div>
    <div class="panel-body">
        <div class="bs-docs-section">

            <c:choose>
                <c:when test="${blogList.size()==0}">
                    <div class="bs-callout bs-callout-info" id="callout-helper-bg-specificity">
                        <div align="center" style="padding-top: 20px">未查询到结果，请换个关键字试试看！</div>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="blog" items="${blogList}">
                        <div class="bs-callout bs-callout-info">
                            <h4><a class="title" href="${pageContext.request.contextPath}/blog/articles/${blog.id}.html"
                                   target="_blank">${blog.title }</a></h4>

                            <p> ${blog.content}...</p>
                            <br>
                            <a href="${pageContext.request.contextPath}/blog/articles/${blog.id}.html">http://shawblog.me/blog/articles/${blog.id}.html</a>
                            <span class="info">&nbsp;&nbsp;&nbsp;&nbsp;发布日期：${blog.releaseDateStr }</span>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    ${pageCode }
</div>