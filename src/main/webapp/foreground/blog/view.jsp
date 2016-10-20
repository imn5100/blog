<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%--<script type="text/javascript"--%>
        <%--src="${pageContext.request.contextPath}/static/ueditor/third-party/SyntaxHighlighter/shCore.js"></script>--%>
<%--<link rel="stylesheet"--%>
      <%--href="${pageContext.request.contextPath}/static/ueditor/third-party/SyntaxHighlighter/shCoreDefault.css">--%>
<%--<script type="text/javascript">--%>
    <%--SyntaxHighlighter.all();--%>
<%--</script>--%>
<div class="data_list">
    <div class="data_list_title">
        博客信息
    </div>
    <div>
        <div class="blog_title"><h3><strong>${blog.title }</strong></h3></div>
        <div style="padding-left: 330px;padding-bottom: 20px;padding-top: 10px">
        </div>
        <div class="blog_info">
            发布时间：『 <fmt:formatDate value="${blog.releaseDate }" type="date" pattern="yyyy-MM-dd HH:mm"/>』&nbsp;&nbsp;博客类别：${blog.blogType.typeName}&nbsp;&nbsp;浏览(${blog.clickHit})
        </div>
        <div class="blog_content">
            ${blog.content }
        </div>
        <div class="blog_keyWord">
            关键字：
            <c:choose>
                <c:when test="${keyWords==null}">
                    &nbsp;&nbsp;无
                </c:when>
                <c:otherwise>
                    <c:forEach var="keyWord" items="${keyWords }">
                        &nbsp;&nbsp;<a href="${pageContext.request.contextPath}/blog/q.html?q=${keyWord}" target="_blank">${keyWord }</a>&nbsp;&nbsp;
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="blog_lastAndNextPage">
            ${pageCode }
        </div>
    </div>
</div>
<div class="ds-thread" data-thread-key="${blog.id}" data-title="${blog.title}" data-url="/blog/articles/${blog.id}.html"></div>

