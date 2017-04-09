<%@page import="com.rosense.module.common.web.servlet.WebContextUtil"%>
<%@page import="com.rosense.basic.util.date.DateUtils"%>
<%@page import="com.rosense.module.app.web.form.BbsForm"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<link rel="stylesheet" href="${webRoot}/template/resource/app/bbsview.css" />
<link rel="stylesheet" href="${webRoot}/template/resource/plugins/editor/summernote.css">
<script src="${webRoot}/template/resource/plugins/tools/table.js"></script>
<script src="${webRoot}/template/resource/plugins/editor/summernote.js"></script>
<script src="${webRoot}/template/resource/plugins/editor/summernote-zh-CN.js"></script>
<%
	BbsForm bbs = (BbsForm) request.getAttribute("bbs");
%>
<style>
<!--
.operate img {
	width: 40px;
	height: 40px;
	border-radius: 4px;
}

.agree,.disagree {
	cursor: pointer;
}

.delete {
	margin-left: 5px;
	display: none;
}

.admin .delete {
	display: inline !important;
}

.fa-trash {
	margin-left: 5px;
}
-->
</style>
<div class="aw-content-wrap clearfix container">
	<div class="aw-main-content">
		<div class="aw-mod aw-question-detail aw-item">
			<div class="mod-head">
				<h1><%=bbs.getTitle()%></h1>
				<div class="operate clearfix">
					<%=bbs.getUserName()%>
					<img alt="" src="/common/photo.do?id=<%=bbs.getUserId()%>">
				</div>
			</div>
			<div class="mod-body">
				<div class="content markitup-box">
					<%=bbs.getContent()%>
				</div>
			</div>
			<div class="mod-footer">
				<div class="meta aw-feed-list" style="padding: 0px;">
					<span class="text-color-999"><%=DateUtils.formatDate(bbs.getCreateDate(), "yyyy-MM-dd")%></span> <span class="operate pull-right"> <a class="agree " onclick="zan(this,'<%=bbs.getId()%>');"> <i class="fa fa-thumbs-o-up"></i> <b class="count"><%=bbs.getZans()%></b></a> </a>
					</span>
				</div>
			</div>
		</div>
		<div class="aw-mod aw-question-comment">
			<div class="mod-head">
				<ul class="nav nav-tabs aw-nav-tabs active">
					<h2 class="hidden-xs"><%=bbs.getRemarks()%>
						个<a href="#answer_form">回复</a>
					</h2>
				</ul>
			</div>
			<div class="mod-body aw-feed-list" id="bbsitemid"></div>
			<div id="bbsitempage"></div>
		</div>
		<div class="aw-mod aw-replay-box">
			<a name="answer_form"></a>
			<form id="form-bbsitem">
				<input type="hidden" name="bbsId" id="bbsId" value="<%=bbs.getId()%>"> <input type="hidden" name="reffloor" id="reffloor" value="0">
				<div class="form-group" style="display: none;">
					<input type="text" class="form-control" value="游客" name="userName" id="userName">
				</div>
				<div class="form-group">
					<div id="summernote"></div>
					<textarea style="display: none;" id="content" name="content"></textarea>
				</div>
				<div class="form-group">
					<button class="btn btn-success pull-right" id="save_bbsitem" type="submit">回复</button>
					<div class="pull-right" id="reffloordiv" style="margin-right: 10px;"></div>
					<div class="clearfix"></div>
				</div>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript">
	var answer = '${answer}';
	$(document).ready(function() {
		$('#summernote').summernote({
			height : 300, // set editor height
			minHeight : null, // set minimum height of editor
			maxHeight : null, // set maximum height of editor
			lang : 'zh-CN',
			callbacks : {
				onImageUpload : function(files, formid) {
					sendFile(files[0], formid, "summernote");
				}
			}
		});
	});
	var table;
	$(function() {
		table = cloneTable({
			id : "bbsitemid",
			pid : "bbsitempage",
			limit : 10,
			params : {
				bbsId : $("#bbsId").val()
			},
			url : "/app/bbs/datagriditem.do",
			buildRow : function(row) {
				var html = "";
				html += '<div class="aw-item" val="'+row.id+'">';
				html += '	<div class="mod-head">';
				html += '		<a class="anchor" name="answer_'+row.floor+'"></a>';
				html += '		<a class="aw-user-img aw-border-radius-5 pull-right" href="javascript:;">';
				html += '			<img src="/common/photo.do?id=' + row.userId
						+ '" alt=""></a>';
				html += '		<div class="title">';
				html += '			<p>';
				html += '				<a class="aw-user-name" href="javascript:;">'
						+ row.userName + '</a>';
				html += '			</p>';
				if (row.reffloor > 0) {
					html += '			<p class="text-color-999 aw-agree-by">';
					html += '				<a href="javascript:;" class="aw-user-name aw-floor" val="'+row.reffloor+'">引用'
							+ row.reffloor + '#</a>';
					html += '			</p>';
				}
				html += '		</div>';
				html += '	</div>';
				html += '	<div class="mod-body clearfix">';
				html += '		<div class="markitup-box">' + row.content;
				html += '		</div>';
				html += '	</div>';
				html += '	<div class="mod-footer">';
				html += '		<div class="meta clearfix">';
				html += '			<span class="pull-right"><a href="javascript:;" class="delete">删除</a></span>';
				html += '			<span class="text-color-999 pull-right">'
						+ row.createDate.toDate().Format("yyyy-MM-dd")
						+ '</span>';
				html += '			<span class="operate">';
				html += '				<a class="agree ">';
				html += '				<i class="fa fa-thumbs-o-up"></i> <b class="count">'
						+ row.zans + '</b></a>';
				html += '				<a class="disagree ">';
				html += '				<i class="fa fa-commenting-o"></i> <b class="count">'
						+ row.floor + '#</b></a>';
				html += '			</span>';
				html += '		</div>';
				html += '	</div>';
				html += '</div>';
				$("#bbsitemid").append(html);
			},
			noData : function() {
				$("#bbsitemid").append(
						"<div style='text-align:center;'>没有回复</div>");
			},
			endData : function() {
				if (answer == 'true') {
					answer = "false";
					location.href = "#answer_form";
				}
				if (refId != -1) {
					location.href = "#answer_" + refId;
					refId = -1;
				}
			}
		});

	});
	$.BOOT.form("form-bbsitem", {}, function(params) {
		if ("<p><br></p>" == $("#content").val()) {
			$.BOOT.toast(false, "内容不能为空");
			$.BOOT.btn("save_bbsitem", false);
			return;
		}
		$.post("/app/bbs/no_addItem.do", params, function(result) {
			$.BOOT.toast(result.status, result.msg);
			$.BOOT.btn("save_bbsitem", false);
			$('#summernote').summernote('code', "");
			table.loadData();
		}, 'json');
	}, function() {
		$("#content").val($('#summernote').summernote('code'));
	});
	$(document).on("click", ".delete", function() {
		var obj = $(this).parents(".aw-item");
		var id = obj.attr("val");
		var json = {
			title : "",
			text : "确定删除吗",
			showCancelButton : true,
			type : 'warning',
			call : function() {
				$.post("/app/bbs/no_deleteItem.do", {
					id : id
				}, function(result) {
					$.BOOT.toast1(result);
					if (result.status) {
						obj.remove();
					}
				}, 'json');
			}
		};
		$.BOOT.alert(json, true);
	});
	$(document).on("click", ".agree", function() {
		var b = $(this).children("b");
		var id = $(this).parents(".aw-item").attr("val");
		$.post("/app/bbs/no_zanItem.do", {
			id : id
		}, function(result) {
			if (result.status) {
				b.text(parseInt(b.text()) + 1);
			} else {
				$.BOOT.toast(false, '已经赞过');
			}
		}, 'json');
	});
	$(document).on(
			"click",
			".disagree",
			function() {
				var b = $(this).children("b");
				$("#reffloor").val(b.text().replace("#", ""));
				$("#reffloordiv").html(
						"<button class='btn btn-default deleteref'>" + b.text()
								+ "<i class='fa fa-trash'><i></button>");
				location.href = "#answer_form";
			});
	$(document).on("click", ".deleteref", function() {
		$("#reffloor").val("");
		$(this).remove();
	});
	var refId = -1;
	$(document).on("click", ".aw-floor", function() {
		refId = parseInt($(this).attr("val"));
		var t = refId / table.limit;
		table.offset = Math.ceil(t) - 1;
		table.loadData();
	});
	function zan(c, id) {
		var b = $(c).children("b");
		$.post("/app/bbs/no_zan.do", {
			id : id
		}, function(result) {
			if (result.status) {
				b.text(parseInt(b.text()) + 1);
			} else {
				$.BOOT.toast(false, '已经赞过');
			}
		}, 'json');
	}
</script>