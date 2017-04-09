<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/template/modal.jsp"><jsp:param
		value="addusers" name="id" /><jsp:param value="编辑用户" name="title" /></jsp:include>
<style>
<!--
#uploadUser {
	font-size: 14px;
	overflow: hidden;
	position: absolute;
	margin-left: 65px;
}

#fileInput {
	position: absolute;
	z-index: 100;
	margin-left: -280px;
	font-size: 60px;
	opacity: 0;
	filter: alpha(opacity = 0);
	margin-top: -5px;
}
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
    height: 600px;
}
-->
</style>
<section class="content">
	<div id="person-form-id"></div>
	<div id="filter-bar">
		 <div class="btn-toolbar">
		   <div class="btn-group btn-group-filter-refresh">
		       <button id="buttonByKey" type="button" class="btn btn-default  btn-refresh" data-toggle="dropdown" style="display: block;" style="float: right">搜一下</button>
		   </div>
		</div> 
		<div class="btn-toolbar"> 
			<div class="form-group">
                  <input type="text" name="searchKeyName" id="searchKeyName" class="form-control" placeholder="Search">
			</div> 
		</div>
		<div class="btn-toolbar">
			<select class="form-control" id="selectType" name="selectType" style="width: 100%">
				<option value="name">英文名</option>
				<option value="account">帐号</option> 
				<option value="phone">手机</option>
				<option value="area">地区</option>
			  </select>
		  </div>
	</div>
	<table id="person_table" class="table-condensed table table-hover"
		data-row-style="rowStyle" data-side-pagination="server"></table>
		</section>
<script type="text/javascript">
	var $person_table;
	$(function() {
		$person_table = $.BOOT.table("person_table", $.webapp.root
				+ "/admin/system/user/commondatagridperson.do", {
			columns : [ {
				field : 'account',
				title : '账号',
				sortable : true
			}, {
				field : 'name',
				title : '英文名',
				sortable : true
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
			}, {
				field : 'phone',
				title : '联系手机',
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
				field : 'email',
				title : '邮箱'
			},  {
				field : 'degree',
				title : '学历'
			}, {
				field : 'birthday',
				title : '出生年月'
			},{
				field : 'employmentStr',
				title : '入职日期',
			},{
				field : 'workAge',
				title : '工龄',
			},{
				field : 'becomeStaffDate',
				title : '转正日期',
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
				title : '政治面貌',
			},{
				field : 'certificate',
				title : '证书',
			},{
				field : 'leaveDate',
				title : '离职日期',
			}],
		paginationInfo : true,
		showExport : true
		});

		$('#filter-bar').bootstrapTableFilter({
			filters : [ {
				field : 'name',
				label : '英文名',
				type : 'search'
			}, {
				field : 'chinaname',
				label : '中文名',
				type : 'search'
			},{
				field : 'phone',
				label : '手机',
				type : 'search'
			},{
				field : 'area',
				label : '地区',
				type : 'search'
			} ],
			connectTo : '#person_table'
		});console.info($("tr"))
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
	     $person_table.bootstrapTable('refresh', 
				{url: "/admin/system/user/commondatagridperson.do?searchKeyName="+$("#searchKeyName").val()+"&selectType="+$("#selectType").val()+""}); 

	
	  });
   
    $("#batch_delete").click(function(){
    	var $input = $("input#pitch");
    	var ids = [];
    	$input.each(function(){
    		if($(this).is(':checked')){
    			ids.push($(this).val());
    		}
    	})
    	if(ids.length>0){
    		var json = {
    				title : "",
    				text : "确定批量删除用户吗?",
    				showCancelButton : true,
    				type : 'warning',
    				call : function() {
    					var href = $.webapp.root + '/admin/system/user/delete.do';
    					for (var i = 0; i < ids.length; i++) {
    						$.post(href, {
    							ids : ids[i]
    						}, function(result) {
    							$person_table.bootstrapTable('refresh');
    							$.BOOT.toast1(result);
    						}, 'json');
						}
    				}
    			};
    			$.BOOT.alert(json, true);
    	}
	}); 
	
	$.BOOT.click(".person_delete", function(c) {
		var id = $(c).attr("val");
		var json = {
			title : "",
			text : "确定删除用户吗?",
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
		$.BOOT.page("content_addusers", href, function() {
			$('#addusersModal').modal('toggle');
		});
	});
	
	//邮件通知全体员工
	$.BOOT.click(".person_notice", function(c) {
		var id = $(c).attr("val");
		var href = $.webapp.root + '/admin/system/user/notice.do';
		$.post(href, {
			id : id
		}, function(result) {
			$person_table.bootstrapTable('refresh');
			$.BOOT.toast1(result);
		}, 'json');
	});
	$(document).on("click", "#person_add", function() {
		var href = $.webapp.root + '/admin/system/user/person_form_UI.do';
		$.BOOT.page("content_addusers", href, function() {
			$('#addusersModal').modal('toggle');
		});
	});

	function uploadUser() {
		var text = "";
		var flag = false;
		var options = {
			url : '/admin/system/user/checkUserAccount.do',
			type : 'post',
			dataType : 'text',
			success : function(data) {
				var dataObj = eval("(" + data + ")");
				var myArray = new Array();
				for (var i = 0; i < dataObj.length; i++) {
					$.post('/admin/system/user/importUserId.do', {
						account : dataObj[i].account
					}, function(result) {
						--i;
						if (result == 1) {
							text += "帐号" + dataObj[i].account + " ";
							flag = true;
						}
						if (i == 0 && true == flag) {
							var r = confirm(text + "等已经存在，是否替换？");
							if (r == true) {
								importUser();
								$("#fileInput").val("");
							}else{
								$("#fileInput").val("");
							}
						}
						if (i == 0 && false == flag){
							importUser();
							$("#fileInput").val("");							
						}
					}, 'json');
				}
			}

		};
		$("#formhomepage-user").ajaxSubmit(options);
	}

	function importUser() {
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
	
	$("input#pitch").parent().parent().click(function(){
		alert("ok");
	})
	//批量删除单个激活
	/* function active(c) {
		var $input = $("input#pitch");
		var flag = true;
		if (c.is(':checked')){
			c.parent().parent().addClass("active");
			$input.each(function(){
				if(!$(this).is(':checked'))
					flag = false ;
			})
				if(flag)
					$("input#pitchAll").prop("checked",true);
		}
		else{
			c.parent().parent().removeClass("active");
			$input.each(function(){
				if($(this).is(':checked'))
					flag = false ;
			})
				if(!flag)
					$("input#pitchAll").prop("checked",false);
		}
	}
	//批量删除多个激活
	function activeAll(c){
		var $input = $("input#pitch");
		if (c.is(':checked')){
	    	$input.each(function(){
	    		$(this).prop("checked",true);
	    		$(this).parent().parent().removeClass().addClass("active");
	    	})
		}
		else{
			$input.each(function(){
	    		$(this).prop("checked",false);
	    		$(this).parent().parent().removeClass().removeClass("active");
	    	})
		}
	} */
	
</script>