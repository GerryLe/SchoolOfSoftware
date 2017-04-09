<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
	<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!-- Content Header (Page header) -->
<section class="content">
	<div id="user-toolbar"></div>
	<div class="row">
		<div class="col-xs-6 col-md-4">
			<table id="user_table" class="table-condensed table table-hover" data-side-pagination="server"></table>
		</div>
		<div class="col-xs-12 col-sm-6 col-md-8">
			<button type="button" id="user_permits" class="btn btn-success company_edit"><spring:message code="save" /></button>
			<div id="treeview1" class=""></div>
		</div>
	</div>
</section>
<script type="text/javascript">
	var $user_table, $tree_user;
	$.BOOT.click("#user_permits", function(c) {
		$.BOOT.savePermits($tree_user, $user_table.$value, "USER");
	});
	$(function() {
		$user_table = $.BOOT.table("user_table",
				$.webapp.root + "/admin/system/user/datagrid.do", {
					columns : [ {
						field : 'name',
						title : '姓名'
					}, {
						field : 'account',
						title : '账号'
					}, {
						field : 'id',
						title : 'id',
						visible : false
					} ],
					toolbar : "#user-toolbar",
					shows : false,
				}).on('click-row.bs.table', function(e, row, $element) {
			$user_table.initRow($element, row.id);
			$.BOOT.getPermits($tree_user, tree_data, row.id, "USER");
		});
	});
	var tree_data;
	function loadmenuTree() {
		$.BOOT.loadMenu("treeview1", function($tree, data) {
			tree_data = data;
			$tree_user = $tree;
		});
	}
	$(function() {
		loadmenuTree();
	});
</script>