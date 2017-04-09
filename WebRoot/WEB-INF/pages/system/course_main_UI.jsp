<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/template/modal.jsp"><jsp:param
		value="addCourse" name="id" /><jsp:param value="编辑课程信息" name="title" /></jsp:include>
		
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
-->
</style>
<section class="content">
	<div id="person-form-id"></div>
	<div id="filter-bar">
		<div class="btn-toolbar">
			<button type="button" class="btn btn-primary " id="course_add">添加</button>
		</div>
		<div class="btn-toolbar">
			<button type="button" class="btn btn-primary " id="batch_delete">批量删除</button>
		</div>
		 <div class="btn-toolbar">
		   <div class="btn-group btn-group-filter-refresh">
		       <button id="buttonByKey" type="button" class="btn btn-default  btn-refresh" data-toggle="dropdown" style="display: block;" style="float: right">搜一下</button>
		   </div>
		</div> 
		<div class="btn-toolbar"> 
			<div class="form-group">
                  <input type="text" name="searchKeyName" id="searchKeyName" class="form-control" placeholder="Search">
			</div> 
		</div>
		<div class="btn-toolbar">
			<select class="form-control" id="selectType" name="selectType" style="width: 100%">
				<option value="course_no">课程号</option>
				<option value="course_name">课程名</option>
			  </select>
		  </div>
	</div>
	<table id="course_table" class="table-condensed table table-hover"
		data-row-style="rowStyle" data-side-pagination="server"></table>
		</section>
<script type="text/javascript">
	var $course_table;
	$(function() {
		$course_table = $.BOOT.table("course_table", $.webapp.root+ "/admin/system/course/datagridCourse.do", {
			columns : [ {
				title : "全选",
				field : 'select',
				checkbox : true,
				width : 20,
				align : "center",
				valign : "middle",
			}, {
				field : 'course_no',
				title : '编号',
				sortable : true
			}, {
				field : 'course_name',
				title : '课程名称',
				sortable : true
			}, {
				field : 'credit',
				title : '学分',
			}, {
				field : 'createTime',
				title : '创建时间',
			}, {
			field : 'id',
			title : '操作',
			formatter : function(value, row, index) {
				return $.BOOT.groupbtn(value, [ {
					cla : 'course_edit',
					text : "编辑"
				}, {
					cla : 'course_delete',
					text : "删除"
				} ]);
			}
		    } ],
			paginationInfo : true,
			showExport : true,
			onDblClickRow:function(row, $element){
				onDbClick(row, $element);
			}
		});

		var onDbClick=function(row, $element){ 
			$("#course_table tr").css('background-color','');
			$(this).css('background-color','#D0D0D0');
		    var id = $(this).find("input").val()
		    var href = $.webapp.root + '/admin/system/course/course_form_UI.do?id='
			+ row.id;
			$.BOOT.page("content_addCourse", href, function() {
				$('#addCourseModal').modal('toggle');
				$(this).css('background-color','red');
	           });
        }
		
		$('#filter-bar').bootstrapTableFilter({
			filters : [ {
				field : 'course_name',
				label : '课程名称',
				type : 'search'
			}, {
				field : 'course_no',
				label : '课程编号',
				type : 'search'
			}],
			connectTo : '#course_table'
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
	
	
	$("#batch_delete").click(function() {
		console.info("okd")
		var $input = $("tr.selected");
		var ids = "";
		for (var i = 0; i < $input.length; i++) {
			var html = $input.find("li.person_edit").eq(i).prop('outerHTML');
			var b = html.indexOf("\"");
			var e = html.indexOf("\" ");
			var id = html.substring(b + 1, e);
			ids += id + ",";
		}
		var json = {
			title : "",
			text : "确定批量删除课程吗?",
			showCancelButton : true,
			type : 'warning',
			call : function() {
				var href = $.webapp.root + '/admin/system/course/delete.do';
					$.post(href, {
						ids : ids
					}, function(result) {
						$course_table.bootstrapTable('refresh');
						$.BOOT.toast1(result);
					}, 'json');
			}
		};
		$.BOOT.alert(json, true);
	});

	$.BOOT.click(".course_delete", function(c) {
		var id = $(c).attr("val");
		var json = {
			title : "",
			text : "确定删除用户吗?",
			showCancelButton : true,
			type : 'warning',
			call : function() {
				var href = $.webapp.root + '/admin/system/course/delete.do';
				$.post(href, {
					ids : id
				}, function(result) {
					$course_table.bootstrapTable('refresh');
					$.BOOT.toast1(result);
				}, 'json');
			}
		};
		$.BOOT.alert(json, true);
	});
	$.BOOT.click(".course_edit", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/course/course_form_UI.do?id='
				+ id;
		$.BOOT.page("content_addCourse", href, function() {
			$('#addCourseModal').modal('toggle');
		});
		
	});

	$(document).on("click", "#course_add", function() {
		var href = $.webapp.root + '/admin/system/course/course_form_UI.do';
		$.BOOT.page("content_addCourse", href, function() {
			$('#addCourseModal').modal('toggle');
		});
	});


</script>