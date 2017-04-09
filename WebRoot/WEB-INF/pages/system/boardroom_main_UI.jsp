<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/template/modal.jsp"><jsp:param value="addBoardroom" name="id" /><jsp:param
		value="编辑" name="title" /></jsp:include>
<link href='${webRoot}/template/resource/css/feature.presenter.1.5.css' rel='stylesheet'
	type='text/css'>
<script src="${webRoot}/template/resource/js/feature.presenter.1.5.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="${webRoot}/template/resource/css/bootstrap-clockpicker.min.css">
<link rel="stylesheet" type="text/css" href="${webRoot}/template/resource/css/github.min.css">
<script src="${webRoot}/template/resource/js/bootstrap-clockpicker.min.js"></script>
<style type="text/css">
.input-group {
	width: 110px;
	margin-bottom: 10px;
}
</style>

<section class="content">
	<div id="boardroom-form-id"></div>
	<div id="filter-bar">
		<div class="btn-toolbar">
			<button type="button" class="btn btn-primary " id="boardroom_add">申请</button>
		</div>
	</div>
	<table id="boardroom_table" class="table-condensed table tablse-hover" data-row-style="rowStyle"
		data-side-pagination="server"></table>
</section>
<div id="test-element"></div>
<div id="targetMenu"></div>
<script type="text/javascript">
	var $boardroom_table;
	var getId = $.webapp.root + "/admin/system/boardroom/selectId.do";
	var getJOSN = $.webapp.root + "/admin/system/boardroom/JSONString.do";
	
	$(function() {
		//获取当前用户色申请会议室的信息
		var idList = "";
		$.post(getId, {}, function(result) {
			for(var i=0;i<result.length;i++){
				idList+=result[i].id+" ";
			}
			$boardroom_table = $.BOOT.table("boardroom_table", $.webapp.root
					+ "/admin/system/boardroom/select.do", {
				columns : [ {
					field : 'orgName',
					title : '部门',
				}, {
					field : 'applyName',
					title : '姓名',
				}, {
					field : 'number',
					title : '人数',
				}, {
					field : 'applyRoom',
					title : '会议室',
				}, {
					field : 'applyDate',
					title : '申请日期',
					sortable : true
				}, {
					field : 'applyTime',
					title : '申请时间',
					sortable : true
				}, {
					field : 'device',
					title : '设备',
				}, {
					field : 'material',
					title : '物资',
				}, {
					field : 'remark',
					title : '备注',
				}, {
					field : 'id',
					title : '操作',
					formatter : function(value, row, index) {
						alert(idList.indexOf(value))
							if (idList.indexOf(value)!=-1) {
								return $.BOOT.groupbtn(value, [ {
									cla : 'boardroom_edit',
									text : "编辑"
								}, {
									cla : 'boardroom_delete',
									text : "删除"
								} ]);
							} else {
								return $.BOOT.simplebtn(value, []);
							}
						
					}
				} ],
				paginationInfo : true,
				showExport : true
			});
		}, 'json');
		$.post(getJOSN, {
			d : new Date().getTime()
		}, function(result) {
			var options = {
				circle_radius : 220,
				normal_feature_size : 140,
				highlighted_feature_size : 160,
				top_margin : 100,
				bottom_margin : 50,
				spacing : 40,
				min_padding : 50,
				heading_font_size : 30,
				description_font_size : 20,
				type : 'image'
			};
			var fp = new FeaturePresenter($("#test-element"), result, options);
			fp.createPresenter();
		}, 'json');

		
		$('#filter-bar').bootstrapTableFilter({
			filters : [ {
				field : 'applyDate',
				label : '日期',
				type : 'search'
			}, {
				field : 'applyTime',
				label : '时间',
				type : 'search'
			}, {
				field : 'applyName',
				label : '姓名',
				type : 'search'
			} ],
			connectTo : '#boardroom_table'
		});
	});

	//延迟0.2秒
	/*  setTimeout(
			  
			  function(){ 
				    
					$(document).on("dblclick","#boardroom_table>tbody>tr",function(){
						$("#boardroom_table tr").css('background-color','');
						$(this).css('background-color','#D0D0D0');
						var id = $(this).find(".btn-group").find("ul li:first").attr("val");
						var href = $.webapp.root + '/admin/system/boardroom/boardroom_form_UI.do?id='
						+ id;
						$.BOOT.page("content_addBoardroom", href, function() {
					    $('#addBoardroomModal').modal('toggle');
				});
				   });
		     },200); */

	function rowStyle(row, index) {
		if (row.status == "1") {
			return {
				classes : 'danger'
			};
		}
		return {};
	}

	$(document).on(
			"click",
			"#boardroom_add",
			function() {
				var href = $.webapp.root
						+ '/admin/system/boardroom/boardroom_form_UI.do';
				$.BOOT.page("content_addBoardroom", href, function() {
					$('#addBoardroomModal').modal('toggle');
				});
			});

	$.BOOT.click(".boardroom_edit", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root
				+ '/admin/system/boardroom/boardroom_form_UI.do?id=' + id;
		$.BOOT.page("content_addBoardroom", href, function() {
			$('#addBoardroomModal').modal('toggle');
		});
	});

	$.BOOT.click(".boardroom_delete", function(c) {
		var id = $(c).attr("val");
		var json = {
			title : "",
			text : "确定删除吗?",
			showCancelButton : true,
			type : 'warning',
			call : function() {
				var href = $.webapp.root + '/admin/system/boardroom/delete.do';
				$.post(href, {
					ids : id
				}, function(result) {
					$boardroom_table.bootstrapTable('refresh');
					$.BOOT.toast1(result);
				}, 'json');
			}
		};
		$.BOOT.alert(json, true);
	});

	function createMenu2() {
		var targetDom = document.getElementById("targetMenu");
		nm = new nice_menu(targetDom);

		nm.initScale = 1.0;
		nm.distributionType = 0;
		nm.delay = 50;
		nm.gap = 1;
		nm.nm_ItemsData = [
				[ "Menu1", "#", "_self",
						"${webRoot}/template/resource/images/meet1.jpg" ],
				[ "Menu2", "#", "_self",
						"${webRoot}/template/resource/images/meet2.jpg" ],
				[ "Menu3", "#", "_self",
						"${webRoot}/template/resource/images/meet3.jpg" ],
				[ "Menu4", "#", "_self",
						"${webRoot}/template/resource/images/meet1.jpg" ],
				[ "Menu5", "#", "_self",
						"${webRoot}/template/resource/images/meet3.jpg" ],
				[ "Menu6", "#", "_self",
						"${webRoot}/template/resource/images/meet2.jpg" ],
				[ "Menu7", "#", "_self",
						"${webRoot}/template/resource/images/meet3.jpg" ],
				[ "Menu8", "#", "_self",
						"${webRoot}/template/resource/images/meet1.jpg" ],
				[ "Menu9", "#", "_self",
						"${webRoot}/template/resource/images/meet2.jpg" ],
				[ "Menu10", "#", "_self",
						"${webRoot}/template/resource/images/meet3.jpg" ] ];

		nm.nm_createMenu();
	}

	/*
	var settings = [ {image: '${webRoot}/template/resource/images/meet1.jpg', heading: 'A区会议室', description: 'DC,4人,2016-11-1 10:30'}, 
	 				{image: '${webRoot}/template/resource/images/meet2.jpg', heading: 'B区会议室', description: '技术部,7人,2016-11-5 14:30'}, 
	 				{image: '${webRoot}/template/resource/images/meet1.jpg', heading: 'C区会议室', description: '研发部,4人,2016-11-1 9:30'}, 
	 				{image: '${webRoot}/template/resource/images/meet3.jpg', heading: 'D区会议室', description: 'PTR,4人,2016-11-1 10:30'}, 
	 				{image: '${webRoot}/template/resource/images/meet1.jpg', heading: 'E区会议室', description: '人事部,5人,2016-11-7 20:30'},
	 				{image: '${webRoot}/template/resource/images/meet2.jpg', heading: 'F区会议室', description: '财务部,3人,2016-11-4 10:30'}
	 				];

	  var options = {
	 	circle_radius: 220,
	 	normal_feature_size: 140,
	 	highlighted_feature_size: 160,
	 	top_margin: 100,
	 	bottom_margin: 50,
	 	spacing: 40,
	 	min_padding: 50,
	 	heading_font_size: 30,
	 	description_font_size: 20,
	 	type: 'image'
	 };

	  var fp = new FeaturePresenter($("#test-element"), settings, options);
	 fp.createPresenter();  */
</script>