<%@page import="com.rosense.basic.util.LangUtils"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
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
	border:1px solid black;
	width:680px;
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
	var form_url = $.webapp.root + "/admin/system/Extraworkapplys/add.do";
	var parent_box, Extraworkapplys_type;
	var flag=true;

	$(function() {
		//编辑，加载表单数据
		var get = $.webapp.root + "/admin/system/Extraworkapplys/get.do";
		 var newDate = new Date();
		 var t = newDate.toJSON(); 
			$('#extworkapplystartdata1').datetimepicker({
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
			$('#extworkapplyenddata1').datetimepicker({
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
	});
	
	$.BOOT.form("form_addExtraworkapplys", {
		extworkapplydays :{
			validators :{
				notEmpty :{
					message : '申请天数不能为空'
				}
			}
		},
		extworkapplycontent :{
			validators :{
				notEmpty :{
					message : '申请加班事由不能为空'
				}
			}
		},
		
	}, function(params) {
		if(flag){-
		$.post(form_url, params, function(result) {
			$.BOOT.toast1(result);
			$Extraworkapplys_table.bootstrapTable('refresh');
			modaladdExtraworkapplys();
		}, 'json');
		flag=false
	}
	});

</script>
<section class="content">
	<div class="panel-group" id="accordion" role="tablist">
		<input type="hidden" name="id" value="${id}">
		<table class="employ" style="cellspacing: 0; cellpadding: 0;">
			<tr class="form-group">
				<td>部门：</td>
				<td colspan="5"><input type="text" name="positionname" id="positionname" disabled/></td>
			</tr>
			<tr class="form-group">
				<td>加班时间：</td>
				<td>
					<div class="input-append date"  id="extworkapplystartdata1" data-date="" data-date-format="yyyy-mm-dd" >
	   	 				从<input size="16" type="text" name="extworkapplystartdata" id="extworkapplystartdata" onchange="" readonly disabled>
	    				<span class="add-on"><i class="icon-th"></i></span>
					</div>
				</td>
				<td><select name="startHours">
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
						<option value="6">6</option>
						<option value="7">7</option>
						<option value="8">8</option>
						<option value="9">9</option>
						<option value="10">10</option>
						<option value="11">11</option>
						<option value="12">12</option>
						<option value="13">13</option>
						<option value="14">14</option>
						<option value="15">15</option>
						<option value="16">16</option>
						<option value="17">17</option>
						<option value="18">18</option>
						<option value="19">19</option>
						<option value="20">20</option>
						<option value="21">21</option>
						<option value="22">22</option>
						<option value="23">23</option>
						<option value="24">24</option>
				</select> 时</td>
				<td>
					<div class="input-append date"  id="extworkapplyenddata1" data-date="" data-date-format="yyyy-mm-dd" >
	   	 				至<input size="16" type="text" name="extworkapplyenddata" id="extworkapplyenddata" onchange="" readonly disabled>
	    				<span class="add-on"><i class="icon-th"></i></span>
					</div>
				</td>
				<td><select name="endHours">
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
						<option value="6">6</option>
						<option value="7">7</option>
						<option value="8">8</option>
						<option value="9">9</option>
						<option value="10">10</option>
						<option value="11">11</option>
						<option value="12">12</option>
						<option value="13">13</option>
						<option value="14">14</option>
						<option value="15">15</option>
						<option value="16">16</option>
						<option value="17">17</option>
						<option value="18">18</option>
						<option value="19">19</option>
						<option value="20">20</option>
						<option value="21">21</option>
						<option value="22">22</option>
						<option value="23">23</option>
						<option value="24">24</option>
				</select> 时</td>
				<td>共<input type="text" name="holiapplyDays" id="holiapplyDays"
					style="width: 20px" disabled/>天
				</td>
			</tr>
			<tr style="height: 45px;" class="form-group">
				<td>申请人：</td>
				<td colspan="2"><input type="text" name="holiapplyUserName"
					id="holiapplyUserName" disabled/></td>
				<td>申请时间：</td>
				<td colspan="2"><input type="text" name="applyForTime"
					id="applyForTime" disabled/></td>
			</tr>
			<tr class="form-group">
				<td>主管：</td>
				<td colspan="2"><input type="text" name="*" id=* disabled/></td>
				<td>审批时间：</td>
				<td colspan="2"><input type="text" name="examinationime"
					id="examinationTime" disabled/></td>
			</tr>
			<tr class="form-group">
				<td colspan="6">加班事由：</td>
			</tr>
			<tr class="form-group">
				<td colspan="6"><textarea name="extworkapplycontent"
						id="extworkapplycontent" rows="5" style="width: 100%" disabled></textarea></td>
			</tr>
			<tr class="form-group">
				<td colspan="6">主管意见：</td>
			</tr>
			<tr class="form-group">
				<td colspan="6"><textarea name="*" id="*" rows="5"
						style="width: 100%" disabled></textarea></td>
			</tr>
			<tr class="form-group">
				<td>主管上级意见：</td>
				<td colspan="5"><input type="text" disabled/></td>
			</tr>
			<tr class="form-group">
				<td colspan="6"><textarea name="*" id="*" rows="5"
						style="width: 100%" disabled></textarea></td>
			</tr>
			<tr class="form-group">
				<td>总裁意见：</td>
				<td colspan="5"><input type="text" disabled/></td>
			</tr>
			<tr class="form-group">
				<td colspan="6"><textarea name="*" id="*" rows="5"
						style="width: 100%" disabled></textarea></td>
			</tr>
		</table>
	</div>
</section>