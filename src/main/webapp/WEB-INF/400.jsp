<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Blogger Login</title>
    <script src="${pageContext.request.contextPath}/static/bootstrap3/js/jquery-1.11.2.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/bootstrap3/js/jquery.md5.js"></script>
    <STYLE>
        body {
            background: #ebebeb;
            font-family: "Helvetica Neue", "Hiragino Sans GB", "Microsoft YaHei", "\9ED1\4F53", Arial, sans-serif;
            color: #222;
            font-size: 12px;
        }

        * {
            padding: 0px;
            margin: 0px;
        }

        .top_div {
            background: #0eaaad;
            width: 100%;
            height: 400px;
        }

        a {
            text-decoration: none;
        }

        .tou {
            background: url("${pageContext.request.contextPath}/static/images/tou.png") no-repeat;
            width: 97px;
            height: 92px;
            position: absolute;
            top: -87px;
            left: 140px;
        }

        .initial_left_hand {
            background: url("${pageContext.request.contextPath}/static/images/hand.png") no-repeat;
            width: 30px;
            height: 20px;
            position: absolute;
            top: -12px;
            left: 100px;
        }

        .initial_right_hand {
            background: url("${pageContext.request.contextPath}/static/images/hand.png") no-repeat;
            width: 30px;
            height: 20px;
            position: absolute;
            top: -12px;
            right: -112px;
        }

        .panel {
            background: url("${pageContext.request.contextPath}/static/images/panel.png");
            margin: -100px auto auto;
            border-image: none;
            width: 400px;
            height: 250px;
            text-align: center;
        }

    </STYLE>
</head>
<body>
<DIV class="top_div">
</DIV>
<DIV class="panel">
    <DIV style="width: 165px; height: 96px; position: absolute;">
        <DIV class="tou">
        </DIV>
        <DIV class="initial_left_hand" id="left_hand">
        </DIV>
        <DIV class="initial_right_hand" id="right_hand">
        </DIV>
    </DIV>
    <div align="center" style="padding-top: 40px"><font color="white" size="10"><strong>好像少了些什么？</strong></font></div>
</DIV>
<jsp:include page="${pageContext.request.contextPath}/foreground/common/foot.jsp"/>
</body>
</html>