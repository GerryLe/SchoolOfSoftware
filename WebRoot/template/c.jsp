<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<%@page import="com.rosense.basic.util.cons.Const"%>
<%@page import="com.rosense.basic.util.StringUtil"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh">
<head>
<%
	final String title = request.getParameter("title");
	final String url = request.getParameter("url");
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
	String contextPath = request.getContextPath();
%>
<title><%=Const.base.get("title")%></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link rel="stylesheet" href="<%=basePath%>/template/resource/css/bootstrap.min.css" />
<link rel="stylesheet" href="<%=basePath%>/template/resource/plugins/validator/bootstrapValidator.min.css" />
<link rel="stylesheet" href="<%=basePath%>/template/resource/css/AdminLTE.css" />
<link rel="stylesheet" href="<%=basePath%>/template/resource/css/_all-skins.min.css" />
<link rel="stylesheet" href="<%=basePath%>/template/resource/css/simple.css" />
<script src="<%=basePath%>/template/resource/js/jquery-2.1.4.min.js"></script>
<script src="<%=basePath%>/template/resource/js/bootstrap.min.js"></script>
<script src="<%=basePath%>/template/resource/plugins/validator/bootstrapValidator.min.js"></script>
<script src="<%=basePath%>/template/resource/js/simple.js"></script>
</head>

<body class="skin-blue  sidebar-mini">
	<jsp:include page="<%=url%>"></jsp:include>
	<script src="<%=basePath%>/template/resource/plugins/slimScroll/jquery.slimscroll.min.js"></script>
</body>
</html>
