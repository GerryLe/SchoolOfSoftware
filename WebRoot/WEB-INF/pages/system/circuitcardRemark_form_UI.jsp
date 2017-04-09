<%@page import="com.rosense.basic.util.LangUtils"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script>
	var form_url = $.webapp.root + "/admin/system/circuitcard/false.do";
	var flag = true;//防止重复提交
	var idString=$('input[name=id]').val();
    var idlast=idString.substring(idString.length-4);
	$(function() {
		   $("#save_circuitcard").show();
			if (idlast=="true") {
				$('input[name=id]').val(idString.substring(0,idString.length-4));
				form_url = $.webapp.root + "/admin/system/circuitcard/true.do";
			}
	  });
	$.BOOT.form("form_circuitcard", {}, function(params) {
		if(flag){
			$.post(form_url, params, function(result) {
				$.BOOT.toast1(result);
				$person_table.bootstrapTable('refresh');
				modalcircuitcard();
			}, 'json');
			flag = false;
		}
	});
</script>
<div>
	<input type="hidden" name="id" value="${id}">
	<div class="form-group">
		<label for="applyRemark">备注</label>
		<textarea  name="applyRemark" id="applyRemark" rows="5" style="width: 100%"></textarea>
	</div>
</div>
