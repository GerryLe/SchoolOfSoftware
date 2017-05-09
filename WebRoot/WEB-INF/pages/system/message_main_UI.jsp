<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<section class="content">
	<!-- <div style="margin: 3px auto;"> -->
	<div class="btn-toolbar" style="float: right">
			<button type="button" class="btn btn-primary btn-block" id="message_all_read">全部设为已读</button>
	</div>
	<div class="btn-toolbar" style="float: left">
			<button type="button" class="btn btn-primary " id="batch_delete">批量删除</button>
	</div>
	
		
<!-- 	</div> -->
	<table id="message_table" class="table-condensed table table-hover" data-row-style="rowMessageStyle" data-side-pagination="server"></table>
</section>
<script type="text/javascript">
	var $message_table;
	$(function() {
		var url = $.webapp.root + "/admin/system/message/datagrid.do";
		$message_table = $.BOOT.table("message_table", url, {
			columns : [ {
				title : "全选",
				field : 'select',
				checkbox : true,
				width : 20,
				align : "center",
				valign : "middle",
			},{
				field : 'id',
				title : 'id',
				visible : false,
			}, {
				field : 'content',
				title : '消息列表',
			}, {
				field : 'created',
				title : '时间',
				width : 150,
			},{
				field : 'id',
				class: 'messageId',
				title : '操作',
				formatter : function(value, row, index) {
					 return "<button type='button' class='btn btn-default fa' onclick='deleteMessage(this)' id='"+value+"'>删除</button>";
				}
			}  ],
			shows : false,
			nodata : "没有要处理消息"
		}).on('click-row.bs.table', function(e, row, $element) {
			$($element).removeClass("danger");
			set_status(row.id);
		});
	});
	function buildMessage(row) {
		var newTime = row.created.toDate();
		var html = "<div class='time'>" + row.title;
		html += "<span class='time pull-right'><i class='fa fa-clock-o'></i>"
				+ newTime.Format("MM-dd hh:mm") + "</span>";
		html += "</div><div>" + (row.content == null ? "" : row.content)
				+ "</div>";
		return html;
	}
	function rowMessageStyle(row, index) {
		if (!row.readed) {
			return {
				classes : 'danger'
			};
		}
		return {};
	}
	function set_status(id) {
		var form_url = $.webapp.root + "/admin/system/message/no_update.do";
		$.post(form_url, {
			id : id
		}, function(result) {
			if (id == null)
				$message_table.bootstrapTable('refresh');
		}, 'json').error(function() {
			return;
		});
	};
	$.BOOT.click("#message_all_read", function(c) {
		$("#notice-id-count").html("0");
		set_status();
	});
	
	function deleteMessage(c){
		var id = $(c).attr("id");
		var json = {
			title : "",
			text : "确定删除信息吗?",
			showCancelButton : true,
			type : 'warning',
			call : function() {
				var href = $.webapp.root + '/admin/system/message/delete.do';
				$.post(href, {
					ids : id
				}, function(result) {
					$message_table.bootstrapTable('refresh');
					$.BOOT.toast1(result);
				}, 'json');
			}
		};
		$.BOOT.alert(json, true);
	}
	
	$("#batch_delete").click(function() {
		var $input = $("tr.selected");
		if($input.length==0){
			alert("请选择要删除的记录！");
			return;
		}
		var ids = "";
		for (var i = 0; i < $input.length; i++) {
			var id = $input.find("td:eq(3) button").eq(i).attr("id");
			ids += id + ",";
		}
		 if(ids!=""){
			var json = {
				title : "",
				text : "确定批量删除信息吗?",
				showCancelButton : true,
				type : 'warning',
				call : function() {
					var href = $.webapp.root + '/admin/system/message/delete.do';
						$.post(href, {
							ids : ids
						}, function(result) {
							$message_table.bootstrapTable('refresh');
							$.BOOT.toast1(result);
						}, 'json');
				}
			};
			$.BOOT.alert(json, true);
		} 
	});
</script>
