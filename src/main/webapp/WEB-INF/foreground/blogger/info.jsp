<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<div class="panel panel panel-default">
    <div class="card-heading">
        Music
    </div>
    <div class="card-body">
        <div>
        <%--<div style="padding: 30px">--%>
            ${blogger.proFile }
        </div>
        <div id="music">

        </div>
    </div>
</div>
<script>
    $(function () {
        var musicList = '<iframe frameborder="no" height="500" marginheight="0" marginwidth="0" width="100%"  src="https://music.163.com/outchain/player?type=0&id=58441082&auto=0&height=500"></iframe>';
        $("#music").html(musicList);
    });
</script>