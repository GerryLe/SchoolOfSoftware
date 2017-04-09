<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/template/modal.jsp"><jsp:param value="addremark" name="id" /><jsp:param value="转正信息" name="title" /></jsp:include>
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

#person_table{
  margin-top: 0px !important;;  
}
.fixed-table-header{
   display:none !important;;
} 
.table .btn-group{
   position:relative; 
}
-->
</style>
<section class="content">
	<div id="person-form-id"></div>
	<div id="filter-bar"></div>
	<table id="person_table" class="table-condensed table table-hover" data-row-style="rowStyle" data-side-pagination="server"></table>
</section>
<script type="text/javascript">
	var $person_table;
	var resultFlag;
	var getRoleId = $.webapp.root + "/admin/system/probation/getCurrentUserDefaultRole.do";
	$(function() {
		var func=null;
    	 $.post(getRoleId, {}, function(result) {
    		            resultFlag=result;
					    if(result==0){
					    	$("#permit_addremark").show();
					    	$("#refuse_addremark").show();
					    	/* 超级管理员 */
					    	func=[ {
								cla : 'probation_select',
								text : "查看"
							}];
					    }else if(result==4||result==2){
					    	/* 部门主管或部门负责人 */
					    	$("#permit_addremark").show();
					    	$("#refuse_addremark").show();
					    	func=[ {
								cla : 'probation_select',
								text : "查看"
							}];
					    }else if(result==3){
					    	/* 人事部hr */
					    	func=[ {
								cla : 'probation_select',
								text : "查看"
							}, {
								cla : 'probation_notice',
								text : "邮件通知"
							}];
					    }else if(result==23||result==43){
					    	/* 部门主管或者部门总监及人事部hr */
					    	$("#permit_addremark").show();
					    	$("#refuse_addremark").show();
					    	func=[ {
								cla : 'probation_select',
								text : "查看"
							},{
								cla : 'probation_notice',
								text : "邮件通知"
							}];
					    }else{
					    	/* 普通用户 */
					    	func=[ {
								cla : 'probation_select',
								text : "查看"
							}];
					    }
				}, 'json'); 
		$person_table = $.BOOT.table("person_table", $.webapp.root
				+ "/admin/system/probation/select.do", {
			columns : [  {
				field : 'account',
				title : '帐号',
				sortable : true
			}, {
				field : 'name',
				title : '英文名',
				sortable : true
			}, {
				field : 'chinaname',
				title : '中文名',
			}, {
				field : 'sex',
				title : '性别',
			},  {
				field : 'area',
				title : '地区',
			}, {
				field : 'phone',
				title : '手机',
			},/*  {
				field : 'orgName',
				title : '部门',
			}, */ {
				field : 'orgChildName',
				title : '部门',
			}, {
				field : 'positionName',
				title : '岗位',
			}, {
				field : 'employmentStr',
				title : '入职日期',
			},{
				field : 'probationLimit',
				title : '试用期限',
			},{
				field : 'probationEnd',
				title : '试用到期',
			},{
				field : 'becomeStaffDate',
				title : '试用转正日期',
				sortable : true
			},{
				field : 'orgChargeApprove',
				title : '部门主管审批',
				sortable : true,
				formatter : function(value, row) {
					if (value == 0) {
						return "审核中";
					} else if(value == 1) {
						return "通过";
					}else {
						return "不通过";
					}
				}
			}, {
				field : 'orgHeadApprove',
				title : '部门负责人',
				sortable : true,
				formatter : function(value, row) {
					if (value == 0) {
						return "审核中";
					} else if(value == 1) {
						return "通过";
					}else {
						return "不通过";
					}
				}
			},{
				field : 'approveStatus',
				title : '审批状态',
				sortable : true,
				formatter : function(value, row) {
					if (value == 0) {
						return "等待审核";
					} else if(value == 1) {
						return "通过";
					}else {
						return "不通过";
					}
				}
			},{
				field : 'hrNotice',
				title : '通知转正审批',
				sortable : true,
				formatter : function(value, row) {
					if (value == 1) {
						return "已通知";
					} else {
						return "未通知";
					}
				}
			},{
				field : 'id',
				title : '操作',
				formatter : function(value, row, index) {
					return $.BOOT.groupbtn(value,func);
				}
			} ],
			paginationInfo : true,
			showExport : true,
			onDblClickRow:function(row, $element){
				onDbClick(row, $element);
			}
		});

		var onDbClick=function(row, $element){ 
			$("#holidaysuser_table tr").css('background-color','');
			$(this).css('background-color','#D0D0D0');
			 var href = $.webapp.root + '/admin/system/probation/completeProbation_form_UI.do?id='
				+ row.id+' '+resultFlag;
				$.BOOT.page("content_addremark", href, function() {
					$('#addremarkModal').modal('toggle');
				});
        }
		$('#filter-bar').bootstrapTableFilter({
			filters : [ {
				field : 'name',
				label : '英文名',
				type : 'search'
			}, {
				field : 'chinaname',
				label : '中文名',
				type : 'search'
			},{
				field : 'becomeStaffDate',
				label : '转正日期',
				type : 'search'
			} ],
			connectTo : '#person_table'
		});console.info($("tr"))
	});
	function rowStyle(row, index) {
		if (row.status == "1") {
			return {
				classes : 'danger'
			};
		}
		return {};
	}
	
	
	
	/*  setTimeout(
			  //延迟0.2秒
			  function(){ 
				    
					$(document).on("dblclick","#person_table>tbody>tr",function(){
						$("#person_table tr").css('background-color','');
						$(this).css('background-color','#D0D0D0');
						var id = $(this).find(".btn-group").find("ul li:first").attr("val");
					    var href = $.webapp.root + '/admin/system/probation/completeProbation_form_UI.do?id='
						+ id+' '+resultFlag;
						$.BOOT.page("content_addremark", href, function() {
							$('#addremarkModal').modal('toggle');
						});
				   });
		     },200); */
	
	/* $.BOOT.click(".probation_notice", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/probation/notice.do';
		$.post(href, {
			id : id
		}, function(result) {
			$person_table.bootstrapTable('refresh');
			$.BOOT.toast1(result);
		}, 'json');
	}); */
	
	
	$.BOOT.click(".probation_notice", function(c) {
		var id = $(c).attr("val");
		var json = {
			title : "",
			text : "确定发送邮件吗?",
			showCancelButton : true,
			type : 'warning',
			call : function() {
				var href = $.webapp.root + '/admin/system/probation/notice.do';
				$.post(href, {
					id : id
				}, function(result) {
					$person_table.bootstrapTable('refresh');
					$.BOOT.toast1(result);
				}, 'json');
			}
		};
		$.BOOT.alert(json, true);
	});
	
	$.BOOT.click(".probation_select", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/probation/completeProbation_form_UI.do?id='
				+ id;
		$.BOOT.page("content_addremark", href, function() {
			$('#addremarkModal').modal('toggle');
		});
	});

	$.BOOT.click(".probation_true", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/probation/applyRemark_form_UI.do?id='
				+ id+'true';
		
		$.BOOT.page("content_addremark", href, function() {
			$('#addremarkModal').modal('toggle');
		});
	});
	
	$.BOOT.click(".probation_false", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/probation/applyRemark_form_UI.do?id='
				+ id;
		$.BOOT.page("content_addremark", href, function() {
			$('#addremarkModal').modal('toggle');
		}); 
	});
</script>