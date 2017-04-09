<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:include page="/template/modal.jsp"><jsp:param value="addprofession" name="id" /><jsp:param value="编辑专业" name="title" /></jsp:include>
<script src="${webRoot}/template/resource/plugins/tools/user.js"></script>
<section class="content">
	<div id="org-form-id"></div>
	<table style="width: 100%;">
		<tr>
			<td style="width: 50%;vertical-align: top;">
				<div id="filter-bar">
					<div class="form-inline" role="form">
						<button type="button" class="btn btn-primary org_edit" id="profession_edit_idd">添加</button>
					</div>
				</div>
				<div id="treeview1" class="" style="min-height: 500px;"></div>
			</td>
			<td style="width: 50%;vertical-align: top;">
				<div id="filter-bar">
					<button type="button" class="btn btn-primary " onclick="$('#userModal').modal('show');">添加学生</button>
				</div>
				<table id="refuser_table" class="table-condensed table table-hover" data-side-pagination="server"></table>
			</td>
		</tr>
	</table>
</section>
<script type="text/javascript">
	function loadorgTree() {
		var jstree = null;
		var href = $.webapp.root + "/admin/system/profession/tree.do";
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
						text : "<spring:message code="add" />",
						cla : "org_add"
					}, {
						text : "<spring:message code="edit" />",
						cla : "org_edit"
					}, {
						text : "<spring:message code="delete" />",
						cla : "org_del"
					} ]
				}
			}).bind("loaded.jstree", function(e, data) {
				jstree.jstree("open_all");
			}).bind("select_node.jstree", function(event, data) {
				orgId = data.selected[0];
				$refuser_table.bootstrapTable('refresh', {
					query : {
						id : data.selected[0]
					}
				});
			});
		}, 'json');
	}

	var orgId = null;
	function delete_datas(userId) {
		var form_url = $.webapp.root + "/admin/system/user/deleteuserorg.do";
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
			if (orgId == null) {
				$.BOOT.toast(false, "<spring:message code="Pleaseselectadepartment" />");
			} else {
				var form_url = $.webapp.root
						+ "/admin/system/user/adduserorg.do";
				$.post(form_url, {
					userIds : userId,
					orgId : orgId
				}, function(result) {
					$refuser_table.bootstrapTable('refresh');
					$.BOOT.toast1(result);
				}, 'json');
			}
		};
		var url = $.webapp.root
				+ "/admin/system/user/datagrid_ref.do?param1=org";
		$refuser_table = $.BOOT.table("refuser_table", url, {
			columns : [ {
				field : 'account',
				title : '<spring:message code="account" />'
			}, {
				field : 'name',
				title : '<spring:message code="name" />'
			}, {
				title : "<spring:message code="operation" />",
				formatter : function(value, row, index) {
					return buildact(row);
				}
			} ],
			paginationInfo : true,
			showExport : true
		});
		loadorgTree();
		$.BOOT.click(".profession_del", function(c) {
			var id = $(c).attr("val");
			var form_url = $.webapp.root + "/admin/system/org/delete.do";
			var json = {
				title : "",
				text : "<spring:message code="Childnodedeletion" />",
				showCancelButton : true,
				type : 'warning',
				call : function() {
					$.post(form_url, {
						id : id
					}, function(result) {
						loadorgTree();
						$.BOOT.toast1(result);
					}, 'json');
				}
			};
			$.BOOT.alert(json, true);
		});
		$.BOOT.click(".profession_edit", function(c) {
			var id = $(c).attr("val");
			var href = $.webapp.root + '/admin/system/org/org_form_UI.do?id=';
			href += (id ? id : "");
			$.BOOT.page("content_addprofession", href, function() {
				$('#addprofessionModal').modal('toggle');
			});
		});
		$.BOOT.click("#profession_edit_idd", function(c) {
			var href = $.webapp.root + '/admin/system/org/org_form_UI.do';
			$.BOOT.page("content_addprofession", href, function() {
				$('#addprofessionModal').modal('toggle');
			});
		});
		$.BOOT.click(".profession_add", function(c) {
			var id = $(c).attr("val");
			var href = $.webapp.root + '/admin/system/org/org_form_UI.do?pid=';
			href += (id ? id : "");
			$.BOOT.page("content_addorgs", href, function() {
				$('#addprofessionModal').modal('toggle');
			});
		});
	});
	function buildact(row) {
		var html = "<button class='btn btn-danger btn-xs' onclick=\"delete_datas('"
				+ row.personId + "');\">移除</button>";
		return html;
	}
</script>