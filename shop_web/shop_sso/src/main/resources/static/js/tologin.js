//jsonp -> 解决跨域问题
$(function(){
    $.ajax({
        url: "http://localhost:8084/sso/islogin",
        success: function(data){
            // alert("跨域的返回结果：" + data);
            if(data){
                // 已经登录
                $("#pid").html(JSON.parse(data).nickname + "您好，欢迎来到<b><a>ShopCZ商城</a> <a href='http://localhost:8084/sso/zhuxiao'>注销</a></b>");
            } else {
                // 未登录
                $("#pid").html("[<a href='javascript:login();'>登录</a>][<a href='http://localhost:8084/sso/toregister'>注册</a>]");
            }
        },
        dataType:"jsonp",
        jsonpCallback: "islogin"
    });
});

//进行登录
function login(){
    var badUrl = location.href;
    //编码url
    badUrl = encodeURI(badUrl);

    location.href = "http://localhost:8084/sso/tologin?badUrl=" + badUrl;
}