<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<div class="col-md-12">
    <div class="row gray-bg">
        <footer class="tm-footer">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12">
                <p class="text-xs-center tm-footer-text">Power By  <span class="fa fa-github"></span>&nbsp;<a href="https://github.com/imn5100"
                                                                     target="_blank">Imn5100</a></p>
                <p class="text-xs-center tm-footer-text">浙ICP备16038105号</p>
                <p class="text-xs-center tm-footer-text">浙公网安备33010602007235号</p>
            </div>
        </footer>
    </div>
</div>
<a class="cd-top"></a>
<script src="/static/js/jquery-1.11.3.min.js"></script>
<script src="/static/js/tether.min.js"></script>
<script src="/static/js/bootstrap.min.js"></script>
<script src="/static/js/jquery.singlePageNav.min.js"></script>
<style>
    .cd-top {
        display: none;
        height: 40px;
        width: 40px;
        position: fixed;
        bottom: 10px;
        right: 10px;
        box-shadow: 0 0 10px rgba(0, 0, 0, .05);
        background: url(/static/images/cd-top-arrow.svg) center 50% no-repeat rgba(52, 73, 94, .8);
        opacity: 1;
        -webkit-transition: opacity .3s 0s, visibility 0s .3s;
        -moz-transition: opacity .3s 0s, visibility 0s .3s;
        transition: opacity .3s 0s, visibility 0s .3s;
    }
</style>
<script>
    function throttle(method, delay, duration) {
        var timer = null, begin = new Date();
        return function () {
            var context = this, args = arguments, current = new Date();
            clearTimeout(timer);
            if (current - begin >= duration) {
                method.apply(context, args);
                begin = current;
            } else {
                timer = setTimeout(function () {
                    method.apply(context, args);
                }, delay);
            }
        }
    }
    function showTop() {
        if ($(window).scrollTop() < 100) {
            $(".cd-top").hide();
        } else {
            $(".cd-top").show();
        }
    }
    $(window).scroll(throttle(showTop, 500, 1000));
    $(function () {
        $('#tmNavbar a').click(function () {
            if (this.id == 'navbarDropdownMenuLink') {
                return
            }
            $('#tmNavbar').collapse('hide');
            if (String(this.href).indexOf("#") == -1)
                location.href = this.href;
        });

        $('.jumpHook a').click(function () {
            if (String(this.href) != "#")
                location.href = String(this.href);
        });
        $(".cd-top").eq(0).click(function () {
            $("html,body").animate({scrollTop: 0}, 500);
            return false;
        });
    })
</script>