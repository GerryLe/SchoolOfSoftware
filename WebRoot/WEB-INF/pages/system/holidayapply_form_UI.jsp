<%@page import="com.rosense.module.common.web.servlet.WebContextUtil"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<link rel="stylesheet" href="${webRoot}/template/resource/plugins/upload/css/default.css" />
<link rel="stylesheet" href="${webRoot}/template/resource/plugins/upload/css/fileinput.min.css" />
<script src="${webRoot}/template/resource/plugins/jquery/jquery.form.no.js"></script>
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
	$(function() {
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
		if ($('input[name=id]').val().length > 0) {//更新，审核
			$.post(get, {
				id : $('input[name=id]').val()
			}, function(result) {//根据id获取请假信息
				state(result);
				$('#form_addholidayapplys').form('load', result);
				$("#look").attr('href',result.enclosure);
				$("#img-thumbnail").attr("src",result.enclosure);
				$("#looktwo").attr('href',result.enclosuretwo);
				$("#img-thumbnailtwo").attr("src",result.enclosuretwo);
				if(result.holiapplyhrapproval==1){
					$("#hrApproval").val("通过");
				}
				if(result.holiapplyhrapproval==2){
					$("#hrApproval").val("不通过");
				}
				if($("#look").attr("href")==""){
					$("#look").removeAttr('href');
				}
				if($("#looktwo").attr("href")==""){
					$("#looktwo").removeAttr('href');
				}
				$.post($.webapp.root + '/admin/system/holidayapplys/getaudit.do', {
					id : $("input[name=id]").val()
				}, function(data) {
					if(data.status==false&&$("input[name=uid]").val()==$("input[name=userId]").val()){//获取自己假期申请内容
							$.get($.webapp.root + '/admin/system/holidayapplys/getLimit.do?id='+ $("input[name=id]").val(),function(s){
								if(s.status==true){//未审核可以修改，暂时不具更新功能
								  	//更新时先拆除原来记录，按保存时新增，取消是回滚
								    /* $.get($.webapp.root + "/admin/system/holidayapplys/delete.do",{id:$("input[name=id]").val()},function(){
									})   */
									
									if(result.director2!=null){
										$('tr#special').each(function(){
											$(this).show();
										})
									}
									$("#director").removeAttr("disabled");
									$("#holiapplyremark").removeAttr("disabled");
									$("#holiapplyName").removeAttr("disabled");
									$("#holiapplyContent").removeAttr("disabled");
									$("#disabled").removeAttr("disabled");
									$("#startHours").removeAttr("disabled");
									$("#endHours").removeAttr("disabled");
									$("#Filedata").removeAttr("disabled");
									$("#Filedatatwo").removeAttr("disabled");
									$('#holiapplyStartDate').removeAttr("disabled");
									$('#holiapplyEndDate').removeAttr("disabled");
									$('#director2').removeAttr("disabled");
									showSave(); 
									form_url = $.webapp.root+ "/admin/system/holidayapplys/update.do";
							        $('#form_addholidayapplys').form('load', result);
								}else{//已经审核或者通过审核,不能修改
									//特殊情况
									if(result.director2!=null){
										$('tr#special').each(function(){
											$(this).show();
										})
									}
									hideAll1();										
								}
							},"json");
					}else if(data.status==true){//获取审核请假信息
						//直属上级审核
						if(userName==result.director&&result.holiapplydirectorsapproval==0){
							$('#holiapplydirectorsopinion').removeAttr("disabled");
							showAudit();
						}
						//特殊情况
						else if(userName==result.director2&&result.holiapplydirectorsapproval2==0){
							$('#holiapplydirectorsopinion2').removeAttr("disabled");
							showAudit();
						}
						//HR审核归档
						else{
							$('#holiapplyhropinion').removeAttr("disabled");
							showAudit();
						}
						if(result.director2!=null){
							$('tr#special').each(function(){
								$(this).show();
							})
						}
					}else if(data.status==false){//HR查看已经审核过的信息
						if(result.director2!=null){
							$('tr#special').each(function(){
								$(this).show();
							})
						}
						hideAll1();
					}
				},"json")
			}, 'json');
		}
		else if ($('input[name=id]').val().length == 0) {//新增
			$.post(loading, {
				id : $('input[name=userId]').val()
			}, function(result) {
				//状态
				state(result);
				$("tr#addDirector").show();
				$("#director").removeAttr("disabled");
				$('#form_addholidayapplys').form('load', result);
				$("#holiapplyremark").removeAttr("disabled");
				$("#holiapplyName").removeAttr("disabled");
				$("#holiapplyContent").removeAttr("disabled");
				$("#disabled").removeAttr("disabled");
				$("#startHours").removeAttr("disabled");
				$("#endHours").removeAttr("disabled");
				$("#Filedata").removeAttr("disabled");
				$("#Filedatatwo").removeAttr("disabled");
				$('#holiapplyStartDate').removeAttr("disabled");
				$('#holiapplyEndDate').removeAttr("disabled");
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
	$.BOOT.click("#permit_addholidayapplys", function(c) {
		//参数通过
		form_url = $.webapp.root + '/admin/system/holidayapplys/true.do?pr=pass';
	});
	$.BOOT.click("#refuse_addholidayapplys", function(c) {
		//参数拒绝
		form_url = $.webapp.root + '/admin/system/holidayapplys/true.do?pr=refuse';
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
			$("#holiapplyDays").val(day);
			$.get('/admin/system/holidayapplys/getholidayexit.do',
					{holiapplyStartDate:startDate,holiapplyEndDate:endDate,startHours:startHours,endHours:endHours,holiapplyName:$("#holiapplyName").val(),holiapplyDays:$("#holiapplyDays").val()},
					function(data){
				if(data.status==false){
					$.BOOT.toast1(data);
					$("#save_addholidayapplys").attr("disabled","disabled");
				}else{
					$("#save_addholidayapplys").removeAttr("disabled");
				}
			},"json")
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
		
		if(result.holiapplydirectorsapproval2=="0"){
			$("input#d2").val("未审核");
			$("input#d2").css("color","blue");
		}else if(result.holiapplydirectorsapproval2=="1"){
			$("input#d2").val("通过");
		}else if(result.holiapplydirectorsapproval2=="2"){
			$("input#d2").val("不通过");
			$("input#d2").css("color","red");
		}
		
		if(result.holiapplyhrapproval=="0"){
			$("input#hr").val("未审核");
			$("input#hr").css("color","blue");
		}else if(result.holiapplyhrapproval=="1"){
			$("input#hr").val("通过");
		}else if(result.holiapplyhrapproval=="2"){
			$("input#hr").val("不通过");
			$("input#hr").css("color","red");
		}
	}
	$("input#director").on('keyup',function(){
		var getdir = $.webapp.root + '/admin/system/holidayapplys/getDirector.do?name='+$(this).val();
		$.get(getdir,{},function(dir){
				$("ul#list-director").empty();
			for(var i = 0 ; i<dir.length ; i++){
				$("ul#list-director").append("<li class='list-group-item' id='"+dir[i].name+"' onclick='select(this)' style='width:134px;'><label for='"+dir[i].name+"'>"+dir[i].name+"</label></li>")
			}
		},"json")
	});
	$("input#director2").on('keyup',function(){
		var getdir = $.webapp.root + '/admin/system/holidayapplys/getDirector.do?name='+$(this).val();;
		$.get(getdir,{},function(dir){
				$("ul#list-director2").empty();
			for(var i = 0 ; i<dir.length ; i++){
				$("ul#list-director2").append("<li class='list-group-item' id='"+dir[i].name+"' onclick='select(this)' style='width:134px;'><label for='"+dir[i].name+"'>"+dir[i].name+"</label></li>")
			}
		},"json")
	});
	$("section.content").on('click',function(){
		$("ul#list-director").empty();
		$("ul#list-director2").empty();
	});
	function select(c){
		$(c).parent().parent().find("input").val($(c).attr('id'));
		$(c).parent().empty();
	};
	$("button#addDirector").on('click',function(){
		$('tr#special').each(function(){
			$(this).show();
		})
		$("#director2").removeAttr("disabled");
		$("tr#addDirector").hide();
	})
	
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
				<td colspan="5" ><input type="text" name="positionname" id="positionname" disabled="disabled" /></td>
			</tr>
			<tr class="form-group">
				<td>休假类型:</td>
				<td>
					<select name="holiapplyName" id="holiapplyName" disabled="disabled">
						<option value="年假">事假</option>
						<option value="病假">病假</option>
						<option value="其它">其它</option>
					</select>
				</td>
				<td colspan="2">如果选择了其它，请在这里注明：</td>
				<td colspan="2"><input type="text" name="holiapplyremark" id="holiapplyremark" disabled="disabled" /></td>
			</tr>
			<tr class="form-group">
				<td>请假时间：</td>
				<td>
				<div class="input-append date"  id="holiapplyStartDate1" data-date="" data-date-format="yyyy-mm-dd" >
	   	 			<input size="16" type="text" name="holiapplyStartDate" id="holiapplyStartDate" disabled="disabled" onchange="getholidayday()" readonly>
	    			<span class="add-on"><i class="icon-th"></i></span>
				</div>
				</td>
				<td>
					<select id="startHours" name="startHours" style="width: 60px" disabled="disabled" onchange="getholidayday()">
						<option value="09">09:00</option><option value="14">14:00</option>
					</select>
				</td>
				<td>
				<div class="input-append date"  id="holiapplyEndDate1" data-date="" data-date-format="yyyy-mm-dd" >
	   	 			<input size="16" type="text" name="holiapplyEndDate" id="holiapplyEndDate" disabled="disabled" onchange="getholidayday()" readonly>
	    			<span class="add-on"><i class="icon-th"></i></span>
				</div>
				</td>
				<td>
					<select name="endHours" id="endHours" style="width: 60px" disabled="disabled" onchange="getholidayday()">
						<option value="14">14:00</option><option value="18">18:00</option>
					</select>
				</td>
				<td>共<input type="text" name="holiapplyDays" id="holiapplyDays" style="width: 45px" readonly />天</td>
			</tr>
			<tr style="height: 45px;" class="form-group">
				<td>申请人：</td>
				<td colspan="2"><input type="text" name="holiapplyUserName" id="holiapplyUserName" disabled="disabled"  /></td>
				<td>申请时间：</td>
				<td colspan="2"><input type="text" name="applyForTime" id="applyForTime" disabled="disabled" /></td>
			</tr>
			<tr class="form-group">
				<td>辅导员：</td>
				<td colspan="2"><input type="text" name="director" id="director"  disabled="disabled"/></td>
				<td>审批时间：</td>
				<td colspan="2"><input type="text" name="approvalTime" id="approvalTime" disabled="disabled" /></td>
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
									<input class="fileInput" type="file" name="Filedata" id="Filedata" onchange="uploadImg()" disabled="disabled">
								</div>
							</form>
							<form id="formhomepage-2">
								<div class="fileview">
									<a id="looktwo"  href="" target="_blank">
										<img id="img-thumbnailtwo" class="img-thumbnail" src="" alt="还没有上传图片">
									</a>
								</div>
								<div class="fileInputContainer">
									<input class="fileInput" type="file" name="Filedata" id="Filedatatwo" onchange="uploadImgtwo()" disabled="disabled">
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
				<td colspan="6"><textarea  name="holiapplyContent" id="holiapplyContent"  rows="5" style="width: 100%" disabled="disabled"></textarea></td>
			</tr>
			<tr class="form-group">
				<td colspan="6">辅导员审批意见：</td>
			</tr>
			<tr class="form-group">
				<td colspan="6"><textarea  name="holiapplydirectorsopinion" id="holiapplydirectorsopinion"  rows="5" style="width: 100%" disabled="disabled"></textarea></td>
			</tr>
		</table>
	</div>
</section>