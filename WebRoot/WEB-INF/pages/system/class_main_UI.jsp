<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:include page="/template/modal.jsp"><jsp:param value="addclass" name="id" /><jsp:param value="编辑班级" name="title" /></jsp:include>
<script src="${webRoot}/template/resource/plugins/tools/user.js"></script>
<section class="content">
	<div id="class-form-id"></div>
	<table style="width: 100%;">
		<tr>
			<td style="width: 50%;vertical-align: top;">
				<div id="filter-bar">
					<div class="form-inline" role="form">
						<button type="button" class="btn btn-primary class_edit" id="class_edit_idd">添加班级</button>
					</div>
				</div>
				<div id="treeview1" class="" style="min-height: 500px;"></div>
			</td>
			 <td style="width: 50%;vertical-align: top;">
				<!-- <div id="filter-bar">
					<button type="button" class="btn btn-primary " onclick="$('#userModal').modal('show');">添加学生</button>
				</div> -->
				<table id="refuser_table" class="table-condensed table table-hover" data-side-pagination="server"></table>
			</td> 
		</tr>
	</table>
</section>
<script type="text/javascript">
	function loadclassTree() {
		var jstree = null;
		var href=$.webapp.root +"/admin/system/class/tree.do";
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
						cla : "class_add"
					}, {
						text : "编辑",
						cla : "class_edit"
					}, {
						text : "删除",
						cla : "class_del"
					} ]
				}
			}).bind("loaded.jstree", function(e, data) {
				jstree.jstree("open_all");
			}).bind("select_node.jstree", function(event, data) {
				classId = data.selected[0];
				$refuser_table.bootstrapTable('refresh', {
					query : {
						id : data.selected[0]
					}
				});
			});
		}, 'json');
	}

	var classId = null;
	function delete_datas(userId) {
		var form_url = $.webapp.root + "/admin/system/student/deleteuserclass.do";
		$.post(form_url, {
			userId : userId
		}, function(result) {
			$refuser_table.bootstrapTable('refresh');
			$.BOOT.toast1(result);
		}, 'json');
	}
	var $refuser_table;
	$(function() {
		loadclassTree();
		$.BOOT.click(".class_del", function(c) {
			var id = $(c).attr("val");
			var form_url = $.webapp.root + "/admin/system/class/delete.do";
			var json = {
				title : "",
				text : "<spring:message code="Childnodedeletion" />",
				showCancelButton : true,
				type : 'warning',
				call : function() {
					$.post(form_url, {
						id : id
					}, function(result) {
						loadclassTree();
						$.BOOT.toast1(result);
					}, 'json');
				}
			};
			$.BOOT.alert(json, true);
		});
		$.BOOT.click(".class_edit", function(c) {
			var id = $(c).attr("val");
			var href = $.webapp.root + '/admin/system/class/class_form_UI.do?id=';
			href += (id ? id : "");
			$.BOOT.page("content_addclass", href, function() {
				$('#addclassModal').modal('toggle');
			});
		});
		$.BOOT.click("#class_edit_idd", function(c) {
			var href = $.webapp.root + '/admin/system/class/class_form_UI.do';
			$.BOOT.page("content_addclass", href, function() {
				$('#addclassModal').modal('toggle');
			});
		});
		$.BOOT.click(".class_add", function(c) {
			var id = $(c).attr("val");
			var href = $.webapp.root + '/admin/system/class/class_form_UI.do?pid=';
			href += (id ? id : "");
			$.BOOT.page("content_addclass", href, function() {
				$('#addclassModal').modal('toggle');
			});
		});
	});
	
</script>