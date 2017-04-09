<%@page import="com.rosense.basic.util.StringUtil"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
	<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
	String id = request.getParameter("id");
	String title = request.getParameter("title");
	String bottom = StringUtil.toString(request.getParameter("bottom"),
			"true");
%>
<div class="modal fade" id="<%=id%>Modal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<form id="form_<%=id%>">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title"><%=title%></h4>
				</div>
				<div class="modal-body" id="content_<%=id%>"></div>
				<%
					if ("true".equals(bottom)) {
				%>
				<div class="modal-footer">
					<button type="submit" class="btn btn1 btn-primary" id="permit_<%=id%>" style="display: none;">同意</button>
					<button type="submit" class="btn btn1 btn-primary" id="refuse_<%=id%>" style="display: none;">拒绝</button>
					<button type="button" class="btn btn1 btn-default" data-dismiss="modal"><spring:message code="Shutdown" /></button>
					<button type="submit" class="btn btn1 btn-primary" id="save_<%=id%>"><spring:message code="save" /></button>
					<button type="reset" class="btn btn1 btn-primary" id="reset_<%=id%>" style="display: none;"><spring:message code="reset" /></button>
				</div>
				<%
					}
				%>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript">
	function modal<%=id%>() {
		$('#<%=id%>Modal').modal('hide');
		$.BOOT.btn("save_<%=id%>", false);
	}
</script>