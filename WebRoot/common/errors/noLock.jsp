<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<%String basePath = request.getContextPath();%>
<html>
<head>
<meta charset=utf-8 />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link rel="stylesheet" href="<%=basePath %>/template/resource/css/bootstrap.min.css" />
<title>你的账号已被锁定，请稍后再试，如有疑问，请联系管理员!</title>
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
<body>
	<div class="demo">
		<p>你的账号已被锁定，请稍后再试，如有疑问，请联系管理员!</p>
	</div>
</body>
</html>
