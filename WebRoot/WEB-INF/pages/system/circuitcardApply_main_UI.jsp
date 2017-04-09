<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/template/modal.jsp"><jsp:param value="circuitcard" name="id" /><jsp:param value="申请信息" name="title" /></jsp:include>
<style>
<!--
#uploadUser {
	font-size: 14px;
	overflow: hidden;
	position: absolute;
	margin-left: 65px;
}

#fileInput {
	position: absolute;
	z-index: 100;
	margin-left: -280px;
	font-size: 60px;
	opacity: 0;
	filter: alpha(opacity = 0);
	margin-top: -5px;
}

/* .fixed-table-header{
   margin-right: 0px;
   display:none;
}
.table .btn-group{
  position:relative;
}
.fixed-table-container{
    height: 100%;
} */
-->
</style>
<section class="content">
	<div id="person-form-id"></div>
	<div id="filter-bar">
	     <div class="btn-toolbar">
			<button type="button" class="btn btn-primary " id="circuitcard_add">申请</button>
		</div>
	</div>
	<table id="person_table" class="table-condensed table table-hover" data-row-style="rowStyle" data-side-pagination="server"></table>
</section>
<script type="text/javascript">
	var $person_table;
	$(function() {
		var roledefault=null;
		$.post('/admin/system/circuitcard/roleDefault.do',{},function(result){
			roledefault=result;
		});
		$person_table = $.BOOT.table("person_table", $.webapp.root
				+ "/admin/system/circuitcard/select.do", {
			columns : [  {
				field : 'name',
				title : '英文名',
				sortable : true
			}, {
				field : 'chinaname',
				title : '中文名',
			}, {
				field : 'sex',
				title : '性别',
			}, {
				field : 'phone',
				title : '手机号码',
			},  {
				field : 'telePhone',
				title : '座机号码',
			},  {
				field : 'orgName',
				title : '部门',
			}, {
				field : 'positionName',
				title : '职位',
			}, {
				field : 'email',
				title : '邮箱',
			},{
				field : 'applyDate',
				title : '申请日期',
			},{
				field : 'orgChargeApprove',
				title : '上级审批',
				sortable : true,
				formatter : function(value, row) {
					if (value == 0) {
						return "审核中";
					} else if(value == 1) {
						return "已通过";
					}else {
						return "不通过";
					}
				}
			},{
				field : 'applyStatus',
				title : '申请状态',
				sortable : true,
				formatter : function(value, row) {
					if (value == 0) {
						return "申请中";
					} else if(value == 1) {
						return "审批通过，等待印制";
					}else if(value == 2) {
						return "完成";
					}else if(value == 3) {
						return "审批不通过";
					}else {
						return "撤销";
					}
				}
			},{
				field : 'id',
				title : '操作',
				formatter : function(value, row, index) {
					if(roledefault==3){
						return $.BOOT.groupbtn(value, [ {
							cla : 'circuitcard_get',
							text : "查看"
						},{
							cla : 'circuitcard_complete',
							text : "完成"
						}]);
					}else if(roledefault==2||roledefault==4||roledefault==5){
						return $.BOOT.groupbtn(value, [ {
							cla : 'circuitcard_get',
							text : "查看"
						},{
							cla : 'circuitcard_true',
							text : "批准"
						}, {
							cla : 'circuitcard_false',
							text : "拒绝"
						}]);
					}else if(roledefault==13){
						return $.BOOT.groupbtn(value, [ {
							cla : 'circuitcard_edit',
							text : "编辑"
						},{
							cla : 'circuitcard_complete',
							text : "完成"
						},{
							cla : 'circuitcard_delete',
							text : "撤销"
						}]);
					}else{
						return $.BOOT.groupbtn(value, [ {
							cla : 'circuitcard_edit',
							text : "编辑"
						},{
							cla : 'circuitcard_delete',
							text : "撤销"
						}]);
					}
				}
			} ],
			paginationInfo : true,
			showExport : true
		});

		$('#filter-bar').bootstrapTableFilter({
			filters : [ {
				field : 'name',
				label : '英文名',
				type : 'search'
			}, {
				field : 'chinaname',
				label : '中文名',
				type : 'search'
			}],
			connectTo : '#person_table'
		});console.info($("tr"))
	});
	
	setTimeout(//延迟0.5秒
			  function(){  
					$(document).on("dblclick","#person_table>tbody>tr",function(){
						$("#person_table tr").css('background-color','');
						$(this).css('background-color','#D0D0D0');
					    //var id = $(this).find("input").val()
					    var id = $(this).find(".btn-group").find("ul li:first").attr("val");
					    var href = $.webapp.root + '/admin/system/circuitcard/circuitcardApply_form_UI.do?id='
						+ id+'select';
						$.BOOT.page("content_circuitcard", href, function() {
						$('#circuitcardModal').modal('toggle');
				});
				   });
		     },500);
	
	function rowStyle(row, index) {
		if (row.status == "1") {
			return {
				classes : 'danger'
			};
		}
		return {};
	}
	
	$.BOOT.click(".circuitcard_complete", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/circuitcard/complete.do';
		$.post(href, {
			id : id
		}, function(result) {
			$person_table.bootstrapTable('refresh');
			$.BOOT.toast1(result);
		}, 'json');
	});
	
	$.BOOT.click(".circuitcard_delete", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/circuitcard/delete.do';
		$.post(href, {
			id : id
		}, function(result) {
			$person_table.bootstrapTable('refresh');
			$.BOOT.toast1(result);
		}, 'json');
	});
	
	$.BOOT.click(".circuitcard_get", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/circuitcard/circuitcardApply_form_UI.do?id='
				+ id+'select';
		$.BOOT.page("content_circuitcard", href, function() {
			$('#circuitcardModal').modal('toggle');
		});
	});


	
	$(document).on("click","#circuitcard_add",function(){
		var href = $.webapp.root + '/admin/system/circuitcard/circuitcardApply_form_UI.do';
		$.BOOT.page("content_circuitcard", href, function() {
			$('#circuitcardModal').modal('toggle');
        });
	});
	
 	$.BOOT.click(".circuitcard_edit",function(c){
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/circuitcard/circuitcardApply_form_UI.do?id='
				+ id;
		$.BOOT.page("content_circuitcard", href, function() {
			$('#circuitcardModal').modal('toggle');
		}); 
		
	}); 
	
	$.BOOT.click(".circuitcard_true", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/circuitcard/circuitcardRemark_form_UI.do?id='
				+ id+'true';
		$.BOOT.page("content_circuitcard", href, function() {
			$('#circuitcardModal').modal('toggle');
		});
	});
	
	$.BOOT.click(".circuitcard_false", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/circuitcard/circuitcardRemark_form_UI.do?id='
				+ id;
		$.BOOT.page("content_circuitcard", href, function() {
			$('#circuitcardModal').modal('toggle');
		}); 
	});
</script>