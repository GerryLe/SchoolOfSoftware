<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<%String basePath = request.getContextPath();%>
<html>
<head>
<meta charset=utf-8 />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link rel="stylesheet" href="<%=basePath %>/template/resource/css/bootstrap.min.css" />
<script src="<%=basePath%>/template/resource/js/jquery-2.1.4.min.js"></script>
<title>操作警告</title>
<style>
body {
	background: #d2d6de;
	font-family: 'Open Sans', sans-serif;
	font-size: 14px;
	color: #3c3c3c;
	margin: 7% auto;
	text-align: center;
	background-image: url("/template/images/bg.gif");
}
</style>
</head>
<script>
$(function() {
	var winWidth = parseInt($.webapp.getInner().width) ;
	var winHeight = parseInt($.webapp.getInner().height) ;
	
	var noSecurity = $("#noSecurity") ;
	var width =  parseInt($.webapp.getStyle(noSecurity[0],"width"));
	var height =  parseInt($.webapp.getStyle(noSecurity[0],"height"));
	
	noSecurity.css("left",((winWidth-width)/2)-50+"px");
	noSecurity.css("top",((winHeight-height)/2)-80+"px");
	var str = "权限不足，请<a href='javascript:callMe();'>联系超管</a>赋予您资源访问权限。<br/>"+
	  		  "&nbsp;${msg}";
	$("#msg_error").html(str) ;
	
});
function callMe() {
	alert("Don't call me！") ;
}
</script>
<body>
<div id="noSecurity">
	<h1>操作警告</h1>
	<div id="msg_error"></div>
</div>
</body>
</html>