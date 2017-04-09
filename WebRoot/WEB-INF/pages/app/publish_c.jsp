<%@page import="com.rosense.module.common.web.servlet.WebContextUtil"%>
<%@page import="com.rosense.module.app.web.form.BbsForm"%>
<%@page import="com.rosense.basic.util.StringUtil"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<link href="${webRoot}/template/resource/plugins/editor/summernote.css" rel="stylesheet">
<script src="${webRoot}/template/resource/plugins/editor/summernote.js"></script>
<script src="${webRoot}/template/resource/plugins/editor/summernote-zh-CN.js"></script>
<%
	BbsForm bbs = (BbsForm) request.getAttribute("bbs");
%>
<div style="padding: 10px;">
	<form id="form-bbs">
		<input type="hidden" id="id" name="id" value="${id}">
		<div class="form-group" style="display: none;">
			<label for="userName">昵称</label> <input type="text" class="form-control" value="游客" name="userName" id="userName">
		</div>
		<div class="form-group">
			<label for="title">标题</label> <input type="text" class="form-control" value="<%=StringUtil.toString(bbs.getTitle(), "")%>" name="title" id="title">
		</div>
		<div class="form-group">
			<label for="title">内容</label>
			<div id="summernote"><%=StringUtil.toString(bbs.getContent(), "")%></div>
			<textarea style="display: none;" id="content" name="content"></textarea>
		</div>
		<div class="form-group">
			<button class="btn btn-success pull-right" id="save_bbs" type="submit">发布</button>
			<div class="clearfix"></div>
		</div>
	</form>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		$('#summernote').summernote({
			height : 300, // set editor height
			minHeight : null, // set minimum height of editor
			maxHeight : null, // set maximum height of editor
			focus : true,
			lang : 'zh-CN',
			callbacks : {
				onImageUpload : function(files, formid) {
					sendFile(files[0], formid,"summernote");
				}
			}
		});
	});
	$.BOOT.form("form-bbs", {
		title : {
			validators : {
				notEmpty : {
					message : '标题不能为空'
				}
			}
		}
	}, function(params) {
		$("#content").val($('#summernote').summernote('code'));
		$.post("/app/bbs/no_add.do", params, function(result) {
			$.BOOT.toast(result.status, result.msg);
			$.BOOT.btn("save_bbs", false);
			if (result.status) {
				location.href = "/";
			}
		}, 'json');
	}, function() {
		$("#content").val($('#summernote').summernote('code'));
	});
</script>