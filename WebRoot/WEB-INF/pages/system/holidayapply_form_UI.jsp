<%@page import="com.rosense.module.common.web.servlet.WebContextUtil"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<link rel="stylesheet" href="${webRoot}/template/resource/plugins/upload/css/default.css" />
<link rel="stylesheet" href="${webRoot}/template/resource/plugins/upload/css/fileinput.min.css" />
<link rel="stylesheet" type="text/css" href="${webRoot}/template/resource/css/bootstrap-clockpicker.min.css">
<script src="${webRoot}/template/resource/plugins/jquery/jquery.form.no.js"></script>
<script type="text/javascript" src="${webRoot}/template/resource/js/bootstrap-clockpicker.min.js"></script>
<script src="${webRoot}/template/resource/js/city.js"></script>
<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<style>
<!--
.modal-dialog{
 width:60%;
 height:80%;
 text-align: center;

}
 .employ {
	border:1px solid black;
	width:600px;
	height:800px;
	margin-left: 18px;
	font-size: 13px;
}
 .employ tr td{
  border:1px solid black;
   padding: 0;
}
 input{
  /* border: 0px; */
  text-align: center;
} 
.fileview img {
	height: 100px;
	width: 100px;
}
.fileview {
	position: relative;
	float: left;
	text-align: center;
	margin-right: 3px;
}
.fileview i {
	position: absolute;
	top: 5px;
	right: 5px;
	font-size: 30px;
	color: red;
}
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

