<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script>
	var form_url = $.webapp.root + "/admin/system/role/add.do";
	var parent_box, role_type;
	
	var flag = true;//防止重复提交
	$(function() {
		var href = $.webapp.root + "/admin/system/role/get.do";
		$('input[type="radio"].iradio').iCheck({
			radioClass : 'iradio_flat-green'
		});
		//编辑，加载表单数据
		 if ($('input[name=id]').val().length > 0) {
			$.post(href, {
				id : $('input[name=id]').val()
			}, function(result) {
				form_url = $.webapp.root + "/admin/system/role/update.do";
				$('#form_addroles').form('load', {
					'name' : result.name
				});
				$("#defaultRole").iCheck('check');
			}, 'json');
		} else {
		} 
	});
	$.BOOT.form("form_addroles", {
		name : {
			validators : {
				notEmpty : {
					message : '角色名称不能为空'
				}
			}
		}
	}, function(params) {
		if(flag){
			$.post(form_url, params, function(result) {
				$role_table.bootstrapTable('refresh');
				modaladdroles();
			}, 'json');
			flag = false;
		}
	});
</script>

<div class="">
	<input type="hidden" name="id" value="${id}">
	<div class="form-group">
		<label for="name">名称</label> <input type="text" class="form-control" name="name" id="name" placeholder="输入角色名称">
	</div>
	<!-- <div class="form-group">
		<label for="defaultRole">默认角色</label> <br /> <input id="defaultRole" class="radio form-control" type="radio" name="defaultRole" value="1">
	</div> -->
</div>
