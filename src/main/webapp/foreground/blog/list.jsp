<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style>
    .liclass {
        margin: 5px;
        position: relative;
        background: #fff;
        -webkit-transition: all 0.2s ease-in
    }

</style>
<div class="data_list">
    <div class="data_list_title">
        <img src="${pageContext.request.contextPath}/static/images/list_icon.png"/>
        最新博客
    </div>
    <div class="datas">
        <ul>
            <c:forEach var="blog" items="${blogList}">
                <li class="liclass">
                    <span class="title"> <strong><a
                            href="${pageContext.request.contextPath}/blog/articles/${blog.id}.html">${blog.title }</a></strong></span>
                    <span class="summary">${blog.summary }...</span>
                    <span class="summary"><a href="${pageContext.request.contextPath}/blog/articles/${blog.id}.html"
                                             name="url">http://shawblog.me/blog/articles/${blog.id}.html</a></span>
				  	<span class="img">
				  		<c:forEach var="image" items="${blog.imagesList }">
                            <a href="${pageContext.request.contextPath}/blog/articles/${blog.id}.html">${image }</a>
                            &nbsp;&nbsp;
                        </c:forEach>
				  	</span>
                    <span class="info">${blog.releaseDateStr}&nbsp;阅读(${blog.clickHit}) &nbsp;
                        <span class="ds-thread-count" data-thread-key="${blog.id}" data-count-type="comments"></span>
                </li>
                <br>
                <hr style="height:0px;border:none;border-top:0px dashed gray;padding-bottom:0px;"/>
            </c:forEach>
        </ul>
    </div>
</div>

<div>
    <nav align="center">
        <ul class="pagination pagination-sm">
            ${pageCode }
        </ul>
    </nav>
</div>