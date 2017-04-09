<%@page import="com.rosense.basic.util.LangUtils"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script src="${webRoot}/template/resource/js/city.js"></script>
<style>
<!--
.modal-dialog {
	width: 50%;
	height: 80%;
	text-align: center;
}

.employ {
	border: 5px;
	width: 780px;
	height: 550px;
	text-align: center;
	margin-left: 23px;
}

.employ tr td {
	border: 1px solid black;
}

.trsolid {
	font-weight: bold;
}

.trleft {
	text-align: left;
}

.trcenter {
	text-align: center;
}

input {
	border: 0px;
	text-align: center;
}

.inputAge {
	border-top: 0px;
	border-left: 0px;
	border-right: 0px;
	border-bottom: 1px;
	width: 30px;
}
-->
</style>
<script>
	 var form_url = $.webapp.root + "/admin/system/employ/add.do";
	var employ_box, employ_type;
	var flag = true;//防止重复提交
	$(function() {
		//编辑，加载表单数据
		        var get = $.webapp.root + "/admin/system/employ/get.do";
				if ($('input[name=id]').val().length > 0) {
					$.post(get, {
						id : $('input[name=id]').val()
					}, function(result) {
						form_url = $.webapp.root+ "/admin/system/employ/update.do";
						if(result.applyName!=result.userName){
							$("input,textarea").attr("disabled", true);
							$("#save_addEmploy").hide();
						}
						else{
							
						}
						$('#form_addEmploy').form('load', result);
					}, 'json');
				} else {
				} 
				$("#save_addEmploy").show();
	});
      $.BOOT.form("form_addEmploy", {}, function(params) {
			if(flag){
			$.post(form_url, params, function(result) {
				$.BOOT.toast1(result);
				$employ_table.bootstrapTable('refresh');
				modaladdEmploy();
			}, 'json');
			flag = false;
		 }
	}); 
