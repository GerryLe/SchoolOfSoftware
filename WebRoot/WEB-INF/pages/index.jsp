<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
		<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
	<%-- <% response.sendRedirect("hello.do"); %> --%>
<jsp:include page="/WEB-INF/pages/c.jsp">
	<jsp:param value="/WEB-INF/pages/index_c.jsp" name="url" /></jsp:include>