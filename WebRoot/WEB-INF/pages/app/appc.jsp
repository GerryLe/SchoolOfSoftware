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
<link rel="stylesheet" href="${webRoot}/template/resource/plugins/validator/bootstrapValidator.min.css" />
<link rel="stylesheet" href="${webRoot}/template/resource/css/AdminLTE.css" />
<link rel="stylesheet" href="${webRoot}/template/resource/css/_all-skins.min.css" />
<link rel="stylesheet" href="${webRoot}/template/resource/plugins/sweetalert/sweetalert.css" />
<link rel="stylesheet" href="${webRoot}/template/resource/plugins/toast/jquery.toast.min.css" />
<link rel="stylesheet" href="${webRoot}/template/resource/plugins/font-awesome/css/font-awesome.min.css" />
<link rel="stylesheet" href="${webRoot}/template/resource/css/simple.css" />
<script src="${webRoot}/template/resource/js/jquery-2.1.4.min.js"></script>
<script src="${webRoot}/template/resource/plugins/validator/bootstrapValidator.min.js"></script>
<script src="${webRoot}/template/resource/plugins/jquery/jquery.timers-1.1.2.js"></script>
<script src="${webRoot}/template/resource/js/bootstrap.min.js"></script>
<script src="${webRoot}/template/resource/plugins/sweetalert/sweetalert-dev.js"></script>
<script src="${webRoot}/template/resource/plugins/toast/jquery.toast.min.js"></script>
<script src="${webRoot}/template/resource/js/simple.js"></script>
<style>
<!--
body {
	background-image: url("/template/images/bg.gif");
}

.container {
	width: 1000px;
	margin: 0 auto;
	background-color: white;
	    padding-left: 0px!important;
    padding-right: 0px!important;
}
-->
</style>
</head>
<body>
	<div class="container">
	<jsp:include page="<%=url%>"></jsp:include>
	</div>
</body>
</html>
