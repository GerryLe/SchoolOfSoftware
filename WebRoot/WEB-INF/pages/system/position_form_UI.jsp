<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<style>
<!--
 .select input {
    width: 16px;
    height: 16px;
    cursor: pointer;
    margin-top: 20px;
    margin-left: 16px;
    position: relative;
} 
 
.select{
    font-size: 16px;
}
-->
</style>
<script>
	var form_url = $.webapp.root + "/admin/system/position/add.do";
	var flag = true;//防止重复提交
	$(function() {
		var tree = $.webapp.root + "/admin/system/position/tree.do";
		$.post(tree, {}, function(result) {
			$.BOOT.select("pid", result, "根目录");
			if ($('input[name=id]').val().length > 0) {
				var get = $.webapp.root + "/admin/system/position/get.do";
				$.post(get, {
					id : $('input[name=id]').val()
				}, function(result) {
					form_url = $.webapp.root + "/admin/system/position/update.do";
					$('#form_addposition').form('load', result);
				}, 'json');
			} else {
				$('#form_addposition').form('load', {
					pid : '${pid}'
				});
			}
		}, 'json');
	});
	$.BOOT.form("form_addposition", {
		name : {
			validators : {
				notEmpty : {
					message : '部门名称不能为空'
				}
			}
		},
		workType : {
			validators : {
				notEmpty : {
					message : '请选择上班方式'
				}
			}
		}
	}, function(params) {
		if(flag){
			$.post(form_url, params, function(result) {
				loadpositionTree();
				modaladdposition();
			}, 'json');
			flag = false;
		}
	});
</script>
<div>
	<input type="hidden" name="id" value="${id}">
	<div class="form-group">
		<label for="pid">父目录</label> <select class="form-control" id="pid" name="pid">
		</select>
	</div>
	<div class="form-group">
		<label for="positionName">名称</label> <input type="text" class="form-control" name="name" id="positionName" placeholder="输入部门名称">
    </div>
		<!-- <select class="form-control" name="workType" id="workType" title="选择上班方式">
		   <option value="正常上班">正常上班</option>
		   <option value="弹性上班">弹性上班</option>
		</select> -->
	  <div class="form-group select" id="workType">
		<label for="workType">上班方式：</label> 
		<input  type="radio" name="workType"  value="1">正常上班
		<input  type="radio" name="workType"  value="0">弹性上班 
	  </div>
	  <div class="form-group select" id="approveAuth">
		<label for="workType">审批权限：</label> 
		<input  type="radio" name="approveAuth" value="0">无
		<input  type="radio" name="approveAuth"  value="1">有
	  </div>
</div>
