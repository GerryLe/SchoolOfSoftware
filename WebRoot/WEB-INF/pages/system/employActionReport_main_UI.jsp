<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/template/modal.jsp"><jsp:param value="addEmploy" name="id" /><jsp:param value="申请表" name="title" /></jsp:include>

<section class="content">
	<div id="employ-form-id"></div>
	<div id="filter-bar">
		<div class="btn-toolbar">
			<button type="button" class="btn btn-primary " id="employ_add">申请</button>
			<button type="button" class="btn btn=primary" id="daoWord" onClick="AllAreaWord('#employ_table')">导出</button>
		</div> 
	</div>
	<table id="employ_table" class="table-condensed table tablse-hover" data-row-style="rowStyle" data-side-pagination="server"></table>
</section>

<script type="text/javascript">
	var $employ_table;
	$(function() {
		$employ_table = $.BOOT.table("employ_table", $.webapp.root
				+ "/admin/system/employ/select.do", {
				columns : [ {
					field : 'applyName',
					title : '申请人姓名',
				},{
					field : 'applyAreaAndorg',
					title : '申请部门',
				}, {
					field : 'applyPosition',
					title : '申请职位',
				}, {
					field : 'applyGenre',
					title : '申内部级别',
				}, {
					field : 'applyPayment',
					title : '申请薪金',
				}, {
					field : 'applySubsidy',
					title : '申请补贴',
				}, {
					field : 'planComeDate',
					title : '预算入职日期',
				},{
					field : 'orgChargeApply',
					title : '部门主管审批',
					sortable : true,
					formatter : function(value, row) {
						if (value == "0") {
							return "审核中";
						} else if(value == "1") {
							return "已通过";
						}else {
							return "未通过";
						}
					}
				}, {
					field : 'superiorApply',
					title : '直接上司审批',
					sortable : true,
					formatter : function(value, row) {
						if (value == "0") {
							return "审核中";
						} else if(value == "1") {
							return "已通过";
						}else {
							return "未通过";
						}
					}
				}, {
					field : 'managerApply',
					title : '当地总经理审批',
					sortable : true,
					formatter : function(value, row) {
						if (value == "0") {
							return "审核中";
						} else if(value == "1") {
							return "已通过";
						}else {
							return "未通过";
						}
					}
				},{
					field : 'bossApply',
					title : '总裁审批',
					sortable : true,
					formatter : function(value, row) {
						if (value == "0") {
							return "审核中";
						} else if(value == "1") {
							return "已通过";
						}else {
							return "未通过";
						}
					}
				}, {
					field : 'applyStatement',
					title : '招聘申请状态',
					sortable : true,
					formatter : function(value, row) {
						if (value == "0") {
							return "暂未通过";
						} else if(value == "1") {
							return "已通过";
						}else if(value == "2"){
							return "不通过";
						}else{
							return "撤销";
						}
						}
				},{
					field : 'id',
					title : '操作',
					formatter : function(value, row, index) {
						return $.BOOT.groupbtn(value, [{
							cla : 'employ_edit',
							text : "查看"
						}, {
							cla : 'employ_true',
							text : "批准"
						}, {
							cla : 'employ_false',
							text : "拒绝"
						}, {
							cla : 'employ_delete',
							text : "撤销"
						} ]);
					}
				} ],
			paginationInfo : true,
			showExport : true
		});
		
		$('#filter-bar').bootstrapTableFilter({
			filters : [ {
				field : 'apploNname',
				label : '申请人姓名',
				type : 'search'
			  } ],
			connectTo : '#employ_table'
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
	$.BOOT.click(".employ_delete", function(c) {
		var id = $(c).attr("val");
		var json = {
			title : "",
			text : "确定要撤销吗?",
			showCancelButton : true,
			type : 'warning',
			call : function() {
				var href = $.webapp.root + '/admin/system/employ/delete.do';
				$.post(href, {
					ids : id
				}, function(result) {
					$employ_table.bootstrapTable('refresh');
					$.BOOT.toast1(result);
				}, 'json');
			}
		};
		$.BOOT.alert(json, true);
	});
	$(document).on("click", "#employ_add", function() {
		var href = $.webapp.root + '/admin/system/employ/employActionReport_form_UI.do';
		$.BOOT.page("content_addEmploy", href, function() {
			$('#addEmployModal').modal('toggle');
		});
	});
	$.BOOT.click(".employ_edit", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/employ/employActionReport_form_UI.do?id='
				+ id;
		$.BOOT.page("content_addEmploy", href, function() {
			$('#addEmployModal').modal('toggle');
		});
	});
	$.BOOT.click(".employ_true", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/employ/true.do';
		$.post(href, {
			id : id
		}, function(result) {
			$employ_table.bootstrapTable('refresh');
			$.BOOT.toast1(result);
		}, 'json');
	});
	$.BOOT.click(".employ_false", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/employ/false.do';
		$.post(href, {
			id : id
		}, function(result) {
			$employ_table.bootstrapTable('refresh');
			$.BOOT.toast1(result);
		}, 'json');
	}); 
	
	 function AllAreaWord(tableid)
	 {

	 var PrintA= document.getElementById(tableid); 
	 var oWD = new ActiveXObject("Word.Application");
	 var oDC = oWD.Documents.Add("",0,1);
	 var oRange =oDC.Range(0,1);
	 var sel = document.body.createTextRange();
	 sel.moveToElementText(PrintA);
	 sel.select();
	 sel.execCommand("Copy");
	 oRange.Paste();
	 oWD.Application.Visible = true;
	 }
</script>