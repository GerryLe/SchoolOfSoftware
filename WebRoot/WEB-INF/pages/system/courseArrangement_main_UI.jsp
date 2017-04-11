<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/template/modal.jsp"><jsp:param
		value="addScore" name="id" /><jsp:param value="编辑成绩" name="title" /></jsp:include>
		
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
				<option value="stu_no">学号</option>
				<option value="stu_name">姓名</option>
			  </select>
		 </div>
		 <div class="btn-toolbar">
			<select class="form-control" id="course_id" name="course_id" style="width: 200px;"  > </select>
		 </div>
		  <div class="btn-toolbar">
			<select class="form-control" id="semester" name="semester" style="width: 200px;"  > 
			    <option value="semester_1">第一学期</option>
				<option value="semester_2">第二学期</option>
			</select>
		 </div>
		 <div class="btn-toolbar">
			<select class="form-control" id="class_id" name="class_id" style="width: 200px;"  > </select>
		 </div>
		
	</div>
	<table id="student_table" class="table-condensed table table-hover"
	  data-row-style="rowStyle" data-side-pagination="server"></table>
		<div class="btn-toolbar">
			<button type="button" class="btn btn-primary " id="attendance_commit">提交</button>
		</div>
		</section>
<script type="text/javascript">
	var $student_table;
	var classtree = $.webapp.root + "/admin/system/class/tree.do";
	var coursetree=$.webapp.root + "/admin/system/course/tree.do";
	var selectSection="<select class='form-control' id='class_id' name='class_id' style='width: 100%;'>"
		selectSection+="<option value="section12">一、二节</option>";
		selectSection+="<option value="section123">一、二、三节</option>";
		selectSection+="<option value="section34">三、四节</option>";
		selectSection+="<option value="section1234">一、二、三、四节</option>";
		selectSection+="<option value="section56">五、六节</option>";
		selectSection+="<option value="section567">五、六、七节</option>";
		selectSection+="<option value="section78">七、八节</option>";
		selectSection+="<option value="section5678">五、六、七、八节</option>";
		selectSection+="<option value="section90">九、十节</option>";
		selectSection+="<option value="section911">九、十、十一节</option>";
		selectSection+="</select>";
	$(function() {
		$.BOOT.autoselect("class_id", classtree, {
			title : "选择班级"
		}); 
		$.BOOT.autoselectCourse("course_id", coursetree, {
			title : "选择课程"
		}); 
		$student_table = $.BOOT.table("student_table", $.webapp.root+ "/admin/system/course/datagridClass.do", {
			columns : [ /* {
				title : "全选",
				field : 'select',
				checkbox : true,
				width : 20,
				align : "center",
				valign : "middle",
			},  */{
				field : 'class_no',
				title : '班级编号',
				sortable : true
			}, {
				field : 'class_name',
				title : '班级名称',
			}/*, {
				field : 'monday',
				title : '星期一',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					return "<input type='checkbox' name='section1' id='section1'/>"
				}
			} ,{
				field : 'tuesday',
				title : '星期二',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					return selectSection
				}
			},{
				field : 'wednesday',
				title : '星期三',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					return selectSection
				}
			}, {
				field : 'thursday',
				title : '星期四',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					return selectSection
				}
			}, {
				field : 'friday',
				title : '星期五',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					return selectSection
				}
			}, {
				field : 'saturday',
				title : '星期六',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					return selectSection
				}
			}, {
				field : 'sunday',
				title : '星期日',
				align : "center",
				valign : "middle",
				formatter : function(value, row, index) {
					return selectSection
				}
			} */],
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
				field : 'class_name',
				label : '班级名称',
				type : 'search'
			}, {
				field : 'class_no',
				label : '班级编号',
				type : 'search'
			}],
			connectTo : '#student_table'
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
				{url: "/admin/system/user/datagridperson.do?searchKeyName="+$("#searchKeyName").val()+"&selectType="+$("#selectType").val()+""}); 

	
	  });
	
	
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

	/* 账号重复提醒 */
 	 function uploadUser() {
		var text = "账号 ";
		var flag = true;
		var options = {
			url : '/admin/system/user/checkUserAccount.do',
			type : 'post',
			dataType : 'text',
			success : function(data) {
				var dataObj = eval("(" + data + ")");
				for (var i=0; i < dataObj.length; i++) {
					 $.ajax({
						type:"get",
						url:"/admin/system/user/importUserId.do",
						data:"account="+ dataObj[i].account,
						async:false,
						success:function(result) {
							if (result == 1) {
								text +=dataObj[i].account + "、 ";
								flag = false;
							}
							if ((i+1) == dataObj.length) {
								if(!flag){
									var r = confirm(text + "等已经存在，是否替换？");  
									if (r == true) {
										importUser();
									}
								}
								if(flag){
									importUser()
								}
							}
							
						}
					}); 
					 
				}
			}

		};
		$("#formhomepage-user").ajaxSubmit(options);
	} 

  /* function uploadUser() {
		var flag=true;
		var options = {
			url : '/admin/system/user/checkUserAccount.do',
			type : 'post',
			dataType : 'text',
			success : function(data) {
				var dataObj=eval("("+data+")");
				var myArray=new Array();
		        for(var i=0;i<dataObj.length;i++){
		        	 $.post('/admin/system/user/importUserId.do', 
		        			 {account : dataObj[i].account}, function(result) {
		        	 		    			if(result==1){
		        	 		    				falg=false;
		        	 		    				var r=confirm("帐号"+dataObj[--i].account+"已经存在,是否替换");
		        	 		    				if(r==true){
		        	 		    				falg=true;
		        	 		    				}
		        	 		    			}
		        	 		    		}, 'json');
		        }
			}
		};
		$("#formhomepage-user").ajaxSubmit(options);
		if(flag){
	        importUser();
	        }
	}   
  */
	function importUser() {
		var options = {
			url : '/admin/system/user/importUser.do',
			type : 'post',
			dataType : 'json',
			success : function(data) {
				$student_table.bootstrapTable('refresh');
				$.BOOT.toast1(result);
			}
		};
		$("#formhomepage-user").ajaxSubmit(options);
	}
	
	$("input#pitch").parent().parent().click(function(){
		alert("ok");
	})

</script>