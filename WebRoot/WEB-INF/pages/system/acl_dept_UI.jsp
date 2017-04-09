<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
	<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
		<section class="content">
<div id="org-toolbar"></div>
<div class="row">
	<div class="col-xs-6 col-md-4">
		<div id="org_tree_id"></div>
	</div>
	<div class="col-xs-12 col-sm-6 col-md-8">
		<button type="button" id="org_permits"
			class="btn btn-success company_edit"><spring:message code="save" /></button>
		<div id="treeview1_org" class=""></div>
	</div>
</div></section>
<script type="text/javascript">
	var $org_table, $tree_org;
	var org_id;
	$.BOOT.click("#org_permits", function(c) {
		$.BOOT.savePermits($tree_org, org_id, "DEPT");
	});
	$(function() {
		var href = $.webapp.root + "/admin/system/org/tree.do";
		$.post(href, {}, function(result) {
			org_table = $('#org_tree_id').jstree({
				'plugins' : [ "wholerow" ],
				'core' : {
					'data' : result,
					'themes' : {
						'name' : 'proton',
						'responsive' : true
					}
				}
			}).bind("select_node.jstree", function(event, data) {
				org_id = data.selected[0];
				$.BOOT.getPermits($tree_org, tree_data_org, org_id, "DEPT");
			});
		}, 'json');
	});
	var tree_data_org;
	function loadmenuTree_org() {
		$.BOOT.loadMenu("treeview1_org", function($tree, data) {
			tree_data_org = data;
			$tree_org = $tree;
		});
	}
	$(function() {
		loadmenuTree_org();
	});
</script>