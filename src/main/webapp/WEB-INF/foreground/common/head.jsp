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
    <nav class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                        aria-expanded="false" aria-controls="navbar">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="/">ShawのSecret Base</a>
            </div>
            <div id="navbar" class="navbar-collapse collapse">
                <ul class="nav navbar-nav">
                    <li <c:if test="${indexActive !=null}"> class="active" </c:if> ><a
                            href="/">首页</a></li>
                    <li <c:if test="${aboutActive!=null}"> class="active" </c:if> ><a
                            href="/about.html">关于本站</a></li>
                    <li <c:if test="${aboutBloggerActive !=null}"> class="active" </c:if> ><a
                            href="/blogger/about.html">关于站长</a></li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">实验室<b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="/rainyRoom.html" target="_blank">Rainy Room</a></li>
                        </ul>
                    </li>
                </ul>
                <form action="/blog/q.html" class="navbar-form navbar-right"
                      role="search" method="post" onsubmit="return checkData()">
                    <div class="form-group">
                        <input type="text" id="q" name="q" value="${q}" class="form-control"
                               placeholder="Search...">
                    </div>
                    <button type="submit" class="btn btn-inverse">Go!</button>
                </form>
            </div>
        </div>
    </nav>
</div>