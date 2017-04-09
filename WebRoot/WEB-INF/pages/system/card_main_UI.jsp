<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<jsp:include page="/template/modal.jsp"><jsp:param value="addcards" name="id" /><jsp:param value="编辑考勤" name="title" /></jsp:include>
<style>
<!--
#uploadUser{ font-size:14px; overflow:hidden; position:absolute ;margin-left: 65px;}
#fileInput{position:absolute; z-index:100; margin-left:-280px; font-size:60px;opacity:0;filter:alpha(opacity=0); margin-top:-5px;}
-->
</style>
<section class="content">
	<div id="person-form-id"></div>
	<div id="filter-bar">
	<form id="formhomepage-card">
           <span id="uploadUser">
                <input type="file" id="fileInput" class="fileInput" size="1" name="uploadFile" onchange="uploadUser()"/>
                <input  type="button" class="btn btn-primary" id="filebutton" value="导入信息"/>
            </span>
   </form>
</div>
	<table id="card_table" class="table-condensed table table-hover" data-row-style="rowStyle" data-side-pagination="server"></table>
</section>
<script type="text/javascript">
    var flag = true;//防止重复提交
	var $card_table;
	$(function() {
		$card_table = $.BOOT.table("card_table", $.webapp.root
				+ "/admin/system/card/datagridcard.do", {
			columns : [ {
				field : 'account',
				title : '员工编号',
				sortable: true
			}, {
				field : 'name',
				title : '姓名',
			}, {
				field : 'recordDate',
				title : '日期',
			}, {
				field : 'startTime',
				title : '签到时间',
			}, {
				field : 'endTime',
				title : '签退时间',
			}, {
				field : 'lateTime',
				title : '迟到时间',
			}, {
				field : 'orgName',
				title : '所属部门',
			}, {
				field : 'positionName',
				title : '岗位',
			}, {
				field : 'workType',
				title : '上班方式',
			} , {
				field : 'workTime',
				title : '工作时间',
			}, ],
			paginationInfo : true,
			showExport : true
		});
		$('#filter-bar').bootstrapTableFilter({
			filters : [ {
				field : 'name',
				label : '姓名',
				type : 'search'
			}, {
				field : 'account',
				label : '编号',
				type : 'search'
			}, {
				field : 'recordDate',
				label : '日期',
				type : 'search'
			}, {
				field : 'orgName',
				label : '部门',
				type : 'search'
			} ],
			connectTo : '#card_table'
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
	
	function uploadUser() {
		var options = {
			url : '/admin/system/card/importCard.do',
			type : 'post',
			dataType : 'json',
			success : function(data) {
				$card_table.bootstrapTable('refresh');
				$.BOOT.toast1(result);
			}
		};
		$("#formhomepage-card").ajaxSubmit(options);
	}
</script>