-->
</style>
<script>
	var form_url = $.webapp.root + "/admin/system/holidayapplys/add.do";
	var parent_box, holidayapplys_type;
	var flag=true;
	var defaultRole='<%=WebContextUtil.getCurrentUser().getUser().getDefaultRole()%>';
	var userId=null;
	$(function() {
		 $('.clockpicker').clockpicker();
		//编辑，加载表单数据
		var get = $.webapp.root + "/admin/system/holidayapplys/get.do";
		//申请，加载用户数据
		var loading = $.webapp.root + "/admin/system/holidayapplys/loading.do";
		$('#holiapplyStartDate1').datetimepicker({
			minView:2 ,
            language:  'zh-CN',
	        todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			forceParse: 0,
			format: 'yyyy-mm-dd',
			weekStart: 1, 
	    });
		$('#holiapplyEndDate1').datetimepicker({
			minView:2 ,
            language:  'zh-CN',
	        todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			forceParse: 0,
			format: 'yyyy-mm-dd',
			weekStart: 1, 
	    });
		$('#approvalTime').attr("disabled","disabled");
		if ($('input[name=id]').val().length > 0) {//更新，审核
			userId=$('input[name=id]').val();
			$.post(get, {
				id : $('input[name=id]').val()
			}, function(result) {//根据id获取请假信息
				state(result);
				$('#form_addholidayapplys').form('load', result);
				$("#look").attr('href',result.enclosure);
				$("#img-thumbnail").attr("src",result.enclosure);
				$("#looktwo").attr('href',result.enclosuretwo);
				$("#img-thumbnailtwo").attr("src",result.enclosuretwo);
				if(result.holiapplydirectorsapproval==1){
					$("#directorsApproval").val("通过");
				}
				if(result.holiapplyhrapproval==2){
					$("#directorsApproval").val("不通过");
				}
				if($("#look").attr("href")==""){
					$("#look").removeAttr('href');
				}
				if($("#looktwo").attr("href")==""){
					$("#looktwo").removeAttr('href');
				}
				//当前用户为普通用户
				if(defaultRole==3||defaultRole==4){
					if(result.holiapplystatement==0){//未审核可以修改
						$('#holiapplydirectorsopinion').attr("disabled","disabled");
						showSave(); 
						form_url = $.webapp.root+ "/admin/system/holidayapplys/update.do";
					}else{//已经审核或者通过审核,不能修改
						hideAll1();										
					}
				}else{//当前用户为辅导员
					$('input,textarea,select').attr("disabled","disabled");
					$('#holiapplydirectorsopinion').removeAttr("disabled");
					showAudit();
				}
			}, 'json');
		}
		else if ($('input[name=id]').val().length == 0) {//新增
			$.post(loading, {
				id : $('input[name=userId]').val()
			}, function(result) {
				//状态
				state(result);
				$('#form_addholidayapplys').form('load', result);
				$('#holiapplydirectorsopinion').attr("disabled","disabled");
				showSave();
			}, 'json');
		}
		if($("#look").attr("href")==""){
			$("#look").removeAttr('href');
		}
		if($("#looktwo").attr("href")==""){
			$("#looktwo").removeAttr('href');
		}
	});
	$.BOOT.click("#permit_addholidayapplys", function() {
		//参数通过
		form_url = $.webapp.root + '/admin/system/holidayapplys/true.do?pr=pass&id='+userId;
	});
	$.BOOT.click("#refuse_addholidayapplys", function() {
		//参数拒绝
		form_url = $.webapp.root + '/admin/system/holidayapplys/true.do?pr=refuse&id='+userId;
	});
 	$.BOOT.form("form_addholidayapplys", {},function(params) {
		if(flag){
		$.post(form_url, params, function(result) {
			$.BOOT.toast1(result);
			$holidayapplys_table.bootstrapTable('refresh');
			modaladdholidayapplys();
		}, 'json');
		flag=false
	}
	});

 	function uploadImg() {
		var options = {
			url : '/admin/system/holidayapplys/updateEnclosure.do',
			type : 'post',
			dataType : 'json',
			success : function(data) {
				$("#enclosure").val(data.obj);
				$("#img-thumbnail").attr("src",data.obj);
				$('#look').attr('href',data.obj); 
			}
		};
		$("#formhomepage-1").ajaxSubmit(options);
	}
	function uploadImgtwo() {
		var options = {
			url : '/admin/system/holidayapplys/updateEnclosure.do',
			type : 'post',
			dataType : 'json',
			success : function(data) {
				$("#enclosuretwo").val(data.obj);
				$("#img-thumbnailtwo").attr("src",data.obj);
				$('#looktwo').attr('href',data.obj); 
			}
		};
		$("#formhomepage-2").ajaxSubmit(options);
	}
	function getholidayday(){
		var startDate = $("#holiapplyStartDate").val();
		var endDate = $("#holiapplyEndDate").val();
		var startHours = $("#startHours").val();
		var endHours = $("#endHours").val();
		var holiday ;
		if(endDate!=""&&startDate!=""){
			var start = Date.parse(startDate)/1000;
			var end = Date.parse(endDate)/1000;
		    var day = (end - start)/(3600 * 24) - 1;
			if(startHours==9)
				day+=1;
			else if(startHours==14)
				day+=0.5;
			if(endHours==14)
				day+=0.5;
			else if(endHours==18)
				day+=1;
		}
	} 
	function showSave(){
		$("#permit_addholidayapplys").hide();
		$("#refuse_addholidayapplys").hide();
		$("#save_addholidayapplys").show();
		$("#save_addholidayapplys").removeAttr("disabled");
	}
	function showAudit(){
		$("#permit_addholidayapplys").show();
		$("#refuse_addholidayapplys").show();
		$("#save_addholidayapplys").hide();
		$("#permit_addholidayapplys").removeAttr("disabled");
		$("#refuse_addholidayapplys").removeAttr("disabled");
	}
	function hideAll(){
		$("#permit_addholidayapplys").hide();
		$("#refuse_addholidayapplys").hide();
		$("#save_addholidayapplys").show();
		$("#save_addholidayapplys").removeAttr("disabled");
	}
	function hideAll1(){
		$("#permit_addholidayapplys").hide();
		$("#refuse_addholidayapplys").hide();
		$("#save_addholidayapplys").hide();
	}
	//改变审核状态
	function state(result){
		if(result.holiapplydirectorsapproval=="0"){
			$("input#d1").val("未审核");
			$("input#d1").css("color","blue");
		}else if(result.holiapplydirectorsapproval=="1"){
			$("input#d1").val("通过");
		}else if(result.holiapplydirectorsapproval=="2"){
			$("input#d1").val("不通过");
			$("input#d1").css("color","red");
		}
	}
	
	var getdir = $.webapp.root + '/admin/system/holidayapplys/getDirector.do';
	$.post( getdir, "选择辅导员名字", function(result) {
		var select = $("#director");
		var recursion = function(data, deep) {
			for ( var n in data) {
				var text = "";
				for (var i = 0; i < deep; i++) {
					text += "&nbsp;&nbsp;";
				}
				text += data[n].name;
				select.append("<option value='" + data[n].name + "'>" + text
						+ "</option>");
				if (data[n].nodes) {
					recursion(data[n].nodes, deep + 1);
				}
				if (data[n].children) {
					recursion(data[n].children, deep + 1);
				}
			}
		};
		select.append("<option value=''>" + "选择辅导员名字" + "</option>");
		recursion(result, 1);
		if (params.callback) {
			params.callback();
		}
	}, 'json');
	
