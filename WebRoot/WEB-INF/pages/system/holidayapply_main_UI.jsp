 <%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:include page="/template/modal.jsp"><jsp:param value="addholidayapplys" name="id" /><jsp:param value="请假申请" name="title" /></jsp:include>

<section class="content">
	<div id="holidayapplys-form-id"></div>
	
	<div id="filter-bar">
		<div class="btn-toolbar">
			<button type="button" class="btn btn-primary " id="holidayapplys_add"><spring:message code="Toapplyforvacation" /></button>
		</div>
	</div>
	<table id="holidayapplys_table" class="table-condensed table table-hover" data-row-style="rowStyle" data-side-pagination="server" data-search="true"></table>
</section>
<script type="text/javascript">
	var $holidayapplys_table;
	$(function() {
		$holidayapplys_table = $.BOOT.table("holidayapplys_table", $.webapp.root
				+ "/admin/system/holidayapplys/holidayApplysdg.do", {
			columns : [ {
				field : 'class_name',
				title : '班级名称',
				width : 120,
			},{
				field : 'account',
				title : '学号',
				width : 120,
			},{
				field : 'holiapplyUserName',
				title : '姓名',
				width : 120,
			}, {
				field : 'holiapplyName',
				title : '申请类型',
			}, {
				field : 'holiapplyContent',
				title : '申请事由',
			}, {
				field : 'holiapplyDays',
				title : '申请天数',
				width : 30,
			}, {
				field : 'holiapplyStartDate',
				title : '开始日期',
				width : 150,
				sortable : true,
			}, {
				field : 'holiapplyEndDate',
				title : '结束日期',
				width : 150,
			},{
				field : 'director',
				title : '审批人',
				width : 150,
			},{
				field : 'holiapplydirectorsapproval',
				title : '审批状态',
				width : 80,
				sortable : true,
				class:'apploveDirector',
				formatter : function(value, row, index) {
					if (value == "0") {
					   $(".apploveDirector:contains('等待审批')").css({color: "blue"});
						return "等待审批";
					} else if(value == "1") {
						return "通过";
					}else {
					   $(".apploveDirector:contains('不通过')").css({color: "red"});
						return "不通过";
					}
				}
			}, {
				field : 'holiapplystatement',
				title : '申请状态',
				width : 80,
				sortable : true,
				class:'apploveStatement',
				formatter : function(value, row) {
					if (value == "0") {
						$(".apploveStatement:contains('审核中')").css({color: "blue"});
						return "审核中";
					} else if(value == "1") {
						return "通过";
					}else if(value == "3") {
						$(".apploveStatement:contains('撤销')").css({color: "red"});
						return "撤销";
					}else {
						$(".apploveStatement:contains('不通过')").css({color: "red"});
						return "不通过";
					}
				}
			}, {
				field : 'holiapplyremark',
				title : '备注',
				width : 250,
			}, {
				field : 'id',
				title : '操作',
				width : 70,
				formatter : function(value, row, index) {
					return "<button type='button' class='btn btn-default fa' onclick='repeal(this)' id='"+value+"'>撤销</button>";
				}
			}  ],
			paginationInfo : true,
			showExport : true,
			search : true,
			onDblClickRow : function(row,$element){
				$("#holidayapplys_table tr").css('background-color','');
				$element.css('background-color','#D0D0D0');
				var id = row.id;
				var href = $.webapp.root + '/admin/system/holidayapplys/holidayapply_form_UI.do?id='
						+ id;
				$.BOOT.page("content_addholidayapplys", href, function() {
					$('#addholidayapplysModal').modal('toggle');
				});
			}
		});
		$("input.form-control").attr('placeholder','姓名');
	});
	
	    
	function rowStyle(row, index) {
		if (row.status == "1") {
			return {
				classes : 'danger'
			};
		}
		return {};
	}
	function repeal(c){
		var id = $(c).attr('id');
		var json = {
			title : "",
			text : "确定删除该记录吗?",
			showCancelButton : true,
			type : 'warning',
			call : function() {
				var href = $.webapp.root
						+ '/admin/system/holidayapplys/delete.do';
				$.post(href, {
					id : id
				}, function(result) {
					$.BOOT.toast1(result);
					$holidayapplys_table.bootstrapTable('refresh');
				}, 'json');
			}
		};
		$.BOOT.alert(json, true);
	}
	
	$(document).on("click", "#holidayapplys_add", function() {
		var href = $.webapp.root + '/admin/system/holidayapplys/holidayapply_form_UI.do';
		$.BOOT.page("content_addholidayapplys", href, function(data) {
			$('#addholidayapplysModal').modal('toggle');
		});
	});
	$.BOOT.click("#holidayapplys_get", function() {
		var href = $.webapp.root + '/admin/system/holidayapplys/cha.do';
		$.post(href,function(result) {
			$holidayapplys_table.bootstrapTable('refresh');
			$.BOOT.toast1(result);
		}, 'json');
	});
	
</script>