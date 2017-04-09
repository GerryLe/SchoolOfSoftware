<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<jsp:include page="/template/modal.jsp"><jsp:param
		value="addstationeries" name="id" /><jsp:param value="添加文具"
		name="title" /></jsp:include>
<style>

#fileInput {
	position: absolute;
	width: 82px;
	height: 34px;
	left: 92px;
	opacity: 0;
	filter: alpha(opacity = 0);
}
</style>
<section class="content">
	<div id="filter-bar" >
		<form id="form-import">
		<div class="btn-toolbar">
			<button type="button" class="btn btn-primary " id="stationeries_add">添加文具</button>
			<input type="button" class="btn btn-primary" id="bulk_import" value="导入信息" />
			<input type="file" id="fileInput" class="fileInput" size="1" name="uploadFile" onchange="bulkImport()" />
		</div>
		</form>
	</div>
	<table id="stationeries_table"
		class="table-condensed table table-hover" data-row-style="rowStyle"
		data-side-pagination="server"></table>
</section>
<script>
	var $stationeries_table;
	$(document)
			.on(
					"click",
					"#stationeries_add",
					function() {
						var href = $.webapp.root
								+ '/admin/system/stationeries/hrstationeries_form_UI.do';
						$.BOOT.page("content_addstationeries", href,
								function() {
									$('#addstationeriesModal').modal('toggle');
								});
					});
	$(function() {
		$stationeries_table = $.BOOT.table("stationeries_table", $.webapp.root
				+ "/admin/system/stationeries/datagridstationeries.do", {
			columns : [ {
				field : 'stationeryName',
				title : '文具名称',
				sortable : true,
			}, {
				field : 'stationeryType',
				title : '文具类型',
			}, {
				field : 'stationeryUnit',
				title : '文具单位',
			}, {
				field : 'price',
				title : '单价',
				sortable : true,
			}, {
				field : 'remark',
				title : '备注',
			}, {
				field : 'id',
				title : '操作',
				formatter : function(value, row, index) {
					return $.BOOT.groupbtn(value, [ {
						cla : 'stationeries_edit',
						text : "编辑"
					}, {
						cla : 'stationeries_delete',
						text : "删除"
					} ]);
				}
			} ],
			paginationInfo : true,
			showExport : true,
			search : true,
			searchAlign : 'left'
		});
		$("input.form-control").attr('placeholder','文具名称');
	});

	$.BOOT.click(".stationeries_edit", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root
				+ '/admin/system/stationeries/hrstationeries_form_UI.do?id='
				+ id;
		$.BOOT.page("content_addstationeries", href, function() {
			$('#addstationeriesModal').modal('toggle');
		});
	});
	$.BOOT.click(".stationeries_delete", function(c) {
		var id = $(c).attr("val");
		var json = {
			title : "",
			text : "确定删除该记录吗?",
			showCancelButton : true,
			type : 'warning',
			call : function() {
				var href = $.webapp.root
						+ '/admin/system/stationeries/deletestationeries.do';
				$.post(href, {
					id : id
				}, function(result) {
					$.BOOT.toast1(result);
					$stationeries_table.bootstrapTable('refresh');
				}, 'json');
			}
		};
		$.BOOT.alert(json, true);
	});
	function rowStyle(row, index) {
		if (row.status == "1") {
			return {
				classes : 'danger'
			};
		}
		return {};
	}
	function bulkImport(){
		var json = {
				title : "",
				text : "批量导入会拆除原先记录，请确定",
				showCancelButton : true,
				type : 'warning',
				call : function() {
					var options = {
							url : '/admin/system/stationeries/bulkImport.do',
							type : 'post',
							dataType : 'text',
							success : function(data) {
								$stationeries_table.bootstrapTable('refresh');
								$.BOOT.toast1(result);
							}
						};
					$("#form-import").ajaxSubmit(options);
				}
			};
			$.BOOT.alert(json, true);
	}
</script>