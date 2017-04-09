<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div>
	<input type="hidden" id="user_id" value="${id}"> <input type="hidden" id="userroles" value="${userroles}">
	<div class="panel-body">
		<table id="role_table" class="table-condensed table table-hover" data-click-to-select="true" data-side-pagination="server"></table>
	</div>
</div>
<script type="text/javascript">
	var $userrole_table;
	$(function() {
		var userroles = $("#userroles").val();
		$userrole_table = $.BOOT.table("role_table", $.webapp.root
				+ "/admin/system/role/datagrid.do", {
			height : "auto",
			shows : false,
			columns : [ {
				field : 'id',
				title : 'id',
				visible : false
			}, {
				field : 'state',
				checkbox : true,
				formatter : function(value, row, index) {
					if (userroles.indexOf(row.id) != -1) {
						return {
							checked : true
						};
					}
					return value;
				}
			}, {
				field : 'name',
				title : '名称'
			} ]
		});
	});
	$.BOOT.form("form_addroles", {}, function(params) {
		var selects = $userrole_table.bootstrapTable('getSelections');
		var ids = $.map(selects, function(row) {
			return row.id;
		});
		var form_url = $.webapp.root + "/admin/system/user/updateRole.do";
		$.post(form_url, {
			id : $("#user_id").val(),
			role_ids : ids.join(",")
		}, function(result) {
			$user_table.bootstrapTable('refresh');
			modaladdroles();
			$.BOOT.toast1(result);
		}, 'json');
	});
</script>