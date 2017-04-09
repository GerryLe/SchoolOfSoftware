<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<style>
<!--
.modal-dialog {
	width: 45%;
	height: 60%;
	text-align: center;
}

.resignation {
	border: 5px;
	width: 700px;
	height: 450px;
	text-align: center;
	margin-left: 23px;
}

.resignation tr td {
	border: 1px solid black;
	width: 175px;
	height: 30px;
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
	height: 100%;
}

.extre {
	display: none;
}
-->
</style>
<div class="row" style="display: none;">
	<input type="hidden" name="id" value="${id}" id="id">
	<table class="resignation">
		<tr class="trsolid trcenter  form-group">
			<td>员工编号</td>
			<td>中文名</td>
			<td>英文名</td>
			<td>岗位</td>
		</tr>
		<tr class="trcenter form-group">
			<td><input type="text" name="account" id="account" readonly /></td>
			<td><input type="text" name="chinaname" id="chinaname" readonly /></td>
			<td><input type="text" name="name" id="name" readonly /></td>
			<td><input type="text" name="positionName" id="positionName"
				readonly /></td>
		</tr>
		<tr class="trsolid trcenter form-group">
			<td>team</td>
			<td>部门</td>
			<td>申请时间</td>
			<td>入职时间</td>
		</tr>
		<tr class="trcenter form-group">
			<td><input type="text" name="childOrgName" id="childOrgName"
				readonly /></td>
			<td><input type="text" name="parentOrgName" id="parentOrgName"
				readonly /></td>
			<td><input type="text" id="applyDate" name="applyDate" readonly /></td>
			<td><input type="text" name="employmentDate" id="employmentDate"
				readonly /></td>
		</tr>
		<tr class="trsolid trcenter form-group">
			<td>地区</td>
			<td>直属上级</td>
			<td>预计离职日期</td>
			<td>工龄</td>
		</tr>
		<tr class="trcenter form-group">
			<td><input type="text" id="area" name="area" /></td>
			<td><input type="text" id="immediateBoss" name="immediateBoss"
				readonly /></td>
			<td><input class="date_class" type="date"
				id="predictResignationDate" name="predictResignationDate" readonly></td>
			<td><input type="number" name="workingAge" id="workingAge"
				readonly /></td>
		</tr>
		<tr class="trsolid trleft disable">
			<td colspan="4">离职原因:</td>
		</tr>
		<tr class="disable form-group">
			<td colspan="4"><textarea name="resignationReason" rows="6"
					style="width: 100%;" readonly></textarea></td>
		</tr>
		<tr class="trsolid trleft disable">
			<td colspan="4">直属上级</td>
		</tr>
		<tr class="disable form-group">
			<td colspan="2" class="trsolid">邮箱处理</td>
			<td colspan="2"><input type="text" id="emailDeal" readonly
				name="emailDeal" /></td>
		</tr>
		<tr class="trcenter disable form-group">
			<td class="trsolid">备注</td>
			<td><input readonly="readonly" type="text" name="directRemark"
				id="directRemark" readonly /></td>
			<td class="trsolid">审批结果</td>
			<td><input readonly="readonly" type="text" name="sta" id="sta"
				readonly /></td>
		</tr>
		<tr class="disable form-group">
			<td class="trsolid">上级审批意见:</td>
			<td colspan="3"><textarea name="directAuditOpinion" rows="6"
					style="width: 100%" readonly></textarea></td>
		</tr>
		<tr class="trsolid trleft disable">
			<td colspan="4">Shadow</td>
		</tr>
		<tr class="trcenter disable form-group">
			<td class="trsolid">备注</td>
			<td><input readonly="readonly" type="text" name="remark1"
				id="remark1" readonly /></td>
			<td class="trsolid">审批结果</td>
			<td><input readonly="readonly" type="text" name="sta1" id="sta"
				readonly /></td>
		</tr>
		<tr class="disable form-group">
			<td class="trsolid">审批意见:</td>
			<td colspan="3"><textarea name="auditOpinion1" rows="6"
					style="width: 100%" readonly></textarea></td>
		</tr>
		<tr class="trsolid trleft disable extre">
			<td colspan="4">May</td>
		</tr>
		<tr class="trcenter disable form-group extre">
			<td class="trsolid">备注</td>
			<td><input readonly="readonly" type="text" name="remark2"
				id="remark2" readonly /></td>
			<td class="trsolid">审批结果</td>
			<td><input readonly="readonly" type="text" name="sta2" id="sta"
				readonly /></td>
		</tr>
		<tr class="disable form-group extre">
			<td class="trsolid">审批意见:</td>
			<td colspan="3"><textarea name="auditOpinion2" rows="6"
					style="width: 100%" readonly></textarea></td>
		</tr>
		<tr class="trsolid trleft disable extre">
			<td colspan="4">Ravic</td>
		</tr>
		<tr class="trcenter disable form-group extre">
			<td class="trsolid">备注</td>
			<td><input readonly="readonly" type="text" name="remark3"
				id="remark3" readonly /></td>
			<td class="trsolid">审批结果</td>
			<td><input readonly="readonly" type="text" name="sta3" id="sta"
				readonly /></td>
		</tr>
		<tr class="disable form-group extre">
			<td class="trsolid">审批意见:</td>
			<td colspan="3"><textarea name="auditOpinion3" rows="6"
					style="width: 100%" readonly></textarea></td>
		</tr>
	</table>
</div>
<script>
	$(function() {
		$("h4.modal-title").html("审核情况");
		$("#save_resignation").hide();
		var id = $("input#id").val();
		$.post($.webapp.root + '/admin/system/resignation/getAuditById.do?id='
				+ id, function(result) {
			if (result.flag) {
				$(".extre").show();
			}
			result.predictResignationDate = result.predictResignationDate.split(" ")[0];
			$('#form_resignation').form('load', result);
			$('input#sta').each(function() {
				if ($(this).val() == 0)
					$(this).val("未审核");
				else if ($(this).val() == 1)
					$(this).val("通过");
				else if ($(this).val() == 2) {
					$(this).val("不通过");
					$(this).css("color", "red");
				}
			});
			result.employmentDate = result.employmentDate.split(" ")[0];
			result.applyDate = result.applyDate.split(" ")[0];
			$("div.row").show();
		}, "json");
	})
</script>