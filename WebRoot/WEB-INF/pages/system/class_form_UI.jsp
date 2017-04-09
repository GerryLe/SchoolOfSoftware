<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script>
	var form_url = $.webapp.root + "/admin/system/class/add.do";
	var flag = true;//防止重复提交
	$(function() {
		var tree = $.webapp.root + "/admin/system/class/tree.do";
		$.post(tree, {}, function(result) {
			$.BOOT.select("pid", result, "根目录");
			if ($('input[name=id]').val().length > 0) {
				var get = $.webapp.root + "/admin/system/class/get.do";
				$.post(get, {
					id : $('input[name=id]').val()
				}, function(result) {
					form_url = $.webapp.root + "/admin/system/class/update.do";
					$('#form_addclass').form('load', result);
				}, 'json');
			} else {
				$('#form_addclass').form('load', {
					pid : '${pid}'
				});
			}
		}, 'json');
	});
	$.BOOT.form("form_addclass", {
		 class_name : {
			validators : {
				notEmpty : {
					message : '班级名称不能为空'
				}
			}
		} , 
		class_no : {
			validators : {
				notEmpty : {
					message : '班级编号不能为空'
				}
			}
		}
	}, function(params) {
		if(flag){
			$.post(form_url, params, function(result) {
				loadclassTree();
				modaladdclass();
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
		<label for="class_no">编号</label> <input type="text" class="form-control" name="class_no" id="class_no" placeholder="输入班级编号">
	</div>
	<div class="form-group">
		<label for="class_name">名称</label> <input type="text" class="form-control" name="class_name" id="class_name" placeholder="输入班级名称">
	</div>
	<div class="form-group">
		<label for="tea_name">班主任</label> <input type="text" class="form-control" name="tea_name" id="tea_name" placeholder="输入班主任名字">
	</div>
</div>
