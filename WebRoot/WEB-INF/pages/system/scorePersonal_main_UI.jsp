<%@page import="java.util.Date"%>
<%@page import="com.rosense.basic.util.date.DateUtils"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

		
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

.modal-dialog{
 width:100%;
 height:50%;
 text-align: center;
margin-top: 0px;
}
-->
</style>
		<section class="content">
				<!-- <div id="person-form-id"></div> -->
				<input type="hidden" name="id" value="${id}"/>
				<input type="hidden" name="uuid" value="${uuid}"/>
	            <div id="filter-bar">
		 <div class="btn-toolbar">
			<select class="form-control" id="course_id" name="course_id" style="width: 200px;"  onchange="recordChange()"  >
                <!-- <option value="semester_1">Java课程设计</option> -->
           </select>
		 </div>
		  <div class="btn-toolbar">
			<select class="form-control" id="semester" name="semester" style="width: 120px;"  onchange="recordChange()"> 
			    <option value="" selected="selected">--选择学期--</option>
			    <option value="semester_1">第一学期</option>
				<option value="semester_2">第二学期</option>
			</select>
		 </div>
		 <div class="btn-toolbar">
			<select class="form-control" id="school_year" name="school_year" style="width: 120px;"  onchange="recordChange()"> 
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
	<table id="score_table" class="table-condensed table table-hover"
	  data-row-style="rowStyle" data-side-pagination="server"></table>
	</section>
<script type="text/javascript">
	var $score_table;

	var  currentDate='<%=DateUtils.formatYYYYMMDD(new Date())%>';
	var coursetree=$.webapp.root + "/admin/system/course/tree.do";
	var getUrl=$.webapp.root+ "/admin/system/score/datagridPersonal.do";
	$(function() {
		
		$.BOOT.autoselectCourse("course_id", coursetree, {
			title : "选择课程"
		}); 
		$score_table = $.BOOT.table("score_table", getUrl, {
			columns : [ {
				field : 'id',
				title : 'id',
				visible : false,
				formatter : function(value, row, index) {
					$("#id").val(value)
					return value;
				}
			},{
				field : 'uuid',
				title : 'uuid',
				visible : false,
				formatter : function(value, row, index) {
					$("#uuid").val(value)
					return value;
				}
			}, {
				field : 'course_name',
				title : '课程名称'
			},  {
				field : 'score',
				title : '分数',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					return "<input type='text'  value="+value+" readonly='readonly'  name='"+row.stu_no+"score' id='score' style='text-align: center;'/>"
				}
			},{
				field : 'credit',
				title : '学分',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					return "<input type='text'  readonly='readonly' value="+value+"  name='"+row.stu_no+"credit' id='credit' style='text-align: center;'/>"
				}
			} ,{
				field : 'apply_date',
				title : '更新日期',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					if(value==null)
						return currentDate;
						return value;
				}
			}],
			paginationInfo : true,
			showExport : true,
			onDblClickRow:function(row, $element){
				onDbClick(row, $element);
			}
		});

		$('#filter-bar').bootstrapTableFilter({
			filters : [ ],
			connectTo : '#score_table'
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
	
	 //条件筛选记录
	  function recordChange(){
		var keyUrl="/admin/system/score/datagridPersonal.do?school_year="+$("#school_year").val()+"&semester="+$("#semester").val()+"&course_id="+$("#course_id").val()+"";
		 $.get(keyUrl, {}, function(result) {
            $score_table.bootstrapTable('refresh', {url: keyUrl}); 
		}, 'json'); 
	 } 
	 
</script>