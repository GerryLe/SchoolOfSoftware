<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<jsp:include page="/template/modal.jsp"><jsp:param value="addholidaysuser" name="id" /><jsp:param value="" name="title" /></jsp:include>
<style>
<!--
#fileInput {
	position: absolute;
	left : 5px;
	width : 54px;
	height : 28px;
	opacity: 0;
	filter: alpha(opacity = 0);
}

#holidaysuser_table {
	margin-top: 0px !important;;
}

/* 设置固定表头隐藏 */
.fixed-table-header {
	display: none !important;;
}

.table .btn-group {
	position: relative;
}
-->
</style>
<section class="content">
	<div id="holidayapplys-form-id"></div>
	<div id="filter-bar">
			<div class="btn-toolbar">
		 <form id="formhomepage-user">
                <input  type="button" class="btn btn-primary" id="filebutton" value="导入"/>
               <input type="file" id="fileInput" class="fileInput" size="1" name="uploadFile"  onchange="uploadUser()"/>
         </form>
        	</div>
	</div>
	<table id="holidaysuser_table" class="table-condensed table table-hover" data-row-style="rowStyle" data-side-pagination="server"></table>
</section>
<script type="text/javascript">
	var $holidaysuser_table;
	
	var getRoleId = $.webapp.root + "/admin/system/holidaysuser/getCurrentUserDefaultRole.do";
	$(function() {
		 $.post(getRoleId, {}, function(result) {
			    if(result==1){
			    	$("#filebutton").hide();
			    }
		}, 'json'); 
		
		$holidaysuser_table = $.BOOT.table("holidaysuser_table", $.webapp.root
				+ "/admin/system/holidaysuser/get.do", {
			columns : [ {
				field : 'id',
				/*title : '<input type="checkbox" style="display: none;"  id="pitchAll" onclick=activeAll($(this))>',*/
				 formatter : function(value, row, index) {
				return '<input type="checkbox" style="display: none;"  id="pitch" value='+value+' onclick=active($(this))>'
				 }
			}, {
				field : 'useraccount',
				title : '员工编号',
			},{
				field : 'name',
				title : '<spring:message code="Englishname" />',
			},{
				field : 'employmentDate',
				title : '入职日期',
			},{
				field : 'becomeStaffDate',
				title : '转正日期',
			},{
				field : 'shouldHoliday',
				title : '全年应有假期',
			},{
				field : 'shouldhaveannualleave',
				title : '应有年假',
			},{
				field : 'alreadyAnnualLeave',
				title : '本年度已休年假',
			},{
				field : 'theremainingannualleave',
				title : '剩余年假',
			},{
				field : 'lastyearsremainingSiLingfalse',
				title : '上年度剩余司龄假',
			},{
				field : 'thisyearshouldbeSiLingfalse',
				title : '本年度应有司龄假',
			},{
				field : 'alreadySiLingFalse',
				title : '本年度已休司龄假',
			},{
				field : 'theremainingSiLingfalse',
				title : '剩余司龄假',
			},{
				field : 'residueHoliday',
				title : '本年度剩余假期天数',
			},{
				field : 'thismonthhasbeenonmedicalleave',
				title : '本月已休病假',
			},{
				field : 'thisyearsdaysworkovertime',
				title : '本年度加班天数',
			},{
				field : 'lastyearsremainingpaidleave',
				title : '上年度剩余调休',
			},{
				field : 'theremainingpaidleave',
				title : '剩余调休',
			},{
				field : 'notvalidonanannualbasis',
				title : '本年度累计事假',
			} ],
			paginationInfo : true,
			showExport : true,
			onDblClickRow:function(row, $element){
				onDbClick(row, $element);
			}
		});
		$('#filter-bar').bootstrapTableFilter({
			filters : [ {
				field : 'name',
				label : '<spring:message code="Englishname" />',
				type : 'search'
			} ],
			connectTo : '#holidaysuser_table'
		});
		$("input.form-control").attr('placeholder','英文名');
		
		var onDbClick=function(row, $element){ 
			
				$("#holidaysuser_table tr").css('background-color','');
				$(this).css('background-color','#D0D0D0');
			    var href = $.webapp.root + '/admin/system/holidaysuser/holidaysuserSelect_main_UI.do?id='
						+ row.id;
				$.BOOT.page("content_addholidaysuser", href, function() {
					$('#addholidaysuserModal').modal('toggle');
				});
	        }

	});
	function rowStyle(row, index) {
		if (row.status == "1") {
			return {
				classes : 'danger'
			};
		}
		return {};
	}
	
	
	
	  //延迟0.2秒
	 /*  setTimeout(
			  function(){ 
					$(document).on("dblclick","#holidaysuser_table>tbody>tr",function(){
						$("#holidaysuser_table tr").css('background-color','');
						$(this).css('background-color','#D0D0D0');
					    var id = $(this).find("input").val()
					    var href = $.webapp.root + '/admin/system/holidaysuser/holidaysuserSelect_main_UI.do?id='
								+ id;
						$.BOOT.page("content_addholidaysuser", href, function() {
							$('#addholidaysuserModal').modal('toggle');
						});
				   });
		     },200); */
	 
	/*   指定的周期执行函数 ，防止刷新tr重新出现第一列  */
	 /*  setInterval("showTime()",200);
	  function showTime(){
		    $('table tr').find("th").eq(0).hide(); 
           $('#holidaysuser_table').find("tr").find("th").eq(0).hide();
		    var trlist=$('#holidaysuser_table').find("tr");
		    for(var i=0;i<trlist.length;i++){
		   	 trlist.eq(i).find("td").eq(0).hide();
		    } 
     } */
	
		/* 账号不存在提醒 */
		function uploadUser() {
			var text = "帐号";
			var i = 0;
			var flag = true;
			var options = {
				url : '/admin/system/holidaysuser/checkUserAccount.do',
				type : 'post',
				dataType : 'text',
				success : function(data) {
					var dataObj = eval("(" + data + ")");
					var myArray = new Array();
					for (i; i < dataObj.length; i++) {
						 $.ajax({
							type:"get",
							url:"/admin/system/holidaysuser/importUserId.do",
							data:"account="+ dataObj[i].useraccount,
							async:false,
							success:function(result) {
								if (result == 0) {
									text +=dataObj[i].useraccount + "、 ";
									flag = false;
								}
								if ((i+1) == dataObj.length) {
									if(!flag){
										var r = confirm(text + "不存在,请核对信息后导入");  
									}
									if(flag){
										importUser()
									}
								}
								
							}
						}); 
						 
					}
				}

			};
			$("#formhomepage-user").ajaxSubmit(options);
		}
		
	  
	function importUser(){
		var options = {
				url : '/admin/system/holidaysuser/import.do',
				type : 'post',
				dataType : 'json',
				success : function(data) {
					$holidaysuser_table.bootstrapTable('refresh');
					$.BOOT.toast1(result);
				}
			};
			$("#formhomepage-user").ajaxSubmit(options);
	}
</script>