</script>
<section class="content">
	<div class="panel-group" id="accordion" role="tablist">
		<input type="hidden" name="id" value="${id}">
		<input type="hidden" name="uid" value="${uid}">
		<input type="hidden" name="param" value="${param}">
		<input type="hidden" name="defaultRole" value="<%=WebContextUtil.getDefaultRole()%>">
		<input type="hidden" name="userId" value="<%=WebContextUtil.getUserId()%>">
		<input type="hidden" name="enclosure" id="enclosure" value="${enclosure}">
		<input type="hidden" name="enclosuretwo" id="enclosuretwo" value="${enclosuretwo}">
		<table class="employ" style="cellspacing : 0; cellpadding : 0;" >
			<tr class="form-group">
				<td >班级名称：</td>
				<td colspan="5" ><input type="text" name="class_name" id="class_name"/></td>
			</tr>
			<tr class="form-group">
				<td>休假类型:</td>
				<td>
					<select name="holiapplyName" id="holiapplyName">
						<option value="年假">事假</option>
						<option value="病假">病假</option>
						<option value="其它">其它</option>
					</select>
				</td>
				<td colspan="2">如果选择了其它，请在这里注明：</td>
				<td colspan="2"><input type="text" name="holiapplyremark" id="holiapplyremark" /></td>
			</tr>
			<tr class="form-group">
				<td>请假时间：</td>
				<td>
				<div class="input-append date"  id="holiapplyStartDate1" data-date="" data-date-format="yyyy-mm-dd" >
	   	 			<input size="16" type="text" name="holiapplyStartDate" id="holiapplyStartDate" readonly>
	    			<span class="add-on"><i class="icon-th"></i></span>
				</div>
				</td>
				<td>
					<!-- <select id="startHours" name="startHours" style="width: 60px"  onchange="getholidayday()">
						<option value="08">08:00</option><option value="14">14:00</option>
					</select> -->
					<div class="input-group clockpicker" data-placement="right" data-align="top" data-autoclose="true" style="width: 100px">
						<input type="text" class="form-control" id="startHours" name="startHours"> <span
							class="input-group-addon"> <span class="glyphicon glyphicon-time"></span>
						</span>
					</div>
				</td>
				<td>
				<div class="input-append date"  id="holiapplyEndDate1" data-date="" data-date-format="yyyy-mm-dd" >
	   	 			<input size="16" type="text" name="holiapplyEndDate" id="holiapplyEndDate"  readonly>
	    			<span class="add-on"><i class="icon-th"></i></span>
				</div>
				</td>
				<td>
					<div class="input-group clockpicker" data-placement="right" data-align="top" data-autoclose="true" style="width: 100px">
						<input type="text" class="form-control" id="endHours" name="endHours"> <span
							class="input-group-addon"> <span class="glyphicon glyphicon-time"></span>
						</span>
					</div>
				</td>
				<td>共<input type="text" name="holiapplyDays" id="holiapplyDays" style="width: 45px"   onchange="getholidayday()"/>天</td>
			</tr>
			<tr style="height: 45px;" class="form-group">
				<td>申请人：</td>
				<td colspan="2"><input type="text" name="holiapplyUserName" id="holiapplyUserName"   /></td>
				<td>申请时间：</td>
				<td colspan="2"><input type="text" name="applyForTime" id="applyForTime"  /></td>
			</tr>
			<tr class="form-group">
				<td>辅导员：</td>
				<td colspan="2"><select id="director" name="director" style="width: 70%; height: 70%"></select></td>
				<td>审批时间：</td>
				<td colspan="2"><input type="text" name="approvalTime" id="approvalTime"  /></td>
			</tr>
			<tr class="form-group">
				<td>文件上传：</td>
				<td colspan="5">
				<div class="panel panel-default">
						<div id="homepage-div-id">
							<form id="formhomepage-1">
								<div class="fileview">
									<a id="look"  href="" target="_blank">
										<img id="img-thumbnail" class="img-thumbnail" src="" alt="还没有上传图片">
									</a>
								</div>
								<div class="fileInputContainer">
									<input class="fileInput" type="file" name="Filedata" id="Filedata" onchange="uploadImg()" >
								</div>
							</form>
							<form id="formhomepage-2">
								<div class="fileview">
									<a id="looktwo"  href="" target="_blank">
										<img id="img-thumbnailtwo" class="img-thumbnail" src="" alt="还没有上传图片">
									</a>
								</div>
								<div class="fileInputContainer">
									<input class="fileInput" type="file" name="Filedata" id="Filedatatwo" onchange="uploadImgtwo()" >
								</div>
							</form>
						</div>
					</div>
				</td>
			</tr>
			<tr class="form-group">
				<td colspan="6">申请事由：</td>
			</tr>
			<tr class="form-group">
				<td colspan="6"><textarea  name="holiapplyContent" id="holiapplyContent"  rows="5" style="width: 100%" ></textarea></td>
			</tr>
			<tr class="form-group">
				<td colspan="2">辅导员审批意见：</td>
				<td colspan="4"><input name="d1" id="d1" readonly type="text" disabled="disabled" style="width: 100%"></td>
			</tr>
			<tr class="form-group">
				<td colspan="6"><textarea  name="holiapplydirectorsopinion" id="holiapplydirectorsopinion"  rows="5" style="width: 100%" ></textarea></td>
			</tr>
		</table>
	</div>
</section>