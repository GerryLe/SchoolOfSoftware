<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!-- Content Header (Page header) -->
<section class="content">
	<div id="menu-form-id"></div>
	<div id="treeview1" class=""></div>
</section>
<script type="text/javascript">
	function loadmenuTree() {
		var href = $.webapp.root + "/admin/system/menu/tree.do";
		$.post(href, {}, function(result) {
			$('#treeview1').empty();
			$('#treeview1').jstree({
				'plugins' : [ "wholerow" ],
				'core' : {
					'data' : result,
					'themes' : {
						'name' : 'proton',
						'responsive' : true
					}
				}
			});
		}, 'json').error(function() {
		});
	}
	$(function() {
		loadmenuTree();
	});
</script>