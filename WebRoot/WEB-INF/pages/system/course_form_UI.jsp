<%@page import="com.rosense.basic.util.LangUtils"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script src="${webRoot}/template/resource/js/city.js"></script>

<style>
<!--
.roleName{
height:13px;vertical-align:text-top;margin-top:0;width:30px;
}

 .modal-dialog{
 width:60%;
 height:85%;
 text-align: center;

} 
.employ {
	border:5px;
	width:610px;
	height:700px;
	text-align: center;
	margin-left: 23px;
} 
.employ tr td{
  border:1px solid black;
}
.trsolid{
    font-weight: bold;
    vertical-align: 
    border
 }
.trleft{
  text-align: left;
}

.trcenter{
  text-align: center;
}
 input{
  border: 0px;
  text-align: center;
  height: 100%;
} 	
select{
  text-align: center;
  height: 80%;
}
.inputAge{
 border-top: 0px;
 border-left: 0px;
 border-right:0px;
 border-bottom: 1px;
 width: 30px;
}
-->
</style>
<script>
	var form_url = $.webapp.root + "/admin/system/course/add.do";
	var get = $.webapp.root + "/admin/system/course/get.do";
	var parent_box, person_type;
	var flag = true;//防止重复提交
	var flagphoto=false;
	var flagget=true;
    $(function() {
		
				if ($('input[name=id]').val().length > 0) {
					$.post(get, {
							id : $('input[name=id]').val()
						}, function(result) {
							form_url = $.webapp.root+ "/admin/system/course/update.do";
							$('#form_addCourse').form('load', result);
						}, 'json');
					
				} 
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
   
	 $.BOOT.form("form_addCourse", {
		 course_no : {
			validators : {
				notEmpty : {
					message : '编号不能为空'
				}
			}
		},
		course_name : {
			validators : {
				notEmpty : {
					message : '课程名臣不能为空'
				}
			}
		},
		}, function(params) {
			 if(flag){
			$.post(form_url, params, function(result) {
				 $.BOOT.toast1(result); 
				 $course_table.bootstrapTable('refresh'); 
				modaladdCourse();
			}, 'json');
			flag = false;
		 } 
	}); 
	 
</script>

	
<div>
	<input type="hidden" name="id" value="${id}"/>
	<!-- <table class="employ">
	  <tr class="trsolid trcenter form-group">
	  <td>学号</td>
	  <td>班级</td>
	  <td>姓名</td>
	  <td>性别</td>
	  </tr>
	  
	  <tr class="trcenter form-group">
	  <td><input type="text" name="stu_no" id="stu_no" style="width: 100%" placeholder="输入学号"/></td>
	  <td><input type="text" name="class_id" id="class_id" style="width: 100%" placeholder="输入班级"/></td>
	  <td><input type="text" name="stu_name" id="stu_name" placeholder="输入姓名" style="width: 100%"/></td>
	  <td><select id="sex" name="sex" style="width: 100%;text-align: center">
				<option value="男" >男</option>
				<option value="女">女</option>
			 </select>
	  </td>
	  </tr>
	  
	</table> -->
	
	
	<div class="form-group">
		<label for="course_no">课程编号</label> <input type="text" class="form-control" name="course_no" id="course_no" placeholder="输入课程编号">
	</div>
	<div class="form-group">
		<label for="course_name">课程名称</label> <input type="text" class="form-control" name="course_name" id="course_name" placeholder="输入课程名称">
    </div>
	  <div class="form-group">
		<label for="credit">学分：</label> 
		<input type="text" class="form-control" name="credit" id="credit" placeholder="输入学分">
	  </div>
	</div>