<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<jsp:include page="/template/modal.jsp"><jsp:param
		value="addroles" name="id" /><jsp:param value="编辑角色" name="title" /></jsp:include>
<section class="content">
	<div id="user-form-id"></div>
	<div id="filter-bar">
		<div class="btn-toolbar">
			<div class="btn-group btn-group-filter-refresh">
				<button id="buttonByKey" type="button"
					class="btn btn-default  btn-refresh" data-toggle="dropdown"
					style="display: block;" style="float: right">搜一下</button>
			</div>
		</div>
		<div class="btn-toolbar">
			<div class="form-group">
				<input type="text" name="searchKeyName" id="searchKeyName"
					class="form-control" placeholder="Search">
			</div>
		</div>
		<div class="btn-toolbar">
			<select class="form-control" id="selectType" name="selectType"
				style="width: 100%">
				<option value="name">姓名</option>
				<option value="account">学号</option>
			</select>
		</div>
		<button type="button" class="btn btn-primary " id="batch_delete">批量删除</button>
	</div>
	<table id="user_table" class="table-condensed table table-hover"
		data-row-style="rowStyle" data-side-pagination="server"></table>
</section>
<script type="text/javascript">
	var $user_table;
	$(function() {
		$user_table = $.BOOT.table("user_table", $.webapp.root
				+ "/admin/system/user/datagrid.do", {
			columns : [ {
				title : "全选",
				field : 'select',
				checkbox : true,
				width : 20,//宽度
				align : "center",//水平
				valign : "middle",//垂直
			}, {
				field : 'account',
				title : '账号'
			}, {
				field : 'name',
				title : '姓名'
			}, {
				field : 'role_names',
				title : '角色'
			}, {
				field : 'status',
				title : '状态',
				sortable : true,
				formatter : function(value, row) {
					if (value == "0") {
						return "正常";
					} else {
						return "锁定";
					}
				}
			}, {
				field : 'id',
				title : '操作',
				formatter : function(value, row, index) {
					return $.BOOT.groupbtn(value, [ {
						cla : 'user_lock',
						text : "锁定解锁"
					}, {
						cla : 'user_delete',
						text : "删除账号"
					}, {
						cla : 'user_reset',
						text : "重置密码"
					}, {
						cla : 'user_edit',
						text : "编辑角色"
					} ]);
				}
			} ],
			paginationInfo : true
		});
	});
	function rowStyle(row, index) {
		if (row.status == "1") {
			return {
				classes : 'danger'
			};
		}
		return {};
	}
	/* 筛选用户信息 */
	$(document).on("click", "#buttonByKey", function() {
		$user_table.bootstrapTable('refresh', 
				{url: "/admin/system/user/datagrid.do?searchKeyName="+$("#searchKeyName").val()+"&selectType="+$("#selectType").val()+""}); 

	
	  });
	
	$.BOOT.click(".user_edit", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/user/userrole_main_UI.do?id='
				+ id;
		$.BOOT.page("content_addroles", href, function() {
			$('#addrolesModal').modal('toggle');
		});
	});

	$.BOOT.click(".user_reset", function(c) {
		var href = $.webapp.root + '/admin/system/user/resetPwd.do';
		var id = $(c).attr("val");
		$.post(href, {
			id : id
		}, function(result) {
			$user_table.bootstrapTable('refresh');
			$.BOOT.toast1(result);
		}, 'json');
	});
	$.BOOT.click(".user_lock", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/user/lockUser.do';
		$.post(href, {
			id : id
		}, function(result) {
			$user_table.bootstrapTable('refresh');
			$.BOOT.toast1(result);
		}, 'json');
	});
	$.BOOT.click(".user_delete", function(c) {
		var id = $(c).attr("val");
		var json = {
			title : "",
			text : "确定删除账号吗?",
			showCancelButton : true,
			type : 'warning',
			call : function() {
				var href = $.webapp.root + '/admin/system/user/delete.do';
				$.post(href, {
					ids : id
				}, function(result) {
					$user_table.bootstrapTable('refresh');
					$.BOOT.toast1(result);
				}, 'json');
			}
		};
		$.BOOT.alert(json, true);
	});

	$("#batch_delete").click(function() {
		var $input = $("tr.selected");
		var ids = "";

		for (var i = 0; i < $input.length; i++) {
			var html = $input.find("li.user_lock").eq(i).prop('outerHTML');
			var b = html.indexOf("\"");
			var e = html.indexOf("\" ");
			var id = html.substring(b + 1, e);
			ids += id + ",";
		}
		var json = {
			title : "",
			text : "确定批量删除用户吗?",
			showCancelButton : true,
			type : 'warning',
			call : function() {
				var href = $.webapp.root + '/admin/system/user/delete.do';
				$.post(href, {
					ids : ids
				}, function(result) {
					$user_table.bootstrapTable('refresh');
					$.BOOT.toast1(result);
				}, 'json');
			}
		};
		$.BOOT.alert(json, true);
	});
</script>