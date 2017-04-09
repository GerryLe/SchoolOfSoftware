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
 width:45%;
 height:80%;
 text-align: center;

}
 .employ {
	margin-left: 18px;
	font-size: 15px;
}
 .employ tr td{
   padding: 15px 15px 15px 15px; 
}
 input{ 
    border-color:#000000;   
    border-style:solid;   
    border-top-width:0px;   
    border-right-width:0px;   
    border-bottom-width:1px;   
    border-left-width:0px; 
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
	var parent_box, holidayapplys_type;
	var flag = true;//防止重复提交
	$(function() {
		//加载表单数据
		var get = $.webapp.root + "/admin/system/holidaysuser/select.do";		
		if ($('input[name=id]').val().length > 0) {
			$.post(get, {
				id : $('input[name=id]').val()
			}, function(result) {
				$("#save_addholidaysuser").hide();
				$('#form_addholidaysuser').form('load', result);				
			}, 'json');
		}
		//$("#save_addholidaysuser").show();
	});	
	

</script>
<section class="content">
	<div class="panel-group" id="accordion" role="tablist">

<input type="hidden" name="id" value="${id}">
	<label style="font-size: 18px;">假期统计单</label>
	<table class="employ" style="cellspacing : 0; cellpadding : 0;" >
		<tr>
			<td style="font-weight:bold;">员工编号：</td>
			<td><input type="text" name="useraccount" id="useraccount" readonly="readonly" ></td>
			<td style="font-weight:bold;">行政级别：</td>
			<td><input type="text" name="grade" id="grade" readonly="readonly" ></td>
		</tr>
		<tr>
			<td style="font-weight:bold;">部门：</td>
			<td><input type="text" name="positionname" id="positionname" readonly="readonly" ></td>
			<td style="font-weight:bold;">工龄：</td>
			<td><input type="text" name="workAge" id="workAge" readonly="readonly" ></td>
			<td>年</td>
		</tr>
		<tr>
			<td style="font-weight:bold;">姓名：</td>
			<td><input type="text" name="name" id="name" readonly="readonly" ></td>
		</tr>
		<tr>
			<td style="font-weight:bold;">当前月份：</td>
			<td><input type="text" name="month" id="month" readonly="readonly" ></td>
		</tr>
		</table>
		<table class="employ" style="cellspacing : 0; cellpadding : 0;" >
		<tr>
			<td style="font-weight:bold;">上年剩余司龄假：</td>
			<td><input type="text" name="lastyearsremainingSiLingfalse" id="lastyearsremainingSiLingfalse" readonly="readonly" ></td>
			<td>天</td>
		</tr>
		<tr>
			<td style="font-weight:bold;">上年剩余补休假：</td>
			<td><input type="text" name="lastyearsremainingpaidleave" id="lastyearsremainingpaidleave" readonly="readonly" ></td>
			<td>天</td>
		</tr>
		<tr>
			<td style="font-weight:bold;">今年应有的年假：</td>
			<td><input type="text" name="shouldhaveannualleave" id="shouldhaveannualleave" readonly="readonly" ></td>
			<td>天</td>
		</tr>
		<tr>
			<td style="font-weight:bold;">今年应有的司龄假：</td>
			<td><input type="text" name="thisyearshouldbeSiLingfalse" id="thisyearshouldbeSiLingfalse" readonly="readonly" ></td>
			<td>天</td>
		</tr>
		<tr>
			<td style="font-weight:bold;">本年度可支取补休假：</td>
			<td><input type="text" name="thisyearsdaysworkovertime" id="thisyearsdaysworkovertime" readonly="readonly" ></td>
			<td>天</td>
		</tr>
		<tr>
			<td style="font-weight:bold;">本年度已支年假：</td>
			<td><input type="text" name="alreadyAnnualLeave" id="alreadyAnnualLeave" readonly="readonly" ></td>
			<td>天</td>
		</tr>
		<tr>
			<td style="font-weight:bold;">本年度已支司龄假：</td>
			<td><input type="text" name="alreadySiLingFalse" id="alreadySiLingFalse" readonly="readonly" ></td>
			<td>天</td>
		</tr>
		<tr>
			<td style="font-weight:bold;">本年度已支补休：</td>
			<td><input type="text" name="haveToTakePaidLeave" id="haveToTakePaidLeave" readonly="readonly" ></td>
			<td>天</td>
		</tr>
		<tr>
			<td style="font-weight:bold;">累计剩余年假：</td>
			<td><input type="text" name="theremainingannualleave" id="theremainingannualleave" readonly="readonly" ></td>
			<td>天</td>
			<td style="font-size: 12px; color : #f00;">(备注：年假不能带到下一年)</td>
		</tr>
		<tr>
			<td style="font-weight:bold;">累计剩余司龄假：</td>
			<td><input type="text" name="theremainingSiLingfalse" id="theremainingSiLingfalse" readonly="readonly" ></td>
			<td>天</td>
		</tr>
		<tr>
			<td style="font-weight:bold;">累计剩余补休假：</td>
			<td><input type="text" name="theremainingpaidleave" id="theremainingpaidleave" readonly="readonly" ></td>
			<td>天</td>
		</tr>
	</table>
</div>
</section>