<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Login</title>
    <link href="/static/css/login_style.css" rel="stylesheet" type="text/css" media="all"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script src="/static/bootstrap3/js/jquery.js"></script>
    <script src="/static/bootstrap3/js/jquery.md5.js"></script>
    <SCRIPT type="text/javascript">
        $(function () {
            $("#login_form").submit(function () {
                var userName = $("#userName").val();
                var password = $("#password").val();
                var vcode = $("#vcode").val();
                $.ajax({
                    type: "POST",
                    async: false,
                    url: "/blogger/login.do",
                    data: "username=" + userName + "&password=" + $.md5(password) + "&vcode=" + vcode,
                    success: function (data) {
                        $("#error").text("");
                        if (data == "200") {
                            location = "/admin/main.jsp"
                        } else if (data == "601") {
                            $("#error").text("验证码错误！");
                        } else {
                            $("#error").text("用户名或密码错误");
                            loadimage();
                        }
                    }
                });
                return false;
            });

        });
        function loadimage() {
            document.getElementById("randImage").src = "/blogger/codesImg.do?" + Math.random();
        }
    </SCRIPT>
</head>

<body>
<div class="header-w3l">
    <h1>最後のハードル</h1>
</div>
<div class="main-content-agile">
    <div class="sub-main-w3">
        <form action="#" method="post" id="login_form">
            <input placeholder="Username" name="Name" class="user" type="text" required="" id="userName"><br>
            <input placeholder="Password" name="Password" class="pass" type="password" required="" id="password"><br>
            <input id="vcode" name="vcode" type="text" placeholder="Verification code" maxlength="4" value=""
                   required=""><br>
            <img onclick="javascript:loadimage();" name="randImage" id="randImage"
                 src="/blogger/codesImg.do"><br>
            <span><font color="#d3d3d3" id="error"></font></span><br>
            <input type="submit" value="">
        </form>

    </div>
</div>

<div class="footer">
    <p>Power by <a style="color: #777777;" href="https://github.com/imn5100"
                   target="_blank">imn5100</a>
    </p>
</div>
</body>
</html>