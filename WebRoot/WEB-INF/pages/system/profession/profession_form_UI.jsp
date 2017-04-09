<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script>
	var form_url = $.webapp.root + "/admin/system/org/add.do";
	var flag = true;//防止重复提交
	$(function() {
		var tree = $.webapp.root + "/admin/system/org/tree.do";
		$.post(tree, {}, function(result) {
			$.BOOT.select("pid", result, "根目录");
			if ($('input[name=id]').val().length > 0) {
				var get = $.webapp.root + "/admin/system/org/get.do";
				$.post(get, {
					id : $('input[name=id]').val()
				}, function(result) {
					form_url = $.webapp.root + "/admin/system/org/update.do";
					$('#form_addorgs').form('load', result);
				}, 'json');
			} else {
				$('#form_addorgs').form('load', {
					pid : '${pid}'
				});
			}
		}, 'json');
	});
	$.BOOT.form("form_addorgs", {
		name : {
			validators : {
				notEmpty : {
					message : '<spring:message code="Departmentnamecannotbeempty" />'
				}
			}
		}
	}, function(params) {
		if(flag){
			$.post(form_url, params, function(result) {
				loadorgTree();
				modaladdorgs();
			}, 'json');
			flag = false;
		}
	});
</script>
<div>
	<input type="hidden" name="id" value="${id}">
	<div class="form-group">
		<label for="pid"><spring:message code="parentdirectory" /></label> <select class="form-control" id="pid" name="pid">
		</select>
	</div>
	<div class="form-group">
		<label for="orgName"><spring:message code="Departmentname" /></label> <input type="text" class="form-control" name="name" id="orgName" placeholder="<spring:message code="Enterthedepartmentname" />">
	</div>
</div>
