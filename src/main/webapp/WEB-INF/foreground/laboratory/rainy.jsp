<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Rainy Room</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="雨天房间">
    <link href="/favicon.ico" rel="SHORTCUT ICON">
    <link rel="stylesheet" href="/static/bootstrap3/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/bootstrap3/css/bootstrap-theme.min.css">
    <script src="/static/bootstrap3/js/jquery.js"></script>
    <script src="/static/bootstrap3/js/bootstrap.min.js"></script>
    <script src="/static/bootstrap3/js/jquery.jplayer.min.js"></script>
</head>
<style>
    body {
        padding-top: 50px;
        padding-bottom: 20px;
        background-color: #efefef;
        font-family: 'marcellus_scregular', serif;
        text-shadow: 0px 0px 15px rgba(0, 0, 0, 0.15);
        font-size: 20px;
        color: #fff;
    }

    .centerDiv {
        text-align: center;
        position: absolute;
        top: 50%;
        left: 20%;
        right: 20%;
        -webkit-transform: translateY(-50%);
        -moz-transform: translateY(-50%);
        -ms-transform: translateY(-50%);
        -o-transform: translateY(-50%);
        transform: translateY(-50%);
    }

    .bgDiv {
        left: 0;
        right: 0;
        top: 0;
        bottom: 0;
        z-index: 0;
        position: fixed;
    }

    .wallpaper {
        width: 100%;
        height: 100%;
    }
</style>
<script type="text/javascript">
    jQuery(document).ready(function () {
        jQuery("#jquery_jplayer_1").jPlayer({
            ready: function () {
                jQuery(this).jPlayer("setMedia", {
                    m4a: "/static/userImages/sound/0.m4a"
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
        var BGswitch = {
            images: '',
            imagesLength: '',
            index: 0,
            init: function () {
                BGswitch.images = $('.wallpaper')
                BGswitch.imagesLength = BGswitch.images.length
                setInterval(function () {
                    if (BGswitch.index == BGswitch.imagesLength - 1) {
                        BGswitch.switch(BGswitch.index, 0)
                    } else {
                        BGswitch.switch(BGswitch.index, BGswitch.index + 1)
                    }
                }, 1000 * 60 * 5)
            },
            switch: function (oldi, newi) {
                setTimeout(function () {
                    BGswitch.images.eq(oldi).fadeOut("normal", function () {
                    });
                }, 100);
                BGswitch.images.eq(newi).fadeIn("normal");
                BGswitch.index = newi
            }
        }
        BGswitch.init();
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
<div class="bgDiv">
    <div class="wallpaper"
         style="background: url('http://cdn.shawblog.me/49649473_p0_1479358279249') no-repeat center;background-size: cover;">
    </div>
    <div class="wallpaper"
         style="background: url('http://cdn.shawblog.me/51368875_p0_1479358290644') no-repeat center;background-size: cover;">
    </div>
</div>
<div class="centerDiv">
    <div id="spinner"><img src="/static/images/rainyroom/spinner2.gif" width="32" height="26" alt=""/></div>
    <div id="volumeDiv" style="display:none;">
        <a onclick="volumeDownOrPause();" title="Volume">
            <img id="volume" src="/static/images/rainyroom/volumeWhite3.png" alt="3"/>
        </a>
    </div>
    <br/>

    <h1>Rainy Room</h1>

    <h2>Rain Makes Everything Better,Good Sleep</h2>
</div>
</body>
</html>
