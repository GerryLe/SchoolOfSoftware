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
<div class="" id="addScoreModal" role="dialog" tabindex="-1">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<form id="form_addScore" novalidate="novalidate" class="bv-form">
			   <div class="modal-body" id="content_addScore">
			      <section class="content">
				<!-- <div id="person-form-id"></div> -->
				<input type="hidden" name="id" value="${id}"/>
				<input type="hidden" name="uuid" value="${uuid}"/>
	            <div id="filter-bar">
		 <div class="btn-toolbar">
			<select class="form-control" id="course_id" name="course_id" style="width: 200px;" onchange="recordChange()"  >
                <!-- <option value="semester_1">Java课程设计</option> -->
           </select>
		 </div>
		  <div class="btn-toolbar">
		  <select class="form-control" id="class_id" name="class_id" style="width: 200px;" onchange="recordChange()" > </select>
		 </div>
		 <div class="btn-toolbar">
			<select class="form-control" id="class_pid" name="class_pid" style="width: 120px;" onchange="classChange()" > </select>
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
  </div>
			<div class="modal-footer">
				<button type="submit" class="btn btn1 btn-primary" id="save_addScore">提交</button>
			</div>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript">
	var $score_table;
	var classtree = $.webapp.root + "/admin/system/class/pidtree.do";
	var  currentDate='<%=DateUtils.formatYYYYMMDD(new Date())%>';
	var coursetree=$.webapp.root + "/admin/system/course/tree.do";
	var getUrl=$.webapp.root+ "/admin/system/score/datagrid.do";
	var formUrl = $.webapp.root + "/admin/system/score/update.do";
	$(function() {
		$.BOOT.autoselect("class_pid", classtree, {
			title : "选择年级"
		});
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
			},{
				field : 'status',
				title : '全选',
				align : "center",
				valign : "middle",
				checkbox : true,
				//visible : false,
				formatter : function(value, row, index) {
						return {
						checked : true
					    };
				}
			},{
				field : 'stu_no',
				title : '学号',
				sortable : true
			}, {
				field : 'stu_name',
				title : '姓名',
			}, /* {
				field : 'course_name',
				title : '课程名称',
				formatter : function(value, row, index) {
					if(value==null)
						return $("#course_id option:selected").test();
						return value;
				}
			}, */ {
				field : 'score',
				title : '分数',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					if(value!=null)
					return "<input type='text' value="+value+" name='"+row.stu_no+"score' id='score' style='text-align: center;'/>"
					return "<input type='text'  name='"+row.stu_no+"score' id='score' style='text-align: center;'/>"
				}
			},{
				field : 'credit',
				title : '学分',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					if(value!=null) 
					return "<input type='text' value="+value+"  name='"+row.stu_no+"credit' id='credit' style='text-align: center;'/>"
					return "<input type='text' name='"+row.stu_no+"credit' id='credit' style='text-align: center;'/>"
				}
			},{
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
			filters : [ {
				field : 'stu_name',
				label : '姓名',
				type : 'search'
			}, {
				field : 'stu_no',
				label : '学号',
				type : 'search'
			}],
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
		var keyUrl="/admin/system/score/datagrid.do?school_year="+$("#school_year").val()+"&semester="+$("#semester").val()+"&class_id="+$("#class_id").val()+"&course_id="+$("#course_id").val()+"";
		 $.get(keyUrl, {}, function(result) {
             if(result.total==0){
            	 keyUrl=$.webapp.root+ "/admin/system/student/datagridperson.do?school_year="+$("#school_year").val()+"&semester="+$("#semester").val()+"&class_id="+$("#class_id").val()+"&course_id="+$("#course_id").val()+"";
            	 formUrl = $.webapp.root + "/admin/system/score/add.do";
             }
            $score_table.bootstrapTable('refresh', {url: keyUrl}); 
		}, 'json'); 
	 } 
	 
	  $.BOOT.form("form_addScore", {}, function(params) {
			var selects = $score_table.bootstrapTable('getSelections');
			if(selects.length==0){
				alert("请在左边勾选要提交信息记录");
				$("#save_addScore").removeAttr("disabled")
				return;
			}
			  if($("#school_year>option:selected").attr("value")==null||$("#school_year>option:selected").attr("value")==""){
				alert("请选择学年");
				$("#save_addScore").removeAttr("disabled")
				return;
			}
			if($("#semester>option:selected").attr("value")==null||$("#semester>option:selected").attr("value")==""){
				alert("请选择学期");
				$("#save_addScore").removeAttr("disabled")
				return;
			}
			if($("#class_id>option:selected").attr("value")==null||$("#class_id>option:selected").attr("value")==""){
				alert("请选择班级");
				$("#save_addScore").removeAttr("disabled")
				return;
			} 
			if($("#course_id>option:selected").attr("value")==null||$("#course_id>option:selected").attr("value")==""){
				alert("请选择课程");
				$("#save_addScore").removeAttr("disabled")
				return;
			} 
			var param=$.map(selects, function(row) {
				    return 'id='+row.id+'&uuid='+row.uuid+'&stu_no='+row.stu_no+'&stu_name='+row.stu_name
				    +'&credit='+$("input[name="+row.stu_no+"credit]").val()
				    +'&score='+$("input[name="+row.stu_no+"score]").val()
				    +'&school_year='+$("#school_year").val() 
				    +'&class_id='+$("#class_id").val()
				    +'&semester='+$("#semester").val()
				    +'&course_id='+$("#course_id").val();
			  });
			param=param.join(",");
			var par=param.split(",");
			 for(var i=0;i<par.length;i++){
				var keyUrl="/admin/system/score/datagrid.do?school_year="+$("#school_year").val()+"&semester="+$("#semester").val()+"&class_id="+$("#class_id").val()+"&course_id="+$("#course_id").val()+"";
				 $.post(formUrl, par[i], function(result) {
					 $score_table.bootstrapTable('refresh',{url: keyUrl});
					$.BOOT.toast1(result);
					$("#save_addScore").removeAttr("disabled")
					}, 'json'); 
			} 

		});

</script>