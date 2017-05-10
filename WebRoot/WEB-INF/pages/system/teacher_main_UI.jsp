<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/template/modal.jsp"><jsp:param
		value="addTea" name="id" /><jsp:param value="编辑教师信息" name="title" /></jsp:include>
		
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
/* #teacher_table{
  width: 4500px;
  margin-top: 0px !important;;  
} */
-->
</style>
<section class="content">
	<div id="person-form-id"></div>
	<div id="filter-bar">
		<div class="btn-toolbar">
			<button type="button" class="btn btn-primary " id="teacher_add">添加</button>
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
				<option value="tea_no">编号</option>
				<option value="tea_name">姓名</option>
			  </select>
		  </div>
	</div>
	<table id="teacher_table" class="table-condensed table table-hover"
		data-row-style="rowStyle" data-side-pagination="server"></table>
		</section>
<script type="text/javascript">
	var $teacher_table;
	$(function() {
		$teacher_table = $.BOOT.table("teacher_table", $.webapp.root+ "/admin/system/teacher/datagridperson.do", {
			columns : [ {
				title : "全选",
				field : 'select',
				checkbox : true,
				width : 20,
				align : "center",
				valign : "middle",
			}, {
				field : 'tea_no',
				title : '编号',
				sortable : true
			},{
				field : 'tea_name',
				title : '姓名',
			}, {
				field : 'sex',
				title : '性别',
			},{
				field : 'phone',
				title : '手机',
			},{
				field : 'cornet',
				title : '短号',
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
			    title : '入职日期'
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
				field : 'contact',
				title : '联系人',
			},{
				field : 'contactPhone',
				title : '联系人电话',
			},{
				field : 'material',
				title : '资料',
			},{
				field : 'bankCard',
				title : '银行卡',
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
		    var href = $.webapp.root + '/admin/system/teacher/teacher_form_UI.do?id='
			+ row.id;
			$.BOOT.page("content_addTea", href, function() {
				$('#addTeaModal').modal('toggle');
				$(this).css('background-color','red');
	           });
        }
		
		$('#filter-bar').bootstrapTableFilter({
			filters : [ {
				field : 'tea_name',
				label : '姓名',
				type : 'search'
			}, {
				field : 'tea_no',
				label : '编号',
				type : 'search'
			}, {
				field : 'phone',
				label : '手机',
				type : 'search'
			}],
			connectTo : '#teacher_table'
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
	     $teacher_table.bootstrapTable('refresh', 
				{url: "/admin/system/teacher/datagridperson.do?searchKeyName="+$("#searchKeyName").val()+"&selectType="+$("#selectType").val()+""}); 

	
	  });
	$("#batch_delete").click(function() {
		console.info("okd")
		var $input = $("tr.selected");
		var ids = "";
		for (var i = 0; i < $input.length; i++) {
			var id = $input.find("input[id=person_edit]").eq(i).attr("name");
			ids += id + ",";
		}
		var json = {
			title : "",
			text : "确定批量删除用户吗?",
			showCancelButton : true,
			type : 'warning',
			call : function() {
				var href = $.webapp.root + '/admin/system/teacher/delete.do';
					$.post(href, {
						ids : ids
					}, function(result) {
						$teacher_table.bootstrapTable('refresh');
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
				var href = $.webapp.root + '/admin/system/teacher/delete.do';
				$.post(href, {
					ids : id
				}, function(result) {
					$teacher_table.bootstrapTable('refresh');
					$.BOOT.toast1(result);
				}, 'json');
			}
		};
		$.BOOT.alert(json, true);
	});
	$.BOOT.click("#person_edit", function(c) {
		var id = $(c).attr("name");
		var href = $.webapp.root + '/admin/system/teacher/teacher_form_UI.do?id='
				+ id;
		$.BOOT.page("content_addTea", href, function() {
			$('#addTeaModal').modal('toggle');
		});
		
	});
	
	$(document).on("click", "#teacher_add", function() {
		var href = $.webapp.root + '/admin/system/teacher/teacher_form_UI.do';
		$.BOOT.page("content_addTea", href, function() {
			$('#addTeaModal').modal('toggle');
		});
	});

	/* 账号重复提醒 */
 	 function uploadUser() {
		var text = "账号 ";
		var flag = true;
		var options = {
			url : '/admin/system/teacher/checkUserAccount.do',
			type : 'post',
			dataType : 'text',
			success : function(data) {
				var dataObj = eval("(" + data + ")");
				for (var i=0; i < dataObj.length; i++) {
					 $.ajax({
						type:"get",
						url:"/admin/system/teacher/importUserId.do",
						data:"account="+ dataObj[i].tea_no,
						async:false,
						success:function(result) {
							if (result == 1) {
								text +=dataObj[i].tea_no + "、 ";
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
	function importUser() {
		var options = {
			url : '/admin/system/teacher/importUser.do',
			type : 'post',
			dataType : 'json',
			success : function(data) {
				$teacher_table.bootstrapTable('refresh');
				$.BOOT.toast1(result);
			}
		};
		$("#formhomepage-user").ajaxSubmit(options);
	}
	

</script>