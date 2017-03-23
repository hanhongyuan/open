<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <script language="javascript">
        var host =  window.location.host;//snsapi_userinfo    snsapi_base
        
        
        var _href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx79bd044fd98536f4"+
        			"&redirect_uri=http%3a%2f%2fwwt.bj37du.com%2fopen%2fcallback"+
        			"&response_type=code&scope=snsapi_userinfo&state=STATE&component_appid=wx4f6a40c48f6da430#wechat_redirect";
        window.location.href=_href;
    </script>
</head>
<body>
</body>
</html>
