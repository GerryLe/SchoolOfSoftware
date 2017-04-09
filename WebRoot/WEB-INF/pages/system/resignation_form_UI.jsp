<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<style>
<!--
ul li#ib{
	float: left;
	list-style: none;
	border: 1px solid #DDDDDD;
	width: 9.5em;
	background-color: #FFFFFF;
}

.modal-dialog {
	width: 45%;
	height: 60%;
	text-align: center;
}

.employ {
	border: 5px;
	width: 94%;
	height: 400px;
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
	height: 100%;
}
-->
</style>
<div class="row">
	<input type="hidden" name="flag" >
	<table class="employ">
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
			<td><input type="text" name="positionName" id="positionName" readonly /></td>
		</tr>
		<tr class="trsolid trcenter form-group">
			<td>部门</td>
			<td>team</td>
			<td>申请时间</td>
			<td>入职时间</td>
		</tr>
		<tr class="trcenter form-group">
			<td><input type="text" name="parentOrgName" id="parentOrgName" readonly /></td>
			<td><input type="text" name="childOrgName" id="childOrgName" readonly /></td>
			<td><input type="text" id="applyDate" name="applyDate" readonly/></td>
			<td><input type="text" name="employmentDate" id="employmentDate" readonly /></td>
		</tr>
		<tr class="trsolid trcenter form-group">
			<td >地区</td>
			<td style="color: red;">直属上级</td>
			<td style="color: red;">预计离职日期</td>
			<td style="color: red;">工龄</td>
		</tr>
		<tr class="trcenter form-group">
			<td><input type="text" id="area" name="area" readonly/></td>
			<td><input type="text" id="immediateBoss" name="immediateBoss"/>
				<ul id="selectusers_tree_id"style="position: absolute; left: 182px; top: 230px;"></ul>
			</td>
			<td><input class="date_class"  type="date"  id="predictResignationDate" name="predictResignationDate"></td>
			<td><input type="number" name="workingAge" id="workingAge" /></td>
		</tr>
		<tr class="trsolid trleft disable">
			<td colspan="4" style="color: red;">离职原因:</td>
		</tr>
		<tr class="disable form-group">
			<td colspan="4"><textarea name="resignationReason" rows="6"
					style="width: 100%; "></textarea></td>
		</tr>
	</table>
</div>
<script>
	var get = $.webapp.root + "/admin/system/resignation/getresignation.do";
	var flag = true;
	var form_url = $.webapp.root + "/admin/system/resignation/add.do";
	$(function() {
		$("h4.modal-title").html("离职申请");
		$("#save_resignation").show();
		$.post(get, function(result) {
			result.employmentDate = result.employmentDate.split(" ")[0];
			result.applyDate = result.applyDate.split(" ")[0];
			if (result.predictResignationDate != null)
				result.predictResignationDate = result.predictResignationDate
						.split(" ")[0];
			$('#form_resignation').form('load', result);
		}, "json");
		$('.form_date').datetimepicker({
	        language:  'zh-CN',
	        weekStart: 1,
	        todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			minView: 2,
			forceParse: 0
	    });
	})
	$.BOOT.form("form_resignation",{}, function(params) {
		if (flag) {
			$.post(form_url, params, function(result) {
				$.BOOT.toast1(result);
				$resignation_table.bootstrapTable('refresh');
				modalresignation();
			}, 'json');
			flag = false;
		}
	});
	//直属上级搜索
	$("#immediateBoss")
			.on(
					"keyup",
					function() {
						var content = $(this).val();
						if (content == "") {
							$("#selectusers_tree_id").empty();
							return;
						}
						var url = $.webapp.root
								+ "/admin/system/resignation/getImmediateBoss.do";
						$("#selectusers_tree_id").empty();
						var bf = function(data) {
							var html = "";
							html += "<li id='ib' onclick='getImmediateBoss(this)' onMouseOver='over(this)' onMouseOut='out(this)'>";
							html += "<label for='" + data.immediateBoss + "' class='group_users' >";
							html += data.immediateBoss;
							html += "<input class='ib' name='ib' type='radio' id='"
					+ data.immediateBoss + "'";
			html += " value='" + data.immediateBoss + "'>";
							html += "</label></li><br/>";
							$("#selectusers_tree_id").append(html);
						};
						$.post(url, {
							name : content
						}, function(rs) {
							for ( var i in rs) {
								bf(rs[i]);
							}
						}, 'json');
					})
	function getImmediateBoss(c) {
		var $c = $(c).find("input");
		$("input#immediateBoss").val($c.val());
		$("#selectusers_tree_id").empty();
	}
	function over(c){
		$(c).css("background-color","#DDDDDD");
	}
	function out(c){
		$(c).css("background-color","#FFFFFF");
	}
</script>