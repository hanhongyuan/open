<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="./resource/js/jquery-3.1.1.min.js"></script>
    <script>
    var host =  window.location.host;
    
    
    var _href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx79bd044fd98536f4"+
    			"&redirect_uri=http%3a%2f%2f"+host+"%2fopen%2fcallback"+
    			"&response_type=code&scope=snsapi_base&state=STATE&component_appid=wx4f6a40c48f6da430#wechat_redirect";
   // window.location.href=_href;
    	function _submit(){
    		var appId = "wx4f6a40c48f6da430";
    		$.get("./authWeChat",{"appId":appId},function(data){
    			if(data.code == 0){
    				var preAuthCode = data.data;
    				var _href = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=wx4f6a40c48f6da430&pre_auth_code="+preAuthCode+"&redirect_uri=http%3a%2f%2f"+host+"%2fopen%2fcallback%3fappId%3d"+appId;
    				window.location.href = _href;
    			}else{
    				alert(data.msg);
    			}
    		})
    	}
    	
    	$(function(){
    		_submit();
    	});
    </script>
</head>
<body>
<h1>智慧商街</h1>
<h2>Hello word!!!</h2>

</body>
</html>
