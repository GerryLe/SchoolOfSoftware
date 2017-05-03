<%@page import="java.util.Date"%>
<%@page import="com.rosense.basic.util.date.DateUtils"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/template/modal.jsp"><jsp:param
		value="addAttendance" name="id" /><jsp:param value="编辑考勤信息信息" name="title" /></jsp:include>
		
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
		<!-- <div class="btn-toolbar">
			<button type="button" class="btn btn-primary " id="student_add">添加</button>
		</div> -->
		<!-- <div class="btn-toolbar">
			<button type="button" class="btn btn-primary " id="batch_delete">批量删除</button>
		</div> -->
		<!-- <div class="btn-toolbar">
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
				<option value="stu_no">学号</option>
				<option value="stu_name">姓名</option>
			  </select>
		 </div> -->
		
		<div class="btn-toolbar">
			<input class="date_class"  type="text"  id="recordDate" name="recordDate" readonly  style="height:34px;text-align: center;">
		   <span class="input-group form_date date col-md-5 input-group-addon" data-date="" data-date-format="yyyy-mm-dd" data-link-field="recordDate" data-link-format="yyyy-mm-dd"  id="glyphicon-planComeDate-calendar" style="margin: 0;width: 34px; height:34px;background-color: #F0F0F0; float: right" ><span class="glyphicon glyphicon-calendar"></span></span>
		 </div> 
		 
		 <div class="btn-toolbar">
			<select class="form-control" id="class_id" name="class_id" style="width: 150px;" onchange="getStuByClass()" > </select>
		 </div>
		 <div class="btn-toolbar">
			<select class="form-control" id="class_pid" name="class_pid" style="width: 150px;" onchange="classChange()" > </select>
		 </div>
		 
		  <div class="btn-toolbar">
			<select class="form-control" id="semester" name="semester" style="width: 150px;"  > 
			    <option value="semester_1">第一学期</option>
				<option value="semester_2">第二学期</option>
			</select>
		 </div>
		 <div class="btn-toolbar">
			<select class="form-control" id="schoolYear" name="schoolYear" style="width: 150px;"  > 
			    <option value="2010-2011">2010-2011</option>
				<option value="2011-2012">2011-2012</option>
				<option value="2012-2013">2012-2013</option>
				<option value="2013-2014">2013-2014</option>
				<option value="2014-2015">2014-2015</option>
				<option value="2015-2016">2015-2016</option>
				<option value="2016-2017">2016-2017</option>
				<option value="2017-2018">2017-2018</option>
			</select>
		 </div>
		 <!-- <div class="btn-toolbar">
			<select class="form-control" id="class_id" name="class_id" style="width: 200px;"  > </select>
		 </div> -->
		  
		
	</div>
	<table id="student_table" class="table-condensed table table-hover"
	  data-row-style="rowStyle" data-side-pagination="server"></table>
		<div class="btn-toolbar">
			<button type="button" class="btn btn-primary " id="attendance_commit">提交</button>
		</div>
		</section>
