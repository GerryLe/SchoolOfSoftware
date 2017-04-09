<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
	<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!-- Content Header (Page header) -->
		<section class="content">
<div id="position-form-id"></div>
<div id="position-toolbar"></div>
<div class="row">
	<div class="col-xs-6 col-md-4">
		<div id="position_tree_id"></div>
	</div>
	<div class="col-xs-12 col-sm-6 col-md-8">
		<button type="button" id="position_permits"
			class="btn btn-success company_edit"><spring:message code="save" /></button>
		<div id="treeview1_position" class=""></div>
	</div>
</div></section>
<script type="text/javascript">
	var $position_table, $tree_position;
	var position_id;
	$.BOOT.click("#position_permits", function(c) {
		$.BOOT.savePermits($tree_position, position_id, "POSITION");
	});
	$(function() {
		var href = $.webapp.root + "/admin/system/position/tree.do";
		$.post(href, {}, function(result) {
			position_table = $('#position_tree_id').jstree({
				'plugins' : [ "wholerow" ],
				'core' : {
					'data' : result,
					'themes' : {
						'name' : 'proton',
						'responsive' : true
					}
				}
			}).bind("select_node.jstree", function(event, data) {
				position_id = data.selected[0];
				$.BOOT.getPermits($tree_position, tree_data_position, position_id, "POSITION");
			});
		}, 'json');
	});
	var tree_data_position;
	function loadmenuTree_position() {
		$.BOOT.loadMenu("treeview1_position", function($tree, data) {
			tree_data_position = data;
			$tree_position = $tree;
		});
	}
	$(function() {
		loadmenuTree_position();
	});
</script>