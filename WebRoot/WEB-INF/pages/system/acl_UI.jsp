<%@page import="java.util.Map"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
	<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<section class="content">
	<select onchange="changePermits(this);" class="form-control" style="margin-bottom: 5px;" id="permits_data_type">
		<option value="acl_role_UI.do"><spring:message code="Roleauthorization" /></option>
		<option value="acl_dept_UI.do"><spring:message code="Departmentauthorized" /></option>
		<option value="acl_position_UI.do"><spring:message code="Positionauthorization" /></option>
		<option value="acl_user_UI.do"><spring:message code="Theuserauthorization" /></option>
	</select>
	<div id="permits-div-id"></div>
</section>

<script type="text/javascript">
	function changePermits(c) {
		var href = $.webapp.root + '/admin/system/acl/' + $(c).val();
		$.BOOT.page("permits-div-id", href);
	}
	changePermits("#permits_data_type");
</script>