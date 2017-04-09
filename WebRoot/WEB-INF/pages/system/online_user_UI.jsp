<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">
	var $online_table;
	$(function() {
		$online_table = $.BOOT.table("online_table", $.webapp.root
				+ "/admin/system/online/datagrid.do", {
			columns : [ {
				field : 'account',
				title : '<spring:message code="landingaccount" />',
			}, {
				field : 'name',
				title : '<spring:message code="Staff'sname" />',
			}, {
				field : 'orgName',
				title : '<spring:message code="Subordinatedepartments" />',
			}, {
				field : 'ip',
				title : '<spring:message code="ip" />',
			}, {
				field : 'loginTime',
				title : '<spring:message code="Thelogintime" />',
			} ],
			shows : false,
		});
	});
</script>
<section class="content">
	<table id="online_table" class="table-condensed table table-hover" data-side-pagination="server"></table>
</section>