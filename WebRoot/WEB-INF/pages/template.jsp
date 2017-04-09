<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%
	Object url = request.getAttribute("url");
%>
<jsp:include page="/WEB-INF/pages/ic.jsp">
	<jsp:param value="<%=url %>" name="url" /></jsp:include>