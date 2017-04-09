<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
	<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:include page="/template/modal.jsp"><jsp:param value="addorgusers" name="id" /><jsp:param value="编辑部门用户" name="title" /></jsp:include>
<style>
<!--
#uploadUser{ font-size:14px; overflow:hidden; position:absolute ;margin-left: 65px;}
#fileInput{position:absolute; z-index:100; margin-left:-280px; font-size:60px;opacity:0;filter:alpha(opacity=0); margin-top:-5px;}
#person_table{
  width: 4500px;
}
.fixed-table-header{
   margin-right: 0px;
   display:none;
}
.table .btn-group{
   position:relative; 
}
.fixed-table-container{
    height: 100%;
}
-->
</style>
<section class="content">
	<div id="person-form-id"></div>
	<div id="filter-bar">
		<%-- <div class="btn-toolbar">
			<button type="button" class="btn btn-primary " id="person_add"><spring:message code="add" /></button>
		</div>
		<div class="btn-toolbar">
		 <form id="formhomepage-user">
           <span id="uploadUser">
               <input type="file" id="fileInput" class="fileInput" size="1" name="uploadFile" onchange="uploadUser()"/>
                <input  type="button" class="btn btn-primary" id="filebutton" value="<spring:message code="Importtheinformation" />"/>
            </span>
            </form>
        </div> --%>
	</div>
	<table id="person_table" class="table-condensed table table-hover" data-row-style="rowStyle" data-side-pagination="server"></table>
	<!-- <div id="treeview1" class="" style="min-height: 500px;"></div> -->
</section>
<script type="text/javascript">
	var $person_table;
	$(function() {
		$person_table = $.BOOT.table("person_table", $.webapp.root
				+ "/admin/system/user/orgdatagridperson.do", {
					columns : [ {
						field : 'name',
						title : '英文名',
						sortable: true
					}, {
						field : 'chinaname',
						title : '中文名',
					}, {
						field : 'sex',
						title : '性别',
					}, {
						field : 'age',
						title : '年龄',
					}, {
						field : 'area',
						title : '地区',
					},{
						field : 'phone',
						title : '手机',
					}, {
						field : 'orgName',
						title : '部门',
					}, {
						field : 'orgChildName',
						title : '部门2',
					}, {
						field : 'positionName',
						title : '职位',
					}, {
						field : 'positionEng',
						title : '职位英文',
					}, {
						field : 'degree',
						title : '学历'
					}, {
						field : 'birthday',
						title : '出生年月'
					}, {
						field : 'idcard',
						title : '身份证号码',
					},{
						field : 'securityDate',
						title : '社保日期'
					}, {
						field : 'fundDate',
						title : '公积金',
					}, {
						field : 'employmentStr',
						title : '入职日期',
					},{
						field : 'leaveDate',
						title : '离职日期',
					},{
						field : 'workAge',
						title : '工龄',
					},{
						field : 'probationLimit',
						title : '试用期限',
					},{
						field : 'probationEnd',
						title : '试用到期',
					},{
						field : 'becomeStaffDate',
						title : '试用转正日期',
					},{
						field : 'agreementLimit',
						title : '合同期限',
					},{
						field : 'agreementStartDate',
						title : '合同开始计算日期',
					},{
						field : 'agreementEndDate',
						title : '合同到期',
					},{
						field : 'agreementTimes',
						title : '合同签订次数',
					},{
						field : 'marriage',
						title : '婚姻状况',
					},{
						field : 'bear',
						title : '生育状况',
					}, {
						field : 'nation',
						title : '民族',
					},{
						field : 'politicalFace',
						title : '政治面面貌',
					},{
						field : 'origin',
						title : '籍贯',
					},{
						field : 'accountAddr',
						title : '户籍地址',
					},{
						field : 'accountPro',
						title : '户口性质',
					},{
						field : 'address',
						title : '现住址',
					},  {
						field : 'school',
						title : '毕业学校',
					},{
						field : 'graduation',
						title : '毕业时间',
					},{
						field : 'profession',
						title : '专业',
					},{
						field : 'certificate',
						title : '证书',
					},{
						field : 'contact',
						title : '联系人',
					},{
						field : 'contactPhone',
						title : '联系人电话',
					},{
						field : 'workOld',
						title : '工作经历',
					},{
						field : 'material',
						title : '资料',
					},{
						field : 'bankCard',
						title : '银行卡',
					},{
						field : 'train',
						title : '培训经历',
					},{
						field : 'securityCard',
						title : '社保卡',
					},{
						field : 'fund',
						title : '公积金簿',
					}/* , {
						field : 'id',
						title : '操作',
						formatter : function(value, row, index) {
							return $.BOOT.groupbtn(value, [ {
								cla : 'person_edit',
								text : "编辑"
							}, {
								cla : 'person_delete',
								text : "删除"
							} ]);
						}
					} */ ],
			paginationInfo : true,
			showExport : true
		});
		$('#filter-bar').bootstrapTableFilter({
			filters : [ {
				field : 'name',
				label : '<spring:message code="Englishname" />',
				type : 'search'
			}, {
				field : 'chinaname',
				label : '<spring:message code="Chinesename" />',
				type : 'search'
			}, {
				field : 'phone',
				label : '<spring:message code="phone" />',
				type : 'search'
			}  ],
			connectTo : '#person_table'
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
	$.BOOT.click(".person_delete", function(c) {
		var id = $(c).attr("val");
		var json = {
			title : "",
			text : "<spring:message code="Suretodelete" />",
			showCancelButton : true,
			type : 'warning',
			call : function() {
				var href = $.webapp.root + '/admin/system/user/delete.do';
				$.post(href, {
					ids : id
				}, function(result) {
					$person_table.bootstrapTable('refresh');
					$.BOOT.toast1(result);
				}, 'json');
			}
		};
		$.BOOT.alert(json, true);
	});
	$.BOOT.click(".person_edit", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/user/person_form_UI.do?id='
				+ id;
		$.BOOT.page("content_addorgusers", href, function() {
			$('#addorgusersModal').modal('toggle');
		});
	});
	$(document).on("click", "#person_add", function() {
		var href = $.webapp.root + '/admin/system/user/person_form_UI.do';
		$.BOOT.page("content_addorgusers", href, function() {
			$('#addorgusersModal').modal('toggle');
		});
	});
	
	function uploadUser() {
		var options = {
			url : '/admin/system/user/importUser.do',
			type : 'post',
			dataType : 'json',
			success : function(data) {
				$person_table.bootstrapTable('refresh');
				$.BOOT.toast1(result);
			}
		};
		$("#formhomepage-user").ajaxSubmit(options);
	}
	

</script>