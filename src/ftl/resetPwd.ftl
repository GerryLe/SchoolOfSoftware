<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head>
    <title>账号</title>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

	<style>
		body,h1,h2,h3,ul,li{margin:0px;padding:0px;}body{width:1000px;margin:0 auto;font-size:14px;}
		ul{list-style:none;}#top{height:80px;border-bottom:6px solid #3F9BCA;position:relative;}
		#top h2{line-height:80px;font-size:26px;text-align:center;}#top .pd{position:absolute;right:0px;bottom:5px;display:inline-block;}
		#footer{margin-top:10px;border-top:3px solid #3F9BCA;height:50px;line-height:20px;}#footer ul{margin-top:5px;float:right;}#footer li{text-align:right;}
		#footer li a{text-decoration:none;color:red;}
		a{text-decoration: none; color: red;} 
	</style>
</head>

<body>

	<div id="top">
		<h2>系统登录账号和密码</h2>
	</div>
	<div id="center">
		您好：${name} <br/><br/>
    		
   		&nbsp;&nbsp;&nbsp;由于你在项目管理系统上的登录密码长时间为空，系统将重置你的密码。<br/>
   		&nbsp;&nbsp;&nbsp;登录账号：${account} <br/>
   		&nbsp;&nbsp;&nbsp;登录密码：${password} <br/>
   		&nbsp;&nbsp;&nbsp;访问地址：${loginURL} <br/><br/><br/>
   		
   		<font>
    		相关事项：<br/>
    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    		请保管好账号密码，登录后请及时修改。<br/>
    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    		系统不支持IE10以下的浏览器，推荐您使用最新版的 Chrome、Firefox 及以上版本的浏览器，以获取最佳的浏览效果<br/>
   		</font>
   		<br/><br/>
	
	</div>
</body>
</html>
