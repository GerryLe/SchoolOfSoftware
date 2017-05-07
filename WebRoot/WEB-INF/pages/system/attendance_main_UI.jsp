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
<div class="" id="addAttendanceModal" role="dialog" tabindex="-1">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<form id="form_addAttendance" novalidate="novalidate" class="bv-form">
			   <div class="modal-body" id="content_addAttendance">
			      <section class="content">
				<!-- <div id="person-form-id"></div> -->
				<input type="hidden" name="id" value="${id}"/>
				<input type="hidden" name="uuid" value="${uuid}"/>
				  <div id="filter-bar">
				     <div class="btn-toolbar">
					   <div class="btn-group btn-group-filter-refresh">
					       <input id="buttonByKey" type="button"  value="搜一下" data-toggle="dropdown" onclick="recordChange()" style="width: 80px;height: 33px" />
					   </div>
		            </div> 
					<div class="btn-toolbar">
						<input class="date_class"  type="text"  id="apply_date" name="apply_date"  
						style="height:34px;text-align: center;" >
					     <span class="input-group form_date date col-md-5 input-group-addon" data-date="" 
					     data-date-format="yyyy-mm-dd" data-link-field="apply_date" data-link-format="yyyy-mm-dd"  
					     id="glyphicon-planComeDate-calendar" style="margin: 0;width: 34px; height:34px;
					     background-color: #F0F0F0; float: right" ><span class="glyphicon glyphicon-calendar"></span></span>
					 </div> 
					 
					 <div class="btn-toolbar">
						<select class="form-control" id="class_id" name="class_id" style="width: 180px;" onchange="recordChange()" > </select>
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
					 <!-- <div class="btn-toolbar">
						<button type="submit" class="btn btn1 btn-primary " id="attendance_commit" style="display: none">提交</button>
					</div> -->
			  </section>
		      </div>
			<div class="modal-footer">
				<button type="submit" class="btn btn1 btn-primary" id="save_addAttendance">提交</button>
			</div>
			</form>
		</div>
	</div>
