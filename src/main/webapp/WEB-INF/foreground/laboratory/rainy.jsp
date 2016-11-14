<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ShawのRainy Room</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="/favicon.ico" rel="SHORTCUT ICON">
    <script src="/static/bootstrap3/js/jquery-1.11.2.min.js"></script>
    <script src="/static/bootstrap3/js/jquery.jplayer.min.js"></script>
</head>
<style>
    body {
        padding-top: 50px;
        padding-bottom: 20px;
        background-color: #5b5c5f;
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
</script>
<body>
<div id="jquery_jplayer_1"></div>
<script type="text/javascript">
    function volumeDownOrPause() {
        oldVol = document.getElementById("volume").src;
        if (oldVol == "http://rainymood.com/i/volumeWhite1.png") {
            document.getElementById("volume").src = "http://rainymood.com/i/volumeWhite0.png";
            jQuery("#jquery_jplayer_1").jPlayer("pause");
        }
        if (oldVol == "http://rainymood.com/i/volumeWhite2.png") {
            document.getElementById("volume").src = "http://rainymood.com/i/volumeWhite1.png";
            jQuery("#jquery_jplayer_1").jPlayer("volume", 0.30);
        }
        if (oldVol == "http://rainymood.com/i/volumeWhite3.png") {
            document.getElementById("volume").src = "http://rainymood.com/i/volumeWhite2.png";
            jQuery("#jquery_jplayer_1").jPlayer("volume", 0.60);
        }
        if (oldVol == "http://rainymood.com/i/volumeWhite0.png") {
            document.getElementById("volume").src = "http://rainymood.com/i/volumeWhite3.png";
            jQuery("#jquery_jplayer_1").jPlayer("volume", 1.00);
            jQuery("#jquery_jplayer_1").jPlayer("play");
        }
    }
</script>
<br/>
<div id="spinner"><img src="http://rainymood.com/i/spinner2.gif" width="32" height="26" alt=""/></div>
<div id="volumeDiv" style="display:none;">
    <a onclick="volumeDownOrPause();" title="Volume"><img id="volume"
                                                          src="http://rainymood.com/i/volumeWhite3.png"/></a>
</div>
<br/>
<h1>Rainy Mood</h1>
<h2>Rain Makes Everything Better</h2>
</body>
</html>
