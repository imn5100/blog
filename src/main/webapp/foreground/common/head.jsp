<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript">
    function checkData() {
        var q = document.getElementById("q").value.trim();
        if (q == null || q == "") {
            alert("请输入您要查询的关键字！");
            return false;
        } else {
            return true;
        }
    }
</script>
<div class="row">
    <div class="col-md-4">
    </div>
    <nav class="navbar navbar-default navbar-fixed-top">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                        aria-expanded="false" aria-controls="navbar">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="${pageContext.request.contextPath}/index.html">Secret Base</a>
            </div>
            <div id="navbar" class="navbar-collapse collapse">
                <ul class="nav navbar-nav">
                    <li <c:if test="${indexActive !=null}"> class="active" </c:if> ><a
                            href="${pageContext.request.contextPath}/index.html">首页</a></li>
                    <li <c:if test="${aboutActive!=null}"> class="active" </c:if> ><a
                            href="${pageContext.request.contextPath}/about.html">关于本站</a></li>
                    <li <c:if test="${aboutBloggerActive !=null}"> class="active" </c:if> ><a
                            href="${pageContext.request.contextPath}/blogger/aboutBlogger.html">博主介绍</a></li>
                </ul>
                <form action="${pageContext.request.contextPath}/blog/q.html" class="navbar-form navbar-right"
                      role="search" method="post" onsubmit="return checkData()">
                    <div class="form-group">
                        <input type="text" id="q" name="q" value="${q }" class="form-control"
                               placeholder="Search...">
                    </div>
                    <button type="submit" class="btn btn-default">Go!</button>
                </form>
            </div><!--/.nav-collapse -->
        </div>
    </nav>
</div>