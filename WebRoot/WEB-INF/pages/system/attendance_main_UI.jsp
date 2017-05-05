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
		<div class="btn-toolbar">
			<input class="date_class"  type="text"  id="apply_date" name="apply_date"  style="height:34px;text-align: center;" readonly="readonly" onchange="recordChange()">
		     <span class="input-group form_date date col-md-5 input-group-addon" data-date="" data-date-format="yyyy-mm-dd" data-link-field="apply_date" data-link-format="yyyy-mm-dd"  id="glyphicon-planComeDate-calendar" style="margin: 0;width: 34px; height:34px;background-color: #F0F0F0; float: right" ><span class="glyphicon glyphicon-calendar"></span></span>
		 </div> 
		 
		 <div class="btn-toolbar">
			<select class="form-control" id="class_id" name="class_id" style="width: 150px;" onchange="recordChange()" > </select>
		 </div>
		 <div class="btn-toolbar">
			<select class="form-control" id="class_pid" name="class_pid" style="width: 150px;" onchange="classChange()" > </select>
		 </div>
		 
		  <div class="btn-toolbar">
			<select class="form-control" id="semester" name="semester" style="width: 150px;"  onchange="recordChange()"> 
			    <option value="" selected="selected">--选择学期--</option>
			    <option value="semester_1">第一学期</option>
				<option value="semester_2">第二学期</option>
			</select>
		 </div>
		 <div class="btn-toolbar">
			<select class="form-control" id="school_year" name="school_year" style="width: 150px;"  onchange="recordChange()"> 
			    <option value="" selected="selected">--选择学年--</option>
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
	</div>
	<table id="attendance_table" class="table-condensed table table-hover"
	  data-row-style="rowStyle" data-side-pagination="server"></table>
		<div class="btn-toolbar">
			<button type="submit" class="btn btn1 btn-primary " id="attendance_commit">提交</button>
		</div>
		</section>
<script type="text/javascript">
	var $student_table;
	var classtree = $.webapp.root + "/admin/system/class/pidtree.do";
	var  currentDate='<%=DateUtils.formatYYYYMMDD(new Date())%>';
	var getUrl=$.webapp.root+ "/admin/system/attendance/datagrid.do";
	var formUrl = $.webapp.root + "/admin/system/attendance/update.do";
	$(function() {
		$("#apply_date").val(currentDate);
		$.BOOT.autoselect("class_pid", classtree, {
			title : "选择年级"
		});
		$attendance_table = $.BOOT.table("attendance_table",getUrl, {
			columns : [{
				field : 'uuid',
				title : '学号',
				sortable : true,
				formatter : function(value, row, index) {
					return value;
				}
			},{
				field : 'stu_no',
				title : '学号',
				sortable : true,
				formatter : function(value, row, index) {
					return value;
				}
			}, {
				field : 'stu_name',
				title : '姓名',
				formatter : function(value, row, index) {
					alert(row.uuid)
					return value;
				}
			}, {
				field : 'section1',
				title : '第一节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					var input="<input type='checkbox' name='section1' id='section1' value=1 ";
					if(value==1)
						input+="checked='checked'";
						return input+=">"
				}
			},{
				field : 'section2',
				title : '第二节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					if(value==1){
						return "<input type='checkbox' name='section2' id='section2' checked='checked' value='1'>"
					}else{
						return "<input type='checkbox' name='section2' id='section2'/>"
					}
				}
			},{
				field : 'section3',
				title : '第三节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					if(value==1){
						return "<input type='checkbox' name='section3' id='section3' checked='checked' value='1'>"
					}else{
						return "<input type='checkbox' name='section3' id='section3'/>"
					}
				}
			}, {
				field : 'section4',
				title : '第四节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					if(value==1){
						return "<input type='checkbox' name='section4' id='section4' checked='checked' value='1'>"
					}else{
						return "<input type='checkbox' name='section4' id='section4'/>"
					}
				}
			}, {
				field : 'section5',
				title : '第五节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					if(value==1){
						return "<input type='checkbox' name='section5' id='section5' checked='checked' value='1'>"
					}else{
						return "<input type='checkbox' name='section5' id='section5'/>"
					}
				}
			}, {
				field : 'section6',
				title : '第六节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					if(value==1){
						return "<input type='checkbox' name='section6' id='section6' checked='checked' value='1'>"
					}else{
						return "<input type='checkbox' name='section6' id='section6'/>"
					}
				}
			},  {
				field : 'section7',
				title : '第七节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					if(value==1){
						return "<input type='checkbox' name='section7' id='section7' checked='checked' value='1'>"
					}else{
						return "<input type='checkbox' name='section7' id='section7'/>"
					}
				}
			},{
			    field : 'section8',
			    title : '第八节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					if(value==1){
						return "<input type='checkbox' name='section8' id='section8' checked='checked' value='1'>"
					}else{
						return "<input type='checkbox' name='section8' id='section8'/>"
					}
				}
		    }, {
			   field : 'section9',
			   title : '第九节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					if(value==1){
						return "<input type='checkbox' name='section9' id='section9' checked='checked' value='1'>"
					}else{
						return "<input type='checkbox' name='section9' id='section9'/>"
					}
				}
		    },{
				field : 'section10',
				title : '第十节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					if(value==1){
						return "<input type='checkbox' name='section10' id='section10' checked='checked' value='1'>"
					}else{
						return "<input type='checkbox' name='section10' id='section10'/>"
					}
				}
			},{
				field : 'section11',
				title : '第十一节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					if(value==1){
						return "<input type='checkbox' name='section11' id='section11' checked='checked' value='1'>"
					}else{
						return "<input type='checkbox' name='section11' id='section11'/>"
					}
				}
			},],
			paginationInfo : true,
			showExport : true,
			onDblClickRow:function(row, $element){
				onDbClick(row, $element);
			}
		});
		
		$('#filter-bar').bootstrapTableFilter({
			filters : [ {
				field : 'school_year',
				label : '学年',
				type : 'search'
			}, {
				field : 'semester',
				label : '学期',
				type : 'search'
			},{
				field : 'class_id',
				label : '班级',
				type : 'search'
			},{
				field : 'apply_date',
				label : '日期',
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
	     $attendance_table.bootstrapTable('refresh', 
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
	 
	 //条件筛选记录
	  function recordChange(){
		//首先加载考勤表信息，为空，从学生表获取信息
		var keyUrl="/admin/system/attendance/datagrid.do?school_year="+$("#school_year").val()+"&semester="+$("#semester").val()+"&class_id="+$("#class_id").val()+"&apply_date="+$("#apply_date").val()+"";
			 $.get(keyUrl, {}, function(result) {
				             if(result.total==0){
				            	 keyUrl=$.webapp.root+ "/admin/system/student/datagridperson.do?school_year="+$("#school_year").val()+"&semester="+$("#semester").val()+"&class_id="+$("#class_id").val()+"&apply_date="+$("#apply_date").val()+"";
				            	 formUrl = $.webapp.root + "/admin/system/attendance/add.do";
				             }
				            $attendance_table.bootstrapTable('refresh', {url: keyUrl}); 
						}, 'json'); 
		 
	 } 
	 //指定班级学生
	 function getStuByClass(){
		  $attendance_table.bootstrapTable('refresh', 
					{url: "/admin/system/student/datagridperson.do?class_id="+$("#class_id").val()+""}); 
		
	 }

	$(document).on("click", "#attendance_commit", function() {
		$.post(formUrl, params, function(result) {
			 $.BOOT.toast1(result); 
			 $attendance_table.bootstrapTable('refresh'); 
		}, 'json');
	});

	
</script>