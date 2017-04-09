<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<jsp:include page="/template/modal.jsp"><jsp:param value="addposition" name="id" /><jsp:param value="编辑职位" name="title" /></jsp:include>
<script src="${webRoot}/template/resource/plugins/tools/user.js"></script>
<section class="content">
	<div id="position-form-id"></div>
	<table style="width: 100%;">
		<tr>
			<td style="width: 50%;vertical-align: top;">
				<div id="filter-bar">
					<div class="form-inline" role="form">
						<button type="button" class="btn btn-primary position_edit" id="position_edit_idd">添加</button>
					</div>
				</div>
				<div id="treeview1" class="" style="min-height: 500px;"></div>
			</td>
			<td style="width: 50%;vertical-align: top;">
				<div id="filter-bar">
					<button type="button" class="btn btn-primary " onclick="$('#userModal').modal('show');">添加用户</button>
				</div>
				<table id="refuser_table" class="table-condensed table table-hover" data-side-pagination="server"></table>
			</td>
		</tr>
	</table>
</section>
<script type="text/javascript">
	function loadpositionTree() {
		var jstree = null;
		var href = $.webapp.root + "/admin/system/position/tree.do";
		$.post(href, {}, function(result) {
			$('#treeview1').data('jstree', false);
			jstree = $('#treeview1').jstree({
				'plugins' : [ "wholerow" ],
				'core' : {
					'data' : result,
					'themes' : {
						'name' : 'proton',
						'responsive' : true
					},
					acts : [ {
						text : "添加",
						cla : "position_add"
					}, {
						text : "编辑",
						cla : "position_edit"
					}, {
						text : "删除",
						cla : "position_del"
					} ]
				}
			}).bind("loaded.jstree", function(e, data) {
				jstree.jstree("open_all");
			}).bind("select_node.jstree", function(event, data) {
				positionId = data.selected[0];
				$refuser_table.bootstrapTable('refresh', {
					query : {
						id : data.selected[0]
					}
				});
			});
		}, 'json');
	}

	var positionId = null;
	function delete_datas(userId) {
		var form_url = $.webapp.root + "/admin/system/user/deleteuserposition.do";
		$.post(form_url, {
			userId : userId
		}, function(result) {
			$refuser_table.bootstrapTable('refresh');
			$.BOOT.toast1(result);
		}, 'json');
	}
	var $refuser_table;
	$(function() {
		$users.initUsers();
		$users.callback = function(userId) {
			if (positionId == null) {
				$.BOOT.toast(false, "请先选择职位");
			} else {
				var form_url = $.webapp.root
						+ "/admin/system/user/adduserposition.do";
				$.post(form_url, {
					userIds : userId,
					positionId : positionId
				}, function(result) {
					$refuser_table.bootstrapTable('refresh');
					$.BOOT.toast1(result);
				}, 'json');
			}
		};
		var url = $.webapp.root
				+ "/admin/system/user/datagrid_ref.do?param1=position";
		$refuser_table = $.BOOT.table("refuser_table", url, {
			columns : [ {
				field : 'account',
				title : '账号'
			}, {
				field : 'name',
				title : '姓名'
			}, {
				title : "操作",
				formatter : function(value, row, index) {
					return buildact(row);
				}
			} ],
			paginationInfo : true,
			showExport : true
		});
		loadpositionTree();
		$.BOOT.click(".position_del", function(c) {
			var id = $(c).attr("val");
			var form_url = $.webapp.root + "/admin/system/position/delete.do";
			var json = {
				title : "",
				text : "同时会删除子节点，确定删除吗?",
				showCancelButton : true,
				type : 'warning',
				call : function() {
					$.post(form_url, {
						id : id
					}, function(result) {
						loadpositionTree();
						$.BOOT.toast1(result);
					}, 'json');
				}
			};
			$.BOOT.alert(json, true);
		});
		$.BOOT.click(".position_edit", function(c) {
			var id = $(c).attr("val");
			var href = $.webapp.root + '/admin/system/position/position_form_UI.do?id=';
			href += (id ? id : "");
			$.BOOT.page("content_addposition", href, function() {
				$('#addpositionModal').modal('toggle');
			});
		});
		$.BOOT.click("#position_edit_idd", function(c) {
			var href = $.webapp.root + '/admin/system/position/position_form_UI.do';
			$.BOOT.page("content_addposition", href, function() {
				$('#addpositionModal').modal('toggle');
			});
		});
		$.BOOT.click(".position_add", function(c) {
			var id = $(c).attr("val");
			var href = $.webapp.root + '/admin/system/position/position_form_UI.do?pid=';
			href += (id ? id : "");
			$.BOOT.page("content_addposition", href, function() {
				$('#addpositionModal').modal('toggle');
			});
		});
	});
	function buildact(row) {
		var html = "<button class='btn btn-danger btn-xs' onclick=\"delete_datas('"
				+ row.personId + "');\">移除</button>";
		return html;
	}
</script>