<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/template/modal.jsp"><jsp:param
		value="addStu" name="id" /><jsp:param value="编辑学生信息" name="title" /></jsp:include>
		
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
/* #student_table{
  width: 4500px;
  margin-top: 0px !important;;  
}

.fixed-table-header{
   display:none !important;;
} 
 .table .btn-group{
   position:relative; 
}
.fixed-table-container{
    height: 600px;
} */

-->
</style>
<section class="content">
	<div id="person-form-id"></div>
	<div id="filter-bar">
		<div class="btn-toolbar">
			<button type="button" class="btn btn-primary " id="student_add">添加</button>
		</div>
		<div class="btn-toolbar">
			<form id="formhomepage-user">
				<span id="uploadUser"> <input type="file" id="fileInput"
					class="fileInput" size="1" name="uploadFile"
					onchange="uploadUser()" /> <input type="button"
					class="btn btn-primary" id="filebutton" value="导入信息" />
				</span>
			</form>
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
				<option value="stu_no">学号</option>
				<option value="stu_name">姓名</option>
				<!-- <option value="class_name">班级</option> -->
			  </select>
		  </div>
		  <div class="btn-toolbar">
			<select class="form-control" id="class_id" name="class_id" style="width: 150px;" onchange="getStuByClass()" > </select>
		 </div>
		 <div class="btn-toolbar">
			<select class="form-control" id="class_pid" name="class_pid" style="width: 150px;" onchange="classChange()" > </select>
		 </div>
		<!-- </form> -->
	</div>
	<table id="student_table" class="table-condensed table table-hover"
		data-row-style="rowStyle" data-side-pagination="server"></table>
		</section>
<script type="text/javascript">
	var $student_table;
	var classtree = $.webapp.root + "/admin/system/class/pidtree.do";
	$(function() {
		$.BOOT.autoselect("class_pid", classtree, {
			title : "选择年份"
		}); 
		//$(".fixed-table-header").hide();
		$student_table = $.BOOT.table("student_table", $.webapp.root+ "/admin/system/student/datagridperson.do", {
			columns : [ {
				title : "全选",
				field : 'select',
				checkbox : true,
				width : 20,
				align : "center",
				valign : "middle",
			}, {
				field : 'stu_no',
				title : '学号',
				sortable : true
			}, {
				field : 'class_name',
				title : '班级名称',
				sortable : true
			}, {
				field : 'stu_name',
				title : '姓名',
			}, {
				field : 'sex',
				title : '性别',
			},{
				field : 'phone',
				title : '手机',
			},{
				field : 'email',
				title : '邮箱',
			}, {
				field : 'birthday',
				title : '出生年月'
			}, {
				field : 'idcard',
				title : '身份证号码',
			}, {
				field : 'province',
				title : '省份',
			},  {
				field : 'city',
				title : '城市',
			},{
			    field : 'entrance_date_Str',
			    title : '入学日期'
		    }, {
			   field : 'nation',
			   title : '民族',
		    },{
				field : 'politicalFace',
				title : '政治面貌',
			},{
				field : 'origin',
				title : '籍贯',
			},{
				field : 'accountAddr',
				title : '户籍地址',
			},{
				field : 'accountPro',
				title : '户口性质',
			},{
				field : 'graduate_school',
				title : '毕业中学',
			},{
				field : 'profession',
				title : '专业',
			},{
				field : 'contact',
				title : '联系人',
			},{
				field : 'contactPhone',
				title : '联系人电话',
			},{
				field : 'material',
				title : '资料',
			}, {
				field : 'id',
				title : '操作',
				formatter : function(value, row, index) {
					/* return $.BOOT.groupbtn(value, [ {
						cla : 'person_edit',
						text : "编辑"
					}, {
						cla : 'person_delete',
						text : "删除"
					} ]); */
					return "<input type='button'  name='"+value+"' id='person_edit' value='编辑' />"
				}
			}  ],
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
			}, {
				field : 'class_name',
				label : '班级',
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
	
	$.BOOT.click("#person_edit", function(c) {
		var id = $(c).attr("name");
		var href = $.webapp.root + '/admin/system/student/student_form_UI.do?id='
		+ id;
		$.BOOT.page("content_addStu", href, function() {
			$('#addStuModal').modal('toggle');
         });
		
	});
	
	/* 筛选学生信息 */
	$(document).on("click", "#buttonByKey", function() {
	     $student_table.bootstrapTable('refresh', 
				{url: "/admin/system/student/datagridperson.do?class_id="+$("#class_id").val()+"searchKeyName="+$("#searchKeyName").val()+"&selectType="+$("#selectType").val()+""}); 
	  });
	
	 function classChange(){
	 	 $("#class_id").html("");
	 	  var selectId = $('#class_pid>option:selected');
	       selectId.html(function(){
	     	  var orgchildtree = $.webapp.root + '/admin/system/class/tree.do?pid='+this.value;
	 		  $.BOOT.autoselect("class_id", orgchildtree, {
	 	 			title : "选择班级"
	 	 	}); 
	   })
	 }
	 function getStuByClass(){
		  $student_table.bootstrapTable('refresh', 
					{url: "/admin/system/student/datagridperson.do?class_id="+$("#class_id").val()+""}); 
		
	 }
	
	$("#batch_delete").click(function(row, $element) {
		console.info("okd")
		var $input = $("tr.selected");
		var ids = "";
		for (var i = 0; i < $input.length; i++) {
			var id = $input.find("input[id=person_edit]").eq(i).attr('name');
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
						//trupdate();
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