<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<section class="content">
	<div style="margin: 3px auto;">
		<button type="button" class="btn btn-primary btn-block" id="message_all_read"><spring:message code="Allsettoread" /></button>
	</div>
	<table id="message_table" class="table-condensed table table-hover" data-row-style="rowMessageStyle" data-side-pagination="server"></table>
</section>
<script type="text/javascript">
	var $message_table;
	$(function() {
		var url = $.webapp.root + "/admin/system/message/datagrid.do";
		$message_table = $.BOOT.table("message_table", url, {
			columns : [ {
				field : 'id',
				title : 'id',
				visible : false,
			}, {
				field : 'content',
				title : '<spring:message code="Themessagelist" />',
			}, {
				field : 'created',
				title : '<spring:message code="time" />',
				width : 150,
			} ],
			shows : false,
			nodata : "<spring:message code="Nottoprocessthemessage" />"
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
</script>
