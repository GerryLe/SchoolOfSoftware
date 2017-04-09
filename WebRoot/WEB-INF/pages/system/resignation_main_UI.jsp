<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<jsp:include page="/template/modal.jsp"><jsp:param
		value="resignation" name="id" /><jsp:param value="离职审核" name="title" /></jsp:include>
<section class="content">
	<div id="filter-bar">
		<div class="btn-toolbar">
			<button type="button" class="btn btn-primary " id="resignation_add">离职申请/修改</button>
		</div>

	</div>
	<table id="resignation_table" class="table-condensed table table-hover"
		data-row-style="rowStyle" data-side-pagination="server"></table>
</section>

<script>
	var $resignation_table;
	$(function() {
		$resignation_table = $.BOOT.table("resignation_table", $.webapp.root
				+ "/admin/system/resignation/datagridresignation.do", {
			columns : [ {
				field : 'chinaname',
				title : '中文名称',
			}, {
				field : 'name',
				title : '英文名',
			}, {
				field : 'account',
				title : '员工编号',
			}, {
				field : 'area',
				title : '地区',
			}, {
				field : 'childOrgName',
				title : 'team',
			}, {
				field : 'parentOrgName',
				title : '部门',
			}, {
				field : 'positionName',
				title : '岗位',
			}, {
				field : 'workingAge',
				title : '工龄',
			}, {
				field : 'employmentDate',
				title : '入职日期',
			}, {
				field : 'predictResignationDate',
				title : '预计离职日期',
			}, {
				field : 'resignationReason',
				title : '离职理由',
			}, {
				field : 'id',
				title : '操作',
				formatter : function(value, row, index) {
					return $.BOOT.groupbtn(value, [ {
						cla : 'audit_edit',
						text : '审核'
					}, {
						cla : 'check_opinion',
						text : "详情"
					}, {
						cla : 'revocation',
						text : "撤销"
					} ]);
				}
			} ],
			paginationInfo : true,
			showExport : true
		});
		$('#filter-bar').bootstrapTableFilter({
			filters : [ {
				field : 'chinaname',
				label : '中文名称',
				type : 'search'
			}, {
				field : 'name',
				label : '英文名',
				type : 'search'
			}, {
				field : 'childOrgName',
				label : 'team',
				type : 'search'
			} ],
			connectTo : '#resignation_table'
		});
	});
	function rowStyle(row, index) {
		if (row.status == "1") {
			return {
				classes : 'danger'
			};
		}
		return {};
	}
	$.BOOT.click(".revocation", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/resignation/revocation.do?id='+id;
		$.get(href,function(result){
			$.BOOT.toast1(result);
			$resignation_table.bootstrapTable('refresh');
			modalresignation();
		},"json")
	});
	$.BOOT.click(".check_opinion", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/resignation/resignationauditcheck_form_UI.do?id='+id;
		$.BOOT.page("content_resignation",href,
				function() {
					$('#resignationModal').modal('toggle');
				});
	});
	$.BOOT.click(".audit_edit",
			function(c) {
				var id = $(c).attr("val");
				var check = $.webapp.root + '/admin/system/resignation/getcheck.do?id=' + id;
					$.get(check,function(result) {
									if (result) {
										var href = $.webapp.root+ '/admin/system/resignation/resignationauditadd_form_UI.do?id='+ id;
										$.BOOT.page("content_resignation",href,
											function() {
												$('#resignationModal').modal('toggle');
											});
									} else {
										$.BOOT.toast(false, '权限不足');
									}
					}, "json")
			});
	$.BOOT.click("#resignation_add",
			function() {
				var href = $.webapp.root
						+ '/admin/system/resignation/resignation_form_UI.do';
				$.BOOT.page("content_resignation", href, function() {
					$('#resignationModal').modal('toggle');
				})
			});
	
</script>