</div>
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
				sortable : true,
				formatter : function(value, row, index) {
					$("#uuid").val(value)
					return value;
				}
			}, {
				field : 'stu_name',
				title : '姓名',
				formatter : function(value, row, index) {
					return value;
				}
			}, {
				field : 'section1',
				title : '第一节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					var input="<input type='checkbox' name='"+row.stu_no+"section1' id='section1' class='section' onclick='AttenCheck(this)' value=0 ";
					if(value==1)
						input+="checked='checked'";
						return input+=">";
				}
			},{
				field : 'section2',
				title : '第二节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					var input="<input type='checkbox' name='"+row.stu_no+"section2' id='section2'  class='section'  onclick='AttenCheck(this)' value=0 ";
					if(value==1)
						input+="checked='checked'";
						return input+=">";
				}
			},{
				field : 'section3',
				title : '第三节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					var input="<input type='checkbox' name='"+row.stu_no+"section3' id='section3' class='section'  onclick='AttenCheck(this)' value=0 ";
					if(value==1)
						input+="checked='checked'";
						return input+=">";
				}
			}, {
				field : 'section4',
				title : '第四节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					var input="<input type='checkbox' name='"+row.stu_no+"section4' id='section4' class='section'  onclick='AttenCheck(this)' value=0 ";
					if(value==1)
						input+="checked='checked'";
						return input+=">";
				}
			}, {
				field : 'section5',
				title : '第五节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					var input="<input type='checkbox' name='"+row.stu_no+"section5' id='section5'  class='section'  onclick='AttenCheck(this)' value=0 ";
					if(value==1)
						input+="checked='checked'";
						return input+=">";
				}
			}, {
				field : 'section6',
				title : '第六节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					var input="<input type='checkbox' name='"+row.stu_no+"section6' id='section6'  class='section'  onclick='AttenCheck(this)' value=0 ";
					if(value==1)
						input+="checked='checked'";
						return input+=">";
				}
			},  {
				field : 'section7',
				title : '第七节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					var input="<input type='checkbox' name='"+row.stu_no+"section7' id='section7'  class='section'  onclick='AttenCheck(this)' value=0 ";
					if(value==1)
						input+="checked='checked'";
						return input+=">";
				}
			},{
			    field : 'section8',
			    title : '第八节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					var input="<input type='checkbox' name='"+row.stu_no+"section8' id='section8'  class='section'  onclick='AttenCheck(this)' value=0 ";
					if(value==1)
						input+="checked='checked'";
						return input+=">";
				}
		    }, {
			   field : 'section9',
			   title : '第九节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					var input="<input type='checkbox' name='"+row.stu_no+"section9' id='section9'   class='section'  onclick='AttenCheck(this)' value=0 ";
					if(value==1)
						input+="checked='checked'";
						return input+=">";
				}
		    },{
				field : 'section10',
				title : '第十节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					var input="<input type='checkbox' name='"+row.stu_no+"section10' id='section10'  class='section'  onclick='AttenCheck(this)' value=0 ";
					if(value==1)
						input+="checked='checked'";
						return input+=">";
				}
			},{
				field : 'section11',
				title : '第十一节',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					var input="<input type='checkbox' name='"+row.stu_no+"section11' id='section11'  class='section'  onclick='AttenCheck(this)' value=0 ";
					if(value==1)
						input+="checked='checked'";
						return input+=">";
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
		//$('#attendance_table').bootstrapTable('hideColumn', 'status');
		
	});
	
	function rowStyle(row, index) {
		if (row.status == "1") {
			return {
				classes : 'danger'
			};
		}
		return {};
	}
	

	/* $("#apply_date").addEventListener("change",changeValue(),false)
	
	function changeValue(){
		alert('changed');
		}  */
	
	/* 筛选用户信息 */
	/* $(document).on("click", "#buttonByKey", function() {
	     $attendance_table.bootstrapTable('refresh', 
				//{url: "/admin/system/user/datagridperson.do?searchKeyName="+$("#searchKeyName").val()+"&selectType="+$("#selectType").val()+""}); 
	    		 {url: "/admin/system/student/datagridperson.do?class_id="+$("#class_id").val()+""}); 
	  }); */
	 
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
	 
	 //选中考勤时自动赋值
	/*  function AttenCheck(c){
		  $("input.section:checked").each(function(){
			    var name=$(c).attr("name")
				$("input[name="+name+"]").val(1)
				//alert( $("input[name="+name+"]").val())
	 		})
	 	
	 } */

	$.BOOT.form("form_addAttendance", {}, function(params) {
		$("input.section:checked").val(1);
		var selects = $attendance_table.bootstrapTable('getSelections');
		if(selects.length==0){
			alert("请在左边勾选要提交信息记录");
			$("#save_addAttendance").removeAttr("disabled")
			return;
		}
		var param=$.map(selects, function(row) {
			    return 'id='+row.id+'&uuid='+row.uuid+'&stu_no='+row.stu_no+'&stu_name='+row.stu_name+'&section1='+$("input[name="+row.stu_no+"section1]").val()
			    +'&section2='+$("input[name="+row.stu_no+"section2]").val()+'&section3='+$("input[name="+row.stu_no+"section3]").val()
			    +'&section4='+$("input[name="+row.stu_no+"section4]").val()+'&section5='+$("input[name="+row.stu_no+"section5]").val()
			    +'&section6='+$("input[name="+row.stu_no+"section6]").val()+'&section7='+$("input[name="+row.stu_no+"section7]").val()
			    +'&section8='+$("input[name="+row.stu_no+"section8]").val()+'&section9='+$("input[name="+row.stu_no+"section9]").val()
			    +'&section10='+$("input[name="+row.stu_no+"section10]").val()+'&section11='+$("input[name="+row.stu_no+"section11]").val()
			    +'&school_year='+$("#school_year").val() +'&class_id='+$("#class_id").val()+'&semester='+$("#semester").val()
			    +'&apply_date='+$("#apply_date").val();
		  });
		param=param.join(",");
		var par=param.split(",");
		for(var i=0;i<par.length;i++){
			 $.post(formUrl, par[i], function(result) {
				 $attendance_table.bootstrapTable('refresh');
				$.BOOT.toast1(result);
				$("#save_addAttendance").removeAttr("disabled")
				}, 'json'); 
		}

	});
	
</script>