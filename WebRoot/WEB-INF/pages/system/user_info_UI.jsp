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
	
	
		<div class="panel panel-default" style="display: none">
			 <div class="panel-heading" role="tab" id="headingOne">
				<h4 class="panel-title">
					<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne"> 修改资料 </a>
				</h4>
			</div> 
			<div id="collapseOne" class="panel-collapse collapse in" role="tabpanel">
				<div class="panel-body row">
				   <form id="form-user-info" method="post">
						<input type="hidden" name="id" value="<%=WebContextUtil.getUserId()%>">
					<div class="form-group col-md-6 user_inf">
						<label for="chinaname">中文名</label> 
					<input type="text" class="form-control" name="chinaname" id="chinaname" placeholder="输入中文名">
					</div>
					
					<div class="form-group col-md-6">
						<label for="name">英文名</label> 
						<input type="text" class="form-control" name="name" id="name" placeholder="输入英文名">
					</div>
					
					<div class="form-group col-md-6">
						<label for="email">邮箱</label> 
						<input type="email" class="form-control" name="email" id="email" placeholder="输入邮箱">
					</div>
					
					<div class="form-group col-md-6">
						<label for="phone">手机<span id="phonereg" style="color: red; font-size: 12px;  display：none;"></span></label> 
						<input type="tel" class="form-control" name="phone" id="phone" placeholder="输入手机">
					</div>
					
					<div class="form-group col-md-6">
						<label for="sex">性别</label> 
						<select class="form-control" id="sex" name="sex">
							<option value="男">男</option>
							<option value="女">女</option>
						</select>
					</div>
					
					<div class="form-group col-md-6">
						<label for="province">地区</label>
						<div class="form-control formselect" style="border:none">
						<select name="province" id="province" style="height: 30px;width:120px;"></select> 
				        <select name="city" id="city" style="height: 30px;width:120px;"></select> 
				        <select id="area" name="area" style="display: none"></select>
				        </div> 
					</div>
					
					<div class="form-group col-md-6 user_inf" >
						<label for="orgId">部门</label>
						 <select class="form-control" id="orgId" name="orgId"> </select>
					</div>
					
					<div class="form-group col-md-6 user_inf">
						<label for="positionId">岗位</label>
						<select class="form-control" id="positionId" name="positionId"> </select>
					</div>
					
					<div class="form-group col-md-6 user_inf">
						<label for="employmentStr">入职日期</label> 
						  <div class="input-group date form_dateu col-md-5" data-date="" data-date-format="yyyy-mm-dd" data-link-field="employmentDate" data-link-format="yyyy-mm-dd">
					                   <input class=""  type="text" id="employmentStr" name="employmentStr" readonly >
					                   <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
									<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					      </div>
						 <!--  <input type="hidden" id="employmentDate" name="employmentDate" value="" /><br/>--> 
				    </div>
						<div class="form-group col-md-12">
							<button class="btn btn-success phone-success" style="float: right;" type="submit">保存</button>
						</div>
					</form>
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
	 var orgtree = $.webapp.root + "/admin/system/org/tree.do";
	 var ptree = $.webapp.root + "/admin/system/position/tree.do";
	 var $person_table;
	$(function() {
		var href = $.webapp.root + "/admin/system/user/get.do";
		form_url = $.webapp.root + "/admin/system/user/update.do";
		region_init("province","city","area");
		$.BOOT.autoselect("orgId", orgtree, {
			title : "选择部门"
		});
		$.BOOT.autoselect("positionId", ptree, {
			title : "选择职位"
		});
		$('.form_dateu').datetimepicker({
	        language:  'zh-CN',
	        weekStart: 1,
	        todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			minView: 2,
			forceParse: 0
	    });
		if ($('input[name=id]').val().length > 0) {
			$.post(href, {
				id : $('input[name=id]').val()
			}, function(result) {
				$('#form-user-info').form('load', result);
			}, 'json');
		}
		
		$person_table = $.BOOT.table("person_table", $.webapp.root
				+ "/admin/system/user/datagridpersonal.do", {
					columns : [ {
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
						title : '联系手机',
					}, {
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
						field : 'email',
						title : '邮箱'
					},  {
						field : 'degree',
						title : '学历'
					}, {
						field : 'birthday',
						title : '出生年月'
					},{
						field : 'employmentStr',
						title : '入职日期',
					},{
						field : 'workAge',
						title : '工龄',
					},{
						field : 'becomeStaffDate',
						title : '转正日期',
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
						field : 'certificate',
						title : '证书',
					},{
						field : 'leaveDate',
						title : '离职日期',
					}],
			paginationInfo : true,
			showExport : true
		});

		
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
 	
 	$("#phone").blur(function(){
		 var flag=false;
	      var message = "";
	      //var myreg = /^(((13[0-9]{1})|(14[0-9]{1})|(17[0]{1})|(15[0-3]{1})|(15[5-9]{1})|(18[0-9]{1}))+\d{8})$/;  
	      var myreg=/^1[3|4|5|8]\d{9}$/;
		  phone = this.value;
	      if(phone.length !=11){
	        message = "请输入有效的手机号码,为11位！";
	      }else if(!myreg.test(phone)){
	        message = "请输入有效的手机号码！";
	      }else{
	    	  $("#phonereg").hide();
	    	  $(".phone-success").removeAttr("disabled"); 
	    	  flag=true;
	      }
	      if(!flag){
	     //提示错误效果
	        $("#phonereg").html(message);
	        $(".phone-success").attr("disabled", true);
	      }
	      
	}); 
 	
</script>