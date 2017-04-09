<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
	<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!-- Content Header (Page header) -->
<section class="content">
	<div id="role-form-id"></div>
	<div id="role-toolbar"></div>
	<div class="row">
		<div class="col-xs-6 col-md-4">
			<table id="role_table" class="table-condensed table table-hover" data-side-pagination="server"></table>
		</div>
		<div class="col-xs-12 col-sm-6 col-md-8">
			<button type="button" id="role_permits" class="btn btn-success company_edit"><spring:message code="save" /></button>
			<div id="treeview1_role" class=""></div>
		</div>
	</div>
</section>
<script type="text/javascript">
	var $role_table, $tree_role;
	$.BOOT.click("#role_permits", function(c) {
		$.BOOT.savePermits($tree_role, $role_table.$value, "ROLE");
	});
	$(function() {
		$role_table = $.BOOT.table("role_table",
				$.webapp.root + "/admin/system/role/datagrid.do", {
					columns : [ {
						field : 'name',
						title : '角色名'
					} ],
					toolbar : "#role-toolbar",
					shows : false,
				}).on('click-row.bs.table', function(e, row, $element) {
			$role_table.initRow($element, row.id);
			$.BOOT.getPermits($tree_role, tree_data_role, row.id, "ROLE");
		});
	});
	var tree_data_role;
	function loadmenuTree_role() {
		$.BOOT.loadMenu("treeview1_role", function($tree, data) {
			tree_data = data;
			$tree_role = $tree;
		});
	}
	$(function() {
		loadmenuTree_role();
	});
</script>