<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
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
<style>
    strong {
        color: #333;
    }
</style>
<div class="row">
    <div class="col-md-12">
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                            data-target="#bs-example-navbar-collapse-1">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="${pageContext.request.contextPath}/index.html"><strong>首页</strong></a>
                </div>

                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav">
                        <li><a href="${pageContext.request.contextPath}/about.html"><strong>关于本站</strong></a></li>
                        <li><a href="${pageContext.request.contextPath}/blogger/aboutBlogger.html"><strong>博主介绍</strong></a>
                        </li>
                    </ul>
                    <form action="${pageContext.request.contextPath}/blog/q.html" class="navbar-form navbar-right"
                          role="search" method="post" onsubmit="return checkData()">
                        <div class="form-group">
                            <input type="text" id="q" name="q" value="${q }" class="form-control"
                                   placeholder="Search...">
                        </div>
                        <button type="submit" class="btn btn-default">Go!</button>
                    </form>
                </div><!-- /.navbar-collapse -->
            </div><!-- /.container-fluid -->
        </nav>
    </div>
</div>