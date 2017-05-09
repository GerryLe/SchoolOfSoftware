<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<jsp:include page="/template/modal.jsp"><jsp:param value="addroles" name="id" /><jsp:param value="编辑角色" name="title" /></jsp:include>
<script src="${webRoot}/template/resource/plugins/tools/user.js"></script>
<section class="content">
	<table style="width: 100%;">
		<tr>
			<td style="width: 50%;vertical-align: top;">
				<div id="filter-bar">
					<button type="button" class="btn btn-primary " id="role_add">添加</button>
				</div>
				<table id="role_table" class="table-condensed table table-hover" data-side-pagination="server"></table>
			</td>
			<td style="width: 50%;vertical-align: top;">
			<!-- 	<div id="filter-bar">
					<button type="button" class="btn btn-primary " onclick="$('#userModal').modal('show');">添加用户</button>
				</div>
				<table id="refuser_table" class="table-condensed table table-hover" data-side-pagination="server"></table>
			 --></td> 
		</tr>
	</table>

</section>

<script type="text/javascript">
	var $role_table, $refuser_table;
	var roleId = null;
	$(function() {
		$users.initUsers();
		var roleurl = $.webapp.root + "/admin/system/role/datagrid.do";
		$role_table = $.BOOT.table("role_table", roleurl, {
			columns : [ {
				field : 'name',
				title : '名称'
			},{
				field : 'defaultRole',
				title : '默认',
				formatter : function(value, row, index) {
					return value?"是":"否";
				}
			}, {
				field : 'id',
				title : '操作',
				formatter : function(value, row, index) {
					return $.BOOT.groupbtn(value, [ {
						cla : 'role_del',
						text : "删除"
					}, {
						cla : 'role_edit',
						text : "编辑"
					} ]);
				}
			} ],
			toolbar : "#role-toolbar",
		}).on('click-row.bs.table', function(e, row, $element) {
			roleId = row.id;
			$role_table.initRow($element, row.id);
			$refuser_table.bootstrapTable('refresh', {
				query : {
					id : row.id
				}
			});
		});

	});
	$.BOOT.click(".role_del", function(c) {
		var id = $(c).attr("val");
		var form_url = $.webapp.root + "/admin/system/role/delete.do";
		var json = {
			title : "",
			text : "确定删除吗?",
			showCancelButton : true,
			type : 'warning',
			call : function() {
				$.post(form_url, {
					id : id
				}, function(result) {
					$role_table.bootstrapTable('refresh');
					$.BOOT.toast1(result);
				}, 'json');
			}
		};
		$.BOOT.alert(json, true);
	});
	$.BOOT.click(".role_edit", function(c) {

		
		var id = $(c).attr("val");
		var href = '/admin/system/role/role_form_UI.do?id=' + id;
		$.BOOT.page("content_addroles", href, function() {
			$('#addrolesModal').modal('toggle');
		});
	});
	$.BOOT.click("#role_add", function(c) {
		var href = $.webapp.root + '/admin/system/role/role_form_UI.do';
		$.BOOT.page("content_addroles", href, function() {
			$('#addrolesModal').modal('toggle');
		});
	});
	$.BOOT.click("#role-search-id", function(c) {
		$role_table.bootstrapTable('refresh', {
			query : {
				name : encodeURI($("#role-name").val())
			}
		});
	});
	function buildact(row) {
		var html = "<button class='btn btn-danger btn-xs' onclick=\"delete_datas('"
				+ row.id + "');\">移除</button>";
		return html;
	}
</script>