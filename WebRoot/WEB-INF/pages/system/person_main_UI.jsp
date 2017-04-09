<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/template/modal.jsp"><jsp:param
		value="addusers" name="id" /><jsp:param value="编辑用户" name="title" /></jsp:include>
		
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
#person_table{
  width: 4500px;
  margin-top: 0px !important;;  
}
/* 设置固定表头隐藏 */
.fixed-table-header{
   display:none !important;;
} 
 .table .btn-group{
   position:relative; 
}
.fixed-table-container{
    height: 600px;
}

-->
</style>
<section class="content">
	<div id="person-form-id"></div>
	<div id="filter-bar">
		<div class="btn-toolbar">
			<button type="button" class="btn btn-primary " id="person_add">添加</button>
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
		<!-- <div class="btn-toolbar">
			 <button type="submit" class="btn btn-default btn-refresh" style="float: right">搜一下</button>
		</div> -->
		<!-- <form class="navbar-right btn-toolbar form-group" role="search"> -->
		
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
				<option value="name">英文名</option>
				<!-- <option value="chinaname">中文名</option> -->
				<option value="phone">手机</option>
				<option value="area">地区</option>
			  </select>
		  </div>
		<!-- </form> -->
	</div>
	<table id="person_table" class="table-condensed table table-hover"
		data-row-style="rowStyle" data-side-pagination="server"></table>
		</section>
