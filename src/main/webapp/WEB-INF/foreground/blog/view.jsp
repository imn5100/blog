<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<meta name="keywords" content="${blog.keyWord}"/>
<script type="text/javascript" src="/static/ueditor/ueditor.parse.js"></script>
<script type="text/javascript">
    uParse('.blog_content',
        {
            'highlightJsUrl': '/static/ueditor/third-party/SyntaxHighlighter/shCore.js',
            'highlightCssUrl': '/static/ueditor/third-party/SyntaxHighlighter/shCoreDefault.css'
        })
    $(document).ready(function () {
        var blogId = $("#blogId").val();


        $.get("/blog/discussList.do", {'blogId': blogId}, function (result) {
            if (result.success && result.data) {
                var html = "";
                result.data.forEach(function (element, index) {
                    html += '     <div class="row">   <div class="col-sm-2">' +
                        '                <img class="img-responsive"' +
                        '                     src="' + element.avatarUrl + '"/>' +
                        '                <p align="center">\n' +
                        '                    <a href="' + element.homePage + '" target="_blank">' +
                        '                           ' + element.account + '(' + element.name + ')</a>' +
                        '                </p>' +
                        '            </div>' +
                        '            <div class="col-sm-10" >' +
                        '                <div class="form-group">' +
                        '                    <textarea class="form-control" rows="5" id="discussContent" readonly=true>' + element.content + '</textarea>' +
                        '                </div>' +
                        '                <p  style="text-align: right;color: #666;font-size: medium;">' +
                        '                  ' + element.discussTime +
                        '                </p>' +
                        '            </div> </div>'
                });
                $("#discussPanel").html(html);
            }
        }, "json")

        $("#oauthLogin").attr("href", "/user/fromGithub.html?redirect=" + window.location);

        $("#submitDiscuss").click(function () {
            var content = $("#discussContent").val();
            if (blogId === null || blogId == "") {
                alert("评论失败")
                return
            }
            if (content === null || "" == content.trim()) {
                alert("请填写评论内容")
                return
            }
            $.post("/blog/submitDiscuss.do", {
                'blogId': blogId,
                'content': content,
            }, function (result) {
                if (result.success) {
                    addDiscuss(result.data);
                    $("#discussContent").val("");
                    alert("评论成功")
                } else {
                    alert("评论失败！" + result.msg);
                }
            }, "json");
        });

        function addDiscuss(element) {
            var newNode = document.createElement("div");
            newNode.setAttribute("class", "row");
            newNode.innerHTML = ' <div class="col-sm-2">' +
                '                <img class="img-responsive"' +
                '                     src="' + element.avatarUrl + '"/>' +
                '                <p align="center">\n' +
                '                    <a href="' + element.homePage + '" target="_blank">' +
                '                           ' + element.account + '(' + element.name + ')</a>' +
                '                </p>' +
                '            </div>' +
                '            <div class="col-sm-10" >' +
                '                <div class="form-group">' +
                '                    <textarea class="form-control" rows="5" id="discussContent" readonly=true>' + element.content + '</textarea>' +
                '                </div>' +
                '                <p  style="text-align: right;color: #666;font-size: medium;">' +
                '                  ' + element.discussTime +
                '                </p>' +
                '            </div> ';
            $("#discussPanel").prepend($(newNode))
        }

    });
</script>
<style>
    .blog_info {
        text-align: center;
        padding: 10px 0;
        color: #666;
        font-size: medium;
    }

    .blog_info li {
        text-align: -webkit-match-parent;
    }

    .blog_content {
        margin-top: 20px;
        padding-bottom: 30px;
    }

    .blog_lastAndNextPage {
        padding: 10px;
    }

    hr {
        border: 0;
        border-top: 1px solid #eee;
        margin: 20px 0;
        display: block;
        -webkit-margin-before: 0.5em;
        -webkit-margin-after: 0.5em;
        -webkit-margin-start: auto;
        -webkit-margin-end: auto;
    }

    .img-responsive {
        display: block;
        max-width: 100%;
        height: auto
    }
</style>
<div style="padding-top: 30px;">
    <div class="card-heading">
    </div>
    <div class="card-body">
        <h3 align="center"><strong>${blog.title }</strong></h3>
        <input type="hidden" id="blogId" value="${blog.encodeId}">
        <div>
        </div>
        <div class="blog_info">
            <ul>
                <span class="icon-calendar"></span>&nbsp;
                <fmt:formatDate value="${blog.releaseDate}" type="date" pattern="yyyy-MM-dd HH:mm"/>
                <span class="glyphicon glyphicon-hand-up"></span>
                (${blog.clickHit})
                <span class="fa fa-tags"></span>&nbsp;
                <c:forEach items="${blog.keywordList}" var="keyword">
                    <a href="/blog/search.html?keyword=${keyword}"
                       target="_blank">${keyword}</a>&nbsp;&nbsp;
                </c:forEach>
                <span class="fa fa-list-alt"></span>&nbsp;
                <a href="/index.html?typeId=${blog.blogType.id}"
                   target="_blank">${blog.blogType.typeName}</a>
            </ul>
        </div>
        <div class="blog_content">
            ${blog.content}
        </div>
        <hr>
        <div class="blog_lastAndNextPage">
            <c:choose>
                <c:when test="${lastBlog!=null && lastBlog.id!=null}">
                    <p>上一篇：<a href="/blog/${lastBlog.encodeId}.html">${lastBlog.title}</a></p>
                </c:when>
                <c:otherwise>
                    <p>上一篇：没有了 XD</p>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${nextBlog!=null && nextBlog.id!=null}">
                    <p>下一篇：<a href="/blog/${nextBlog.encodeId}.html">${nextBlog.title}</a></p>
                </c:when>
                <c:otherwise>
                    <p>下一篇：没有了 XD</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<hr>
<c:choose>
    <c:when test="${OAUTH_USER!=null}">
        <div class="row">
            <div class="col-md-2">
                <img class="img-responsive"
                     src="${OAUTH_USER.avatarUrl}"/>
                <p align="center">
                    <a href="https://github.com/${OAUTH_USER.account}/" target="_blank">
                            ${OAUTH_USER.account}(${OAUTH_USER.name})</a>
                </p>
            </div>
            <div class="col-md-10">
                <div class="form-group">
                    <textarea class="form-control" rows="5" id="discussContent"> </textarea>
                </div>
                <div align="right">
                    <button id="submitDiscuss"> 评论</button>
                </div>
            </div>
        </div>
        <hr>
    </c:when>
    <c:otherwise>
        <div class="row" align="center">
            <a href="/user/fromGithub.html?" id="oauthLogin">通过Github登录评论</a>
            <hr>
            <br>
            <br>
        </div>
    </c:otherwise>
</c:choose>
<div id="discussPanel" class="col-md-12">

</div>