<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Blogger Login</title>
    <script src="${pageContext.request.contextPath}/static/bootstrap3/js/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/static/bootstrap3/js/jquery.md5.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/inner.css">
    <SCRIPT type="text/javascript">
        $(function () {
            //得到焦点
            $("#password").focus(function () {
                $("#left_hand").animate({
                    left: "150",
                    top: " -38"
                }, {
                    step: function () {
                        if (parseInt($("#left_hand").css("left")) > 140) {
                            $("#left_hand").attr("class", "left_hand");
                        }
                    }
                }, 2000);
                $("#right_hand").animate({
                    right: "-64",
                    top: "-38px"
                }, {
                    step: function () {
                        if (parseInt($("#right_hand").css("right")) > -70) {
                            $("#right_hand").attr("class", "right_hand");
                        }
                    }
                }, 2000);
            });
            //失去焦点
            $("#password").blur(function () {
                $("#left_hand").attr("class", "initial_left_hand");
                $("#left_hand").attr("style", "left:100px;top:-12px;");
                $("#right_hand").attr("class", "initial_right_hand");
                $("#right_hand").attr("style", "right:-112px;top:-12px");
            });
            $("#login_form").submit(function () {
                var userName = $("#userName").val();
                var password = $("#password").val();
                var vcode = $("#vcode").val();
                $.ajax({
                    type: "POST",
                    async: false,
                    url: "${pageContext.request.contextPath}/blogger/login.do",
                    data: "username=" + userName + "&password=" + $.md5(password) + "&vcode=" + vcode,
                    success: function (data) {
                        $("#error").text("");
                        if (data == "200") {
                            location = "/admin/main.jsp"
                        } else if (data == "601") {
                            $("#password").val("");
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
            document.getElementById("randImage").src = "${pageContext.request.contextPath}/blog/codesImg.do?" + Math.random();
        }
    </SCRIPT>
</head>
<body>
<DIV class="top_div">
</DIV>
<form action="/login.html" method="post" id="login_form">
    <DIV style="background: rgb(255, 255, 255); margin: -100px auto auto; border: 1px solid rgb(231, 231, 231); border-image: none; width: 400px; height: 250px; text-align: center;">
        <DIV style="width: 165px; height: 96px; position: absolute;">
            <DIV class="tou">
            </DIV>
            <DIV class="initial_left_hand" id="left_hand">
            </DIV>
            <DIV class="initial_right_hand" id="right_hand">
            </DIV>
        </DIV>
        <div>
            <P style="padding: 30px 0px 10px; position: relative;">
                <SPAN class="u_logo"></SPAN>
                <input id="userName" name="userName" class="ipt" type="text" placeholder="请输入用户名" maxlength="20"
                       required/>
            </P>

            <P style="position: relative;">
                <SPAN class="p_logo"></SPAN>
                <input id="password" name="password" class="ipt" type="password" placeholder="请输入密码" maxlength="20"
                       value="" required/>
            </P>

            <P style="position: relative;">
                <SPAN class="v_logo"></SPAN>
                <input id="vcode" name="vcode" class="ipt2" type="text" placeholder="验证码" maxlength="4" value=""
                       required/>
                <img onclick="javascript:loadimage();" title="换一张试试" name="randImage" id="randImage"  align="absmiddle" class="vcode" src="${pageContext.request.contextPath}/blog/codesImg.do">
            </P>
        </div>
        <DIV style="height: 50px; line-height: 50px; margin-top: 30px; border-top-color: rgb(231, 231, 231); border-top-width: 1px; border-top-style: solid;">
            <P style="margin: 0px 35px 20px 45px;">
                <SPAN style="float: left;">最後のハードル</SPAN>
                <span><font color="red" id="error"></font></span>
	        <SPAN style="float: right;">
	              <input type="submit"
                         style="background: rgb(0, 142, 173); padding: 8px 12px; border-radius: 4px; border: 1px solid rgb(26, 117, 152); border-image: none; color: rgb(255, 255, 255); font-weight: bold;"
                         value="登录"/>
	         </SPAN>
            </P>
        </DIV>
    </DIV>
</form>
<jsp:include page="/WEB-INF/foreground/common/foot.jsp"/>
</body>
</html>