<script type="text/javascript">
	var $person_table;
	$(function() {
		//$(".fixed-table-header").css("display","none");
		$(".fixed-table-header").hide();
		$person_table = $.BOOT.table("person_table", $.webapp.root+ "/admin/system/user/datagridperson.do", {
			columns : [ {
				title : "全选",
				field : 'select',
				checkbox : true,
				width : 20,
				align : "center",
				valign : "middle",
			}, {
				field : 'account',
				title : '账号',
				sortable : true
			}, {
				field : 'name',
				title : '英文名',
				sortable : true
			}, {
				field : 'chinaname',
				title : '中文名',
			}, {
				field : 'sex',
				title : '性别',
			}, {
				field : 'age',
				title : '年龄',
			}, {
				field : 'area',
				title : '地区',
			}, {
				field : 'phone',
				title : '手机',
			},/* {
				field : 'callerid',
				title : '座机分机号',
			}, */{
				field : 'email',
				title : '邮箱',
			},{
				field : 'orgName',
				title : '部门',
			}, {
				field : 'orgChildName',
				title : '部门2',
			}, {
				field : 'positionName',
				title : '职位',
			}, {
				field : 'positionEng',
				title : '职位英文',
			}, {
				field : 'degree',
				title : '学历'
			}, {
				field : 'birthday',
				title : '出生年月'
			}, {
				field : 'idcard',
				title : '身份证号码',
			}, {
				field : 'employmentStr',
				title : '入职日期',
			},  {
				field : 'workAge',
				title : '工龄',
			},{
			    field : 'securityDate',
			    title : '社保日期'
		    }, {
			   field : 'fundDate',
			   title : '公积金',
		    },{
				field : 'probationLimit',
				title : '试用期限',
			},{
				field : 'probationEnd',
				title : '试用到期',
			},{
				field : 'becomeStaffDate',
				title : '试用转正日期',
			},{
				field : 'agreementLimit',
				title : '合同期限',
			},{
				field : 'agreementStartDate',
				title : '合同开始计算日期',
			},{
				field : 'agreementEndDate',
				title : '合同到期',
			},{
				field : 'agreementTimes',
				title : '合同签订次数',
			},{
				field : 'marriage',
				title : '婚姻状况',
			},{
				field : 'bear',
				title : '生育状况',
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
				field : 'address',
				title : '现住址',
			},  {
				field : 'school',
				title : '毕业学校',
			},{
				field : 'graduation',
				title : '毕业时间',
			},{
				field : 'profession',
				title : '专业',
			},{
				field : 'certificate',
				title : '证书',
			},{
				field : 'contact',
				title : '联系人',
			},{
				field : 'contactPhone',
				title : '联系人电话',
			},{
				field : 'workOld',
				title : '工作经历',
			},{
				field : 'material',
				title : '资料',
			},{
				field : 'bankCard',
				title : '银行卡',
			},{
				field : 'train',
				title : '培训经历',
			},{
				field : 'securityCard',
				title : '社保卡',
			},{
				field : 'fund',
				title : '公积金簿',
			},{
				field : 'leaveDate',
				title : '离职日期',
			},{
				field : 'id',
				title : '操作',
				formatter : function(value, row, index) {
					return $.BOOT.groupbtn(value, [ {
						cla : 'person_edit',
						text : "编辑"
					}, {
						cla : 'person_delete',
						text : "删除"
					} , {
						cla : 'person_notice',
						text : "邮件通知"
					}]);
				}
			}  ],
			paginationInfo : true,
			showExport : true,
			onDblClickRow:function(row, $element){
				onDbClick(row, $element);
			}
		});

		var onDbClick=function(row, $element){ 
			$("#holidaysuser_table tr").css('background-color','');
			$(this).css('background-color','#D0D0D0');
		    var id = $(this).find("input").val()
		    var href = $.webapp.root + '/admin/system/user/person_form_UI.do?id='
			+ row.id;
			$.BOOT.page("content_addusers", href, function() {
				$('#addusersModal').modal('toggle');
				$(this).css('background-color','red');
	           });
        }
		
		$('#filter-bar').bootstrapTableFilter({
			filters : [ {
				field : 'name',
				label : '英文名',
				type : 'search'
			}, {
				field : 'chinaname',
				label : '中文名',
				type : 'search'
			}, {
				field : 'job',
				label : '是否在职',
				type : 'search'
			}, {
				field : 'phone',
				label : '手机',
				type : 'search'
			} , {
				field : 'area',
				label : '地区',
				type : 'search'
			}],
			connectTo : '#person_table'
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
	     $person_table.bootstrapTable('refresh', 
				{url: "/admin/system/user/datagridperson.do?searchKeyName="+$("#searchKeyName").val()+"&selectType="+$("#selectType").val()+""}); 

	
	  });
	
   /* 双击行对员工信息进行编辑 */
  /*  setTimeout(//延迟0.5秒
	  function(){  
			$(document).on("dblclick","#person_table>tbody>tr",function(){
				$("#person_table tr").css('background-color','');
				$(this).css('background-color','#D0D0D0');
			    var id = $(this).find(".btn-group").find("ul li:first").attr("val");
				var href = $.webapp.root + '/admin/system/user/person_form_UI.do?id='
						+ id;
				$.BOOT.page("content_addusers", href, function() {
					$('#addusersModal').modal('toggle');
					$(this).css('background-color','red');
				});
		   });
     },500); */

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
						$person_table.bootstrapTable('refresh');
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
					$person_table.bootstrapTable('refresh');
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
		$.BOOT.page("content_addusers", href, function() {
			$('#addusersModal').modal('toggle');
		});
		
	});
	
	//邮件通知全体员工
	/* $.BOOT.click(".person_notice", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/user/notice.do';
		$.post(href, {
			id : id
		}, function(result) {
			$person_table.bootstrapTable('refresh');
			$.BOOT.toast1(result);
		}, 'json');
	}); */
	
	$.BOOT.click(".person_notice", function(c) {
		var id = $(c).attr("val");
		var json = {
			title : "",
			text : "确定发送邮件吗?",
			showCancelButton : true,
			type : 'warning',
			call : function() {
				var href = $.webapp.root + '/admin/system/user/notice.do';
				 $.post(href, {
					id : id
				}, function(result) {
					$person_table.bootstrapTable('refresh');
					$.BOOT.toast1(result);
				}, 'json');
			}
		};
		$.BOOT.alert(json, true);
	});
	
	
	$(document).on("click", "#person_add", function() {
		var href = $.webapp.root + '/admin/system/user/person_form_UI.do';
		$.BOOT.page("content_addusers", href, function() {
			$('#addusersModal').modal('toggle');
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
				$person_table.bootstrapTable('refresh');
				$.BOOT.toast1(result);
			}
		};
		$("#formhomepage-user").ajaxSubmit(options);
	}
	
	$("input#pitch").parent().parent().click(function(){
		alert("ok");
	})

</script>