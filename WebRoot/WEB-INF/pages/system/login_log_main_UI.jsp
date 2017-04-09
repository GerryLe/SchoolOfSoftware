<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">
	var $log_table;
	$(function() {
		$log_table = $.BOOT.table("log_table", $.webapp.root
				+ "/admin/system/log/datagridLL.do", {
			columns : [ {
				field : 'userName',
				title : '<spring:message code="username" />'
			}, {
				field : 'browserType',
				title : '<spring:message code="Browsertype" />',
			}, {
				field : 'platformType',
				title : '<spring:message code="platform" />'
			}, {
				field : 'ip',
				title : '<spring:message code="ip" />',
				width : 150,
			}, {
				field : 'created',
				title : '<spring:message code="Operatingtime" />',
				width : 150,
				sortable : true
			} ],
			shows : false
		});
	});
</script>
<!-- Content Header (Page header) -->
<section class="content">
	<table id="log_table" class="table-condensed table table-hover" data-side-pagination="server"></table>
</section>