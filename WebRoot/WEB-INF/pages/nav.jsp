<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%
	String nav = request.getParameter("nav");
%>
<style>
.content-header span {
	margin-right: 15px;
	cursor: pointer;
}

.content-header>.breadcrumb>li+li:before {
	display: none;
}
</style>
<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="/admin/index/index.do"><i class="fa fa-home"></i>
				首页</a></li>
		<li class="active">/ <%=nav%></li>
		<li style="float: right;"><span onclick="$.BOOT.pagerefresh();">刷新</span><span
			onclick="$.BOOT.history();">后退</span></li>
	</ol>
	<ol></ol>
</section>