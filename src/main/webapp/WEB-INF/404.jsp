<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Blogger Login</title>
    <script src="${pageContext.request.contextPath}/static/bootstrap3/js/jquery-1.11.2.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/bootstrap3/js/jquery.md5.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/inner.css">
</head>
<body>
<DIV class="top_div">
</DIV>
<DIV style=" margin: -100px auto auto; border-image: none; width: 400px; height: 200px; text-align: center;">
    <DIV style="width: 165px; height: 96px; position: absolute;">
        <DIV class="tou">
        </DIV>
        <DIV class="initial_left_hand" id="left_hand">
        </DIV>
        <DIV class="initial_right_hand" id="right_hand">
        </DIV>
    </DIV>
    <div align="center"><img alt="logo" src="${pageContext.request.contextPath}/static/images/404.png"></div>
</DIV>
<jsp:include page="${pageContext.request.contextPath}/foreground/common/foot.jsp"/>
</body>
</html>