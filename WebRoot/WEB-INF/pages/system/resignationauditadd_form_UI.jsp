<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="container">
	<input type="hidden" value="${id}" name="id">
	<div class="row">
		<div class="form-group col-xs-3" style="display:none;" id="extre">
			<label for="emailDeal">邮箱处理</label> <input type="text"
				class="form-control" id="emailDeal" name="emailDeal" >
		</div>
		<div class="form-group col-xs-3">
			<label for="directRemark">备注</label> <input type="text"
				class="form-control" id="directRemark" name="directRemark">
		</div>
	</div>
	<div class="row">
		<div class="form-group col-xs-3">
			<label for="sta">审核状态</label><br /> <input type="radio" id="sta"
				name="sta" value="1">通过 <input type="radio" id="sta"
				name="sta" value="2">不通过
			<!-- <input type="radio" id="sta1" name="sta1" value="-1">退回 -->
		</div>
	</div>
	<div class="row">
		<div class="form-group col-xs-6">
			<label for="directAuditOpinion">审批意见</label>
			<textarea rows="4" class="form-control" id="directAuditOpinion"
				name="directAuditOpinion"></textarea>
		</div>
	</div>
</div>
<script>
function a(c) {
	console.info(c.val());
}
var get = $.webapp.root + "/admin/system/resignation/getAudit.do";
var flag = true;
var form_url = $.webapp.root + "/admin/system/resignation/addAudit.do";
$(function() {
	$("h4.modal-title").html("离职审核");
	$("#save_resignation").show();
	$.post(get,{
		id : $('input[name=id]').val()
	}, function(result) {
		if(result.showExtre)
			$("div#extre").css("display","block");
		$('#form_resignation').form('load', result);
	}, "json");
}) 
$.BOOT.form("form_resignation", {
	emailDeal : {
		validators : {
			notEmpty : {
				message : '处理邮箱为空 '
			}
		}
	},
	directRemark : {
		validators : {
			notEmpty : {
				message : '备注不能为空 '
			}
		}
	},
	sta : {
		validators : {
			notEmpty : {
				message : '审核不能为空 '
			}
		}
	},
	directAuditOpinion : {
		validators : {
			notEmpty : {
				message : '审批意见不能为空 '
			}
		}
	},
}, function(params) {
	if (flag) {
		$.post(form_url, params, function(result) {
			$.BOOT.toast1(result);
			$resignation_table.bootstrapTable('refresh');
			modalresignation();
		}, 'json');
		flag = false;
	}
});
</script>