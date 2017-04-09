<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<%@page import="com.rosense.basic.util.cons.Const"%>
<%@page import="com.rosense.basic.util.StringUtil"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-CN">
<head>
<%
	final String title = request.getParameter("title");
	final String url = request.getParameter("url");
%>
<title><%=Const.base.get("title")%></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link rel="stylesheet" href="${webRoot}/template/resource/css/bootstrap.min.css" />
<link rel="stylesheet" href="${webRoot}/template/resource/css/AdminLTE.css" />
<link rel="stylesheet" href="${webRoot}/template/resource/css/_all-skins.min.css" />
<link rel="stylesheet" href="${webRoot}/template/resource/plugins/font-awesome/css/font-awesome.min.css" />
<link rel="stylesheet" href="${webRoot}/template/resource/css/simple.css" />
<script src="${webRoot}/template/resource/js/jquery-2.1.4.min.js"></script>
<script src="${webRoot}/template/resource/plugins/jquery/jquery.timers-1.1.2.js"></script>
<script src="${webRoot}/template/resource/js/bootstrap.min.js"></script>
<script src="${webRoot}/template/resource/js/simple.js"></script>
<script type="text/javascript">
	$.webapp.root = '${webRoot}';
	$.webapp.userId = "${USER_SESSION.user.userId}";
	$.webapp.photo = "${USER_SESSION.user.photo}";
	$.webapp.email = "${USER_SESSION.user.email}";
</script>
<style>
<!--
body {
	overflow: hidden;
}
-->
</style>
</head>

<body class="sidebar-mini fixed skin-green-light">
	<jsp:include page="<%=url%>"></jsp:include>
	<script src="${webRoot}/template/resource/plugins/slimScroll/jquery.slimscroll.min.js"></script>
	<script src="${webRoot}/template/resource/js/app.js"></script>
</body>
</html>
