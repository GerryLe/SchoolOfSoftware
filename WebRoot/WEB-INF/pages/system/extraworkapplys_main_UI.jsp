<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:include page="/template/modal.jsp"><jsp:param value="addExtraworkapplys" name="id" /><jsp:param value="加班申请单" name="title" /></jsp:include>
<section class="content">
	<div id="Extraworkapplys-form-id"></div>
	<div id="filter-bar">
		<div class="btn-toolbar">
			<button type="button" class="btn btn-primary " id="Extraworkapplys_add"><spring:message code="add" /></button>
		</div>
	</div>
	<table id="Extraworkapplys_table" class="table-condensed table table-hover" data-row-style="rowStyle" data-side-pagination="server"></table>
</section>
<script type="text/javascript">
	var $holidayapplys_table;
	$(function() {
		$Extraworkapplys_table = $.BOOT.table("Extraworkapplys_table", $.webapp.root
				+ "/admin/system/Extraworkapplys/select.do", {
			columns : [ 
			    {
				field : 'positionname',
				title : '部门名称',
				width : 120,
			},{
				field : 'extworkapplyname',
				title : '加班申请人',
				width : 120,
			}, {
				field : 'extworkapplycontent',
				title : '加班申请事由',
				width : 150,
			}, {
				field : 'extworkapplydays',
				title : '加班申请天数',
				width : 30,
			}, {
				field : 'extworkapplystartdata',
				title : '加班开始日期',
			}, {
				field : 'extworkapplyenddata',
				title : '加班结束日期',
			}, {
				field : 'extworkdirectorsapproval',
				title : '部门主管审批',
				width : 80,
				sortable : true,
				formatter : function(value, row) {
					if (value == "0") {
						return "<spring:message code="Waitingforapproval" />";
					} else if(value == "1") {
						return "<spring:message code="through" />";
					}else {
						return "<spring:message code="Notthrough" />";
					}
				}
			}, {
				field : 'extworkhrapproval',
				title : '人事审批',
				width : 80,
				sortable : true,
				formatter : function(value, row) {
					if (value == "0") {
						return "<spring:message code="Waitingforapproval" />";
					} else if(value == "1") {
						return "<spring:message code="through" />";
					}else {
						return "<spring:message code="Notthrough" />";
					}
				}
			},  {
				field : 'extworkapplystatement',
				title : '加班申请状态',
				width : 80,
				sortable : true,
				formatter : function(value, row) {
					if (value == "0") {
						return "<spring:message code="Inthereview" />";
					} else if(value == "1") {
						return "<spring:message code="through" />";
					}else if(value == "3") {
						return "<spring:message code="undo" />";
					}else {
						return "<spring:message code="Notthrough" />";
					}
				}
			}, {
				field : 'extworkremark',
				title : '<spring:message code="note" />',
				width : 120,
			}, {
				field : 'id',
				title : '<spring:message code="operation" />',
				width : 70,
				formatter : function(value, row, index) {
					return $.BOOT.groupbtn(value, [ {
						cla : 'Extraworkapplys_true',
						text : "<spring:message code="approval" />"
					}, {
						cla : 'Extraworkapplys_false',
						text : "<spring:message code="Refusedto" />"
					}, {
						cla : 'Extraworkapplys_delete',
						text : "<spring:message code="undo" />"
					} ]);
				}
			} ],
			paginationInfo : true,
			showExport : true,
			search : true
		});
		$("input.form-control").attr('placeholder','英文名');
	});
	function rowStyle(row, index) {
		if (row.status == "1") {
			return {
				classes : 'danger'
			};
		}
		return {};
	}
	$.BOOT.click(".Extraworkapplys_delete", function(c) {
		var id = $(c).attr("val");
		var json = {
			title : "",
			text : "<spring:message code="Determinetocanceltheapplication" />",
			showCancelButton : true,
			type : 'warning',
			call : function() {
				var href = $.webapp.root + '/admin/system/Extraworkapplys/delete.do';
				$.post(href, {
					ids : id
				}, function(result) {
					$Extraworkapplys_table.bootstrapTable('refresh');
					$.BOOT.toast1(result);
				}, 'json');
			}
		};
		$.BOOT.alert(json, true);
	});
	$.BOOT.click(".Extraworkapplys_edit", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/Extraworkapplys/holidayapply_form_UI.do?id='
				+ id;
		$.BOOT.page("content_addExtraworkapplys", href, function() {
			$('#addExtraworkapplysModal').modal('toggle');
		});
	});
	$(document).on("click", "#Extraworkapplys_add", function() {
		var href = $.webapp.root + '/admin/system/Extraworkapplys/extraworkapplys_form_UI.do';
		$.BOOT.page("content_addExtraworkapplys", href, function() {
			$('#addExtraworkapplysModal').modal('toggle');
		});
	});
	$.BOOT.click(".Extraworkapplys_true", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/Extraworkapplys/true.do';
		$.post(href, {
			id : id
		}, function(result) {
			$Extraworkapplys_table.bootstrapTable('refresh');
			$.BOOT.toast1(result);
		}, 'json');
	});
	$.BOOT.click(".Extraworkapplys_false", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/Extraworkapplys/false.do';
		$.post(href, {
			id : id
		}, function(result) {
			$Extraworkapplys_table.bootstrapTable('refresh');
			$.BOOT.toast1(result);
		}, 'json');
	});
</script>
