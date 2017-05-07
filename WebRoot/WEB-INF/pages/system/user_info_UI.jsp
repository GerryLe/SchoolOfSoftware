<%@page import="com.rosense.module.common.web.servlet.WebContextUtil"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<link rel="stylesheet" href="${webRoot}/template/resource/plugins/upload/css/default.css" />
<link rel="stylesheet" href="${webRoot}/template/resource/plugins/upload/css/fileinput.min.css" />
<script src="${webRoot}/template/resource/plugins/jquery/jquery.form.no.js"></script>
<script src="${webRoot}/template/resource/js/city.js"></script>
<style>
<!--
.fileInputContainer {
	margin: 0px;
	padding: 0px;
	height: 100px;
	width: 100px;
	background-image: url('/template/images/default.jpg');
	background-repeat: no-repeat;
	background-size: 100% 100%;
	-moz-background-size: 100% 100%;
	-webkit-background-size: 100% 100%;
	position: relative;
	font-size: 14px;
	line-height: 20px;
	float: left;
	border: 1px solid #ccc;
	margin-right: 3px;
}

.fileInputContainer input {
	margin: 0px;
	padding: 0px;
	width: 100%;
	height: 100px;
	opacity: 0;
	cursor: pointer;
}

.fileview {
	position: relative;
	float: left;
	text-align: center;
	margin-right: 3px;
}

.fileview img {
	height: 100px;
	width: 100px;
}

.fileview i {
	position: absolute;
	top: 5px;
	right: 5px;
	font-size: 30px;
	color: red;
}
.user_inf{
 display: none;
}
.fixed-table-toolbar .columns-right {
display:none;
}
#person_table{
  width: 4500px;
}
.fixed-table-header{
   margin-right: 0px;
   display:none;
}
.table .btn-group{
   position:relative; 
}
.fixed-table-container{
    height: 100%;
}
-->
</style>
<section class="content">
	<div class="panel-group" id="accordion" role="tablist">
	<div class="panel panel-default">
			<div class="panel-heading" role="tab" id="headingTwo">
				<h4 class="panel-title">
					<a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseFive"> 查看资料 </a>
				</h4>
			</div>
			<div id="collapseFive" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingFive">
				<div class="panel-body">
					<!-- <section class="content"> -->
						<table id="person_table" class="table-condensed table table-hover"
							data-row-style="rowStyle" data-side-pagination="server"></table>
		              <!-- </section> -->
				</div>
			</div>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading" role="tab" id="headingOne">
				<h4 class="panel-title">
					<a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo"> 修改密码 </a>
				</h4>
			</div>
			<div id="collapseTwo" class="panel-collapse collapse" role="tabpanel">
				<div class="panel-body">
					<form id="mypass-form-edit" method="post">
						<input type="hidden" name="id" value="${id}">
						<div class="form-group">
							<label>旧密码</label> <input type="password" class="form-control" name="oldPwd" placeholder="输入旧密码">
						</div>
						<div class="form-group">
							<label>新密码</label> <input type="password" class="form-control" name="password" placeholder="输入新密码">
						</div>
						<div class="form-group">
							<label>确认新密码</label> <input type="password" class="form-control" name="confirm_password" placeholder="再次输入新密码">
						</div>
						<div class="form-group">
							<button class="btn btn-success" style="float: right;" type="submit">保存</button>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading" role="tab" id="headingThree">
				<h4 class="panel-title">
					<a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseThree"> 修改头像 </a>
				</h4>
			</div>
			<div id="collapseThree" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingThree">
				<div class="panel-body">
					<div id="homepage-div-id">
						<form id="formhomepage-0">
							<div class="fileview">
								<img id="img-thumbnail" class="img-thumbnail" src="<%=WebContextUtil.getCurrentUser().getUser().getPhoto()%>" alt="还没有设置头像">
							</div>
							<div class="fileInputContainer">
								<input class="fileInput" type="file" name="Filedata" onchange="uploadImg()">
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>
<script>
     var flag=false;
	 var form_url = null;
	 var $person_table;
	 var currentRole=<%=WebContextUtil.getCurrentUser().getUser().getDefaultRole()%>
	$(function() {
		var href = $.webapp.root + "/admin/system/user/get.do";
		form_url = $.webapp.root + "/admin/system/user/update.do";
		if ($('input[name=id]').val().length > 0) {
			$.post(href, {
				id : $('input[name=id]').val()
			}, function(result) {
				$('#form-user-info').form('load', result);
			}, 'json');
		}
		if(currentRole==4){
		 $person_table = $.BOOT.table("person_table", $.webapp.root
				+ "/admin/system/student/datagridpersonal.do", {
					columns : [ {
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
					}],
			paginationInfo : true,
			showExport : true
		  });
		}else{
			$person_table = $.BOOT.table("person_table", $.webapp.root
					+ "/admin/system/teacher/datagridpersonal.do", {
						columns : [ {
							field : 'tea_no',
							title : '编号',
							sortable : true
						}, {
							field : 'tea_name',
							title : '姓名',
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
							field : 'contact',
							title : '联系人',
						},{
							field : 'contactPhone',
							title : '联系人电话',
						},{
							field : 'material',
							title : '资料',
						}],
				paginationInfo : true,
				showExport : true
			  });
		}
	}); 
	
	$.BOOT.form("form-user-info", {}, function(params) {
		$.post(form_url, params, function(result) {
			$.BOOT.toast1(result);
		}, 'json');
	});
	
	$.BOOT.form("mypass-form-edit", {
		oldPwd : {
			validators : {
				notEmpty : {
					message : '密码不能为空'
				}
			}
		},
		password : {
			validators : {
				notEmpty : {
					message : '密码不能为空'
				},
				identical : {
					field : 'confirm_password',
					message : '两次密码输入不一样'
				}
			}
		},
		confirm_password : {
			validators : {
				notEmpty : {
					message : '密码不能为空'
				},
				identical : {
					field : 'password',
					message : '两次密码输入不一样'
				}
			}
		}
	}, function(params) {
		var form_url = $.webapp.root + "/admin/system/user/updatepwd.do";
		$.post(form_url, params, function(result) {
			$.BOOT.toast1(result);
		}, 'json');
	});
	
	
 	function uploadImg() {
		var options = {
			url : '/admin/system/user/updatephoto.do',
			type : 'post',
			dataType : 'json',
			success : function(data) {
				$("#img-thumbnail").attr("src",data.obj);
			}
		};
		$("#formhomepage-0").ajaxSubmit(options);
	}
</script>