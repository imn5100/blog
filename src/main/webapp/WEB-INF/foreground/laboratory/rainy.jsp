<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ShawのRainy Room</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="/favicon.ico" rel="SHORTCUT ICON">
    <link rel="stylesheet" href="/static/bootstrap3/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/bootstrap3/css/bootstrap-theme.min.css">
    <script src="/static/bootstrap3/js/jquery-1.11.2.min.js"></script>
    <script src="/static/bootstrap3/js/bootstrap.min.js"></script>
    <script src="/static/bootstrap3/js/jquery.jplayer.min.js"></script>
</head>
<style>
    body {
        padding-top: 50px;
        padding-bottom: 20px;
        background-color: #5b5c5f;
        font-family: 'marcellus_scregular',serif;
        text-shadow: 0px 0px 15px rgba(0,0,0,0.15);
        font-size: 20px;
        color: #fff;
    }
    .container{
        text-align: center;
        display: table;
        vertical-align: middle;
        max-width: 98%;
    }
</style>
<script type="text/javascript">
    jQuery(document).ready(function () {
        jQuery("#jquery_jplayer_1").jPlayer({
            ready: function () {
                jQuery(this).jPlayer("setMedia", {
                    m4a: "/static/userImages/sound/0.m4a",
                    oga: "/static/userImages/sound/0.ogg"
                }).jPlayer("play");
                var click = document.ontouchstart === undefined ? 'click' : 'touchstart';
                var kickoff = function () {
                    jQuery("#jquery_jplayer_1").jPlayer("play");
                    document.documentElement.removeEventListener(click, kickoff, true);
                };
                document.documentElement.addEventListener(click, kickoff, true);
            },
            playing: function () {
                document.getElementById("spinner").style.display = "none";
                document.getElementById("volumeDiv").style.display = "block";
            }, swfPath: "/js", loop: true, supplied: "m4a, oga", preload: "none", volume: 1.0
        });
    });
    function getHost(url) {
        var host = "null";
        if (typeof url == "undefined"
                || null == url)
            url = window.location.href;
        var regex = /.*\:\/\/([^\/]*).*/;
        var match = url.match(regex);
        if (typeof match != "undefined"
                && null != match)
            host = match[1];
        return host;
    }
</script>
<body>
<div id="jquery_jplayer_1"></div>
<script type="text/javascript">
    function volumeDownOrPause() {
        var host = (document.location.protocol == 'https:' ? 'https://' : 'http://') + getHost();
        var oldVol = document.getElementById("volume").src;
        if (oldVol == (host + "/static/images/rainyroom/volumeWhite3.png")) {
            document.getElementById("volume").src = "/static/images/rainyroom/volumeWhite2.png";
            jQuery("#jquery_jplayer_1").jPlayer("volume", 0.60);
        }
        if (oldVol == host + "/static/images/rainyroom/volumeWhite2.png") {
            document.getElementById("volume").src = "/static/images/rainyroom/volumeWhite1.png";
            jQuery("#jquery_jplayer_1").jPlayer("volume", 0.30);
        }
        if (oldVol == host + "/static/images/rainyroom/volumeWhite1.png") {
            document.getElementById("volume").src = "/static/images/rainyroom/volumeWhite0.png";
            jQuery("#jquery_jplayer_1").jPlayer("pause");
        }
        if (oldVol == host + "/static/images/rainyroom/volumeWhite0.png") {
            document.getElementById("volume").src = "/static/images/rainyroom/volumeWhite3.png";
            jQuery("#jquery_jplayer_1").jPlayer("volume", 1.00);
            jQuery("#jquery_jplayer_1").jPlayer("play");
        }
    }
</script>
<br/>
<div class="container">
<div id="spinner"><img src="/static/images/rainyroom/spinner2.gif" width="32" height="26" alt=""/></div>
<div id="volumeDiv" style="display:none;">
    <a onclick="volumeDownOrPause();" title="Volume">
        <img id="volume" src="/static/images/rainyroom/volumeWhite3.png" alt="3"/>
    </a>
</div>
<br/>
<h1>Rainy Room</h1>
<h2>Rain Makes Everything Better</h2>
</div>
</body>
</html>