</script>
<div class="row">
	<input type="hidden" name="id" value="${id}">
	<label style="margin-left: 250px; font-size: 18px;">职工动态表</label>
	<table class="employ">
		<tr>
			<td colspan="5">Employee Action Report</td>
		</tr>

		<tr class="trsolid trcenter">
			<td>English Name 英文姓名</td>
			<td>Chinese Name 中文姓名</td>
			<td>Location/Department 地点/部门</td>
			<td>Effective Date 实施日期</td>
			<td>Staff No. 员工编号</td>
		</tr>

		<tr class="trcenter">
			<td><input type="text" /></td>
			<td><input type="text" /></td>
			<td><input type="text" /></td>
			<td><input type="text" /></td>
			<td><input type="text" /></td>
		</tr>

		<tr class="trsolid trcenter">
			<td>Dated Joined 入职日期</td>
			<td colspan="2">最新内部职位</td>
			<td>Internal Grade 公司内部级别</td>
			<td>Budget 预算状况</td>
		</tr>

		<tr class="trcenter">
			<td rowspan="5"><input type="text" /></td>
			<td>中文：</td>
			<td><input type="text" /></td>
			<td rowspan="2"><input type="text" /></td>
			<td rowspan="5"><input type="checkbox" style="width: 30px;" />招聘申请/预算<br /> <input
					type="checkbox" style="width: 30px;" />招聘申请/预算外<br /> (<input type="text" style="width: 30px;" />)%
				超招聘申请/预算比例<br /></td>
		</tr>

		<tr class="trcenter">
			<td>英文：</td>
			<td><input type="text" /></td>
		</tr>

		<tr class="trcenter">
			<td colspan="2">对外名片职位（与内部职位一致不需要填列）</td>
			<td>内部职位编号</td>
		</tr>

		<tr class="trcenter">
			<td>中文：</td>
			<td><input type="text" /></td>
			<td rowspan="2"><input type="text" /></td>
		</tr>

		<tr class="trcenter">
			<td>英文：</td>
			<td><input type="text" /></td>
		</tr>

		<tr class="trsolid trleft">
			<td>Items项目</td>
			<td colspan="2">Change From 由此更调</td>
			<td colspan="2">Change To 更调至</td>
		</tr>

		<tr class="trsolid trleft">
			<td></td>
			<td>税前</td>
			<td>税后</td>
			<td>税前</td>
			<td>税后</td>
		</tr>

		<tr class="trsolid trleft">
			<td>薪酬总额：（1+2+3）<input type="text" /></td>
			<td><input type="text" /></td>
			<td><input type="text" /></td>
			<td><input type="text" /></td>
			<td><input type="text" /></td>
		</tr>

		<tr class="trsolid trleft">
			<td>其中:1.基本工资：<input type="text" /></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>

		<tr class="trsolid trleft">
			<td>2. 福利：<input type="text" /></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>

		<tr class="trsolid trleft">
			<td></td>
			<td colspan="2"></td>
			<td colspan="2"></td>
		</tr>

		<tr>
			<td>3. 其它福利：<input type="text" />
			</td>
			<td colspan="2"></td>
			<td colspan="2"></td>
		</tr>

		<tr></tr>

		<tr>
			<td>部门/职级变动：</td>
			<td colspan="2"></td>
			<td colspan="2"></td>
		</tr>

		<tr>
			<td>Department部门<input type="text" />
			</td>
			<td colspan="2"></td>
			<td colspan="2"></td>
		</tr>

		<tr>
			<td>Job Classification职级<input type="text" />
			</td>
			<td colspan="2"></td>
			<td colspan="2"></td>
		</tr>

		<tr></tr>

		<tr>
			<td>其他</td>
			<td colspan="2"></td>
			<td colspan="2"></td>
		</tr>

		<tr class="trleft">
			<td colspan="2"><input type="checkbox" style="width: 30px;" />New Hire 新聘 <input
					type="checkbox" style="width: 30px;" />Resignation 辞职<br /> <input type="checkbox"
					style="width: 30px;" />Promotion 升职 <input type="checkbox" style="width: 30px;" />Warning 警告<br />
				<input type="checkbox" style="width: 30px;" />Transfer 转调 <input type="checkbox"
					style="width: 30px;" />Suspension 停职<br /> <input type="checkbox" style="width: 30px;" />Termination
				辞退 <input type="checkbox" style="width: 30px;" />Complete Probation 转正<br /> <input
					type="checkbox" style="width: 30px;" />With Notice（只适用“辞退”） <input type="checkbox"
					style="width: 30px;" />Salary Adjustment 调薪<br /> 有预先通知 From <br /> <input type="checkbox"
					style="width: 30px;" />Without Notice（只适用“辞退”） To <br /> 无预先通知 <br /></td>
			<td colspan="3">Reasons 理由： <textarea rows="8" style="width: 100%"></textarea>
			</td>
		</tr>

		<tr>
			<td colspan="2">“转调”，员工必须签名：<input type="text" />
			</td>
			<td colspan="3">邮箱申请形式：<input type="text" /></td>
		</tr>

		<tr>
			<td colspan="2">Employee Acknowledged</td>
			<td colspan="3">所属邮箱群组：<input type="text" /></td>
		</tr>

		<tr>
			<td colspan="2"><input type="text" /></td>
			<td colspan="3">开通系统：<input type="text" /></td>
		</tr>

		<tr>
			<td colspan="2"></td>
			<td colspan="3">邮箱处理:<input type="checkbox" style="width: 30px;" />注销 <input type="checkbox"
					style="width: 30px;" />保留保留期限:转移至: <input type="text" /></td>
		</tr>

		<tr>
			<td colspan="2">雇员签署<input type="text" /></td>
			<td colspan="3"></td>
		</tr>

		<tr>
			<td colspan="2">Date 日期<input type="text" /></td>
			<td colspan="2">Department head 部门主管</td>
			<td><input type="text" /></td>
		</tr>

		<tr>
			<td colspan="2"><input type="text" /></td>
			<td colspan="2">Dated 日期:<input type="text" /></td>
			<td><input type="text" /></td>
		</tr>

		<tr>
			<td colspan="2">Remarks 备注</td>
			<td colspan="2">Checked by direct Superior直屬上司审核</td>
			<td><input type="text" /></td>
		</tr>

		<tr>
			<td colspan="2"><input type="text" /></td>
			<td colspan="2">Dated 日期:<input type="text" /></td>
			<td><input type="text" /></td>
		</tr>

		<tr>
			<td colspan="2"></td>
			<td colspan="2">Checked by Local GM 当地总经理审核</td>
			<td><input type="text" /></td>
		</tr>

		<tr>
			<td colspan="2"></td>
			<td colspan="2">Dated 日期:<input type="text" /></td>
			<td><input type="text" /></td>
		</tr>

		<tr>
			<td colspan="2">Local Personnel Dept. 当地人力资源部</td>
			<td><input type="text" /></td>
			<td>Approved by Local MD /HRA/CEO</td>
			<td><input type="text" /></td>
		</tr>

		<tr>
			<td colspan="2">Dated 日期:</td>
			<td><input type="text" /></td>
			<td>Dated 日期:</td>
			<td><input type="text" /></td>
		</tr>

		<tr>
			<td colspan="2">Group Personnel Dept. 总部人力资源部</td>
			<td><input type="text" /></td>
			<td>批 准 :地区总裁 /集团HR总監/CEO审核</td>
			<td><input type="text" /></td>
		</tr>

		<tr>
			<td colspan="2">Dated 日期:</td>
			<td><input type="text" /></td>
			<td>Dated 日期:</td>
			<td><input type="text" /></td>
		</tr>
	</table>
</div>