<script type="text/javascript">
	var $student_table;
	var classtree = $.webapp.root + "/admin/system/class/pidtree.do";
	var  currentDate='<%=DateUtils.formatYYYYMMDD(new Date())%>';
	$(function() {
		$("#recordDate").val(currentDate);
		/* $.BOOT.autoselect("class_id", orgtree, {
			title : "选择班级"
		});  */
		$.BOOT.autoselect("class_pid", classtree, {
			title : "选择年级"
		});
		$student_table = $.BOOT.table("student_table", $.webapp.root+ "/admin/system/student/datagridperson.do", {
			columns : [ /* {
				title : "全选",
				field : 'select',
				checkbox : true,
				width : 20,
				align : "center",
				valign : "middle",
			},  */{
				field : 'stu_no',
				title : '学号',
				sortable : true
			}, {
				field : 'stu_name',
				title : '姓名',
			}, {
				field : 'section1',
				title : '第一节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					return "<input type='checkbox' name='section1' id='section1'/>"
				}
			},{
				field : 'section2',
				title : '第二节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					return "<input type='checkbox' name='section1' id='section1'/>"
				}
			},{
				field : 'section3',
				title : '第三节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					return "<input type='checkbox' name='section1' id='section1'/>"
				}
			}, {
				field : 'section4',
				title : '第四节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					return "<input type='checkbox' name='section1' id='section1'/>"
				}
			}, {
				field : 'section5',
				title : '第五节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					return "<input type='checkbox' name='section1' id='section1'/>"
				}
			}, {
				field : 'section6',
				title : '第六节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					return "<input type='checkbox' name='section1' id='section1'/>"
				}
			},  {
				field : 'section7',
				title : '第七节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					return "<input type='checkbox' name='section1' id='section1'/>"
				}
			},{
			    field : 'section8',
			    title : '第八节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					return "<input type='checkbox' name='section1' id='section1'/>"
				}
		    }, {
			   field : 'section9',
			   title : '第九节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					return "<input type='checkbox' name='section1' id='section1'/>"
				}
		    },{
				field : 'section10',
				title : '第十节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					return "<input type='checkbox' name='section1' id='section1'/>"
				}
			},{
				field : 'section11',
				title : '第十一节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					return "<input type='checkbox' name='section1' id='section1'/>"
				}
			},],
			paginationInfo : true,
			showExport : true,
			onDblClickRow:function(row, $element){
				onDbClick(row, $element);
			}
		});

		var onDbClick=function(row, $element){ 
			$("#teacher_table tr").css('background-color','');
			$(this).css('background-color','#D0D0D0');
		    var id = $(this).find("input").val()
		    var href = $.webapp.root + '/admin/system/student/student_form_UI.do?id='
			+ row.id;
			$.BOOT.page("content_addStu", href, function() {
				$('#addStuModal').modal('toggle');
				$(this).css('background-color','red');
	           });
        }
		
		$('#filter-bar').bootstrapTableFilter({
			filters : [ {
				field : 'stu_name',
				label : '姓名',
				type : 'search'
			}, {
				field : 'stu_no',
				label : '学号',
				type : 'search'
			}],
			connectTo : '#student_table'
		});
		
		$('.form_date').datetimepicker({
	        language:  'zh-CN',
	        weekStart: 1,
	        todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			minView: 2,
			forceParse: 0
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
	
	
	
	/* 筛选用户信息 */
	$(document).on("click", "#buttonByKey", function() {
	     $student_table.bootstrapTable('refresh', 
				//{url: "/admin/system/user/datagridperson.do?searchKeyName="+$("#searchKeyName").val()+"&selectType="+$("#selectType").val()+""}); 
	    		 {url: "/admin/system/student/datagridperson.do?class_id="+$("#class_id").val()+""}); 
	  });
	
	 function classChange(){
	 	 $("#class_id").html("");
	 	  var selectId = $('#class_pid>option:selected');
	       selectId.html(function(){
	     	  var orgchildtree = $.webapp.root + '/admin/system/class/treeChild.do?pid='+this.value;
	 		  $.BOOT.autoselect("class_id", orgchildtree, {
	 	 			title : "选择班级"
	 	 	}); 
	   })
	 }
	 function getStuByClass(){
		  $student_table.bootstrapTable('refresh', 
					{url: "/admin/system/student/datagridperson.do?class_id="+$("#class_id").val()+""}); 
		
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
			text : "确定批量删除用户吗?",
			showCancelButton : true,
			type : 'warning',
			call : function() {
				var href = $.webapp.root + '/admin/system/user/delete.do';
					$.post(href, {
						ids : ids
					}, function(result) {
						$student_table.bootstrapTable('refresh');
						$.BOOT.toast1(result);
					}, 'json');
			}
		};
		$.BOOT.alert(json, true);
	});

	$.BOOT.click(".person_delete", function(c) {
		var id = $(c).attr("val");
		var json = {
			title : "",
			text : "确定删除用户吗?",
			showCancelButton : true,
			type : 'warning',
			call : function() {
				var href = $.webapp.root + '/admin/system/user/delete.do';
				$.post(href, {
					ids : id
				}, function(result) {
					$student_table.bootstrapTable('refresh');
					$.BOOT.toast1(result);
				}, 'json');
			}
		};
		$.BOOT.alert(json, true);
	});
	$.BOOT.click(".person_edit", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/user/person_form_UI.do?id='
				+ id;
		$.BOOT.page("content_addStu", href, function() {
			$('#addStuModal').modal('toggle');
		});
		
	});

	
	
	$(document).on("click", "#student_add", function() {
		var href = $.webapp.root + '/admin/system/student/student_form_UI.do';
		$.BOOT.page("content_addStu", href, function() {
			$('#addStuModal').modal('toggle');
		});
	});

	
</script>