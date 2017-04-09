$(document)
		.on(
				"keyup",
				"#user_search_name",
				function() {
					var content = $(this).val();
					if (content == "") {
						return;
					}
					var url = $.webapp.root + "/admin/system/user/search.do";
					$("#selectusers_tree_id").empty();
					var bf = function(data) {
						var html = "";
						var lid = "UU_" + data.id;
						html += "<label for='" + lid + "' class='group_users'>";
						html += "<div class='clearfix' style='padding:0 5px;'>";
						html += "<div class='pull-right'>";
						html += data.name;
						html += "</div><div class='pull-left' style='margin-top: 4px;'>";
						html += "<input class='icheckbox uselect' type='checkbox' id='"
								+ lid + "'";
						html += " val1='" + data.id + "'";
						html += ">";
						html += "</div></div></label><br/>";
						$("#selectusers_tree_id").append(html);
					};
					$.post(url, {
						content : content
					}, function(rs) {
						for ( var i in rs) {
							bf(rs[i]);
						}
						$('input[type="checkbox"].icheckbox').iCheck({
							checkboxClass : 'icheckbox_flat-green'
						});
					}, 'json');
				});
var $users = {
	callback : null,
	/**
	 * 组织窗口页面
	 */
	initUsers : function() {
		var html = "";
		html += "<div class='modal fade userModal' id='userModal' tabindex='-1' role='dialog'";
		html += "	aria-labelledby='userModalLabel'>";
		html += "	<div class='modal-dialog' role='document'>";
		html += "		<div class='modal-content'>";
		html += "			<div class='modal-header' >";
		html += '				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>';
		html += ' 				<h4 class="modal-title">选择用户</h4>';
		html += "			</div>";
		html += "			<div class='modal-body'  >";
		html += "				<div><input type='text' id='user_search_name' placeholder='查询用户' class='form-control'>";
		html += "					<div id='selectusers_tree_id'></div>";
		html += "				</div>";
		html += "			</div>";
		html += '			<div class="modal-footer">';
		html += '				<button type="button" class="btn btn-primary"  onclick="$users.submit();">保存</button>';
		html += ' 				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>';
		html += '			</div>';
		html += "		</div>";
		html += "	</div>";
		html += "</div>";
		$("body").append(html);
	},
	submit : function() {
		var userIds = "";
		$(".icheckbox").each(function(i) {
			if ($(this).is(':checked')) {
				userIds += $(this).attr("val1") + ",";
			}
		});
		if (userIds == "") {
			$.BOOT.toast(false, "请先选择用户");
			return;
		}
		this.callback(userIds.substring(0, userIds.length - 1));
		$('#userModal').modal('hide');
	}
};