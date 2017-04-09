 <%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:include page="/template/modal.jsp"><jsp:param value="addholidayapplys" name="id" /><jsp:param value="休假申请单" name="title" /></jsp:include>

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
				field : 'stu_no',
				title : '学号',
				width : 120,
			},{
				field : 'stu_name',
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
			}, {
				field : 'holiapplydirectorsapproval',
				title : '审批人',
				width : 80,
				sortable : true,
				class:'apploveDirector',
				formatter : function(value, row) {
					if (value == "0") {
					   $(".apploveDirector:contains('<spring:message code="Waitingforapproval" />')").css({color: "#ff0011"});
						return "<spring:message code="Waitingforapproval" />";
					} else if(value == "1") {
						$(this).attr("color","red");
						return "<spring:message code="through" />";
					}else {
						$(this).attr("color","red");
						return "<spring:message code="Notthrough" />";
					}
				}
			},{
				field : 'holiapplystatement',
				title : '申请状态',
				width : 80,
				sortable : true,
				class:'apploveStatement',
				formatter : function(value, row) {
					if (value == "0") {
						$(".apploveStatement:contains('<spring:message code="Inthereview" />')").css({color: "#ff0011"});
						return "<spring:message code="Inthereview" />";
					} else if(value == "1") {
						return "<spring:message code="through" />";
					}else if(value == "3") {
						return "<spring:message code="undo" />";
					}else {
						$(".apploveStatement:contains('<spring:message code="Notthrough" />')").css({color: "#ff0011"});
						return "<spring:message code="Notthrough" />";
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
					return "<button type='button' class='btn btn-default fa' onclick='repeal(this)' id='"+value+"'><spring:message code='undo' /></button>";
				}
			} ],
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
		$("input.form-control").attr('placeholder','英文名');
	});
	
	
	/*  setTimeout(
			  //延迟0.2秒
			  function(){ 
					$(document).on("dblclick","#holidayapplys_table>tbody>tr",function(){
						$("#holidayapplys_table tr").css('background-color','');
						$(this).css('background-color','#D0D0D0');
						var id = $(this).find(".btn-group").find("ul li:first").attr("val");
						var href = $.webapp.root + '/admin/system/holidayapplys/holidayapply_form_UI.do?id='
								+ id;
						$.BOOT.page("content_addholidayapplys", href, function() {
							$('#addholidayapplysModal').modal('toggle');
						});
				   });
		     },200); */
	    
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
					$stationery_table.bootstrapTable('refresh');
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