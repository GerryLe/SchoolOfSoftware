<%@page import="com.rosense.basic.util.LangUtils"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" type="text/css"
	href="${webRoot}/template/resource/css/bootstrap-clockpicker.min.css">
<link rel="stylesheet" type="text/css" href="${webRoot}/template/resource/css/github.min.css">
<script type="text/javascript" src="${webRoot}/template/resource/js/bootstrap-clockpicker.min.js"></script>
<script src="${webRoot}/template/resource/js/city.js"></script>

<div class="row">
	<input type="hidden" name="id" value="${id}">

	<div class="form-group col-md-6">
		<label for="applyOrg">部门</label> <select class="form-control" id="applyOrg" name="applyOrg">
		</select>
	</div>

	<div class="form-group col-md-6">
		<label for="applyName">姓名</label> <input type="text" class="form-control" name="applyName"
			id="applyName" placeholder="输入姓名">
	</div>

	<div class="form-group col-md-6">
		<label for="number"> 人数<span id="numberreg" style="color: red; font-size: 12px;"></span>
		</label> <input type="text" class="form-control" name="number" id="number" placeholder="输入人数">
	</div>

	<div class="form-group col-md-6">
		<label for="device"> 设备<span id="devicereg" style="color: red; font-size: 12px;"></span>
		</label> <input type="text" class="form-control" name="device" id="device" placeholder="输入设备">
	</div>

	<div class="form-group col-md-6">
		<label for="applyDate"> 申请日期<span id="datereg" style="color: red; font-size: 12px;"></span>
		</label> <br /> <input class="date_class" type="text" id="applyDate" name="applyDate" readonly
			style="float: left; width: 185px; height: 35px"> <span class="input-group-addon"
			id="glyphicon-remove" style="margin: 0; width: 42px; height: 35px; float: left; cursor: p;"><span
			class="glyphicon glyphicon-remove"></span></span> <span id="calendar-id"
			class="input-group form_dateb date col-md-5" data-date="" data-date-format="yyyy-mm-dd"
			data-link-field="applyDate" data-link-format="yyyy-mm-dd"
			style="margin: 0; width: 43px; height: 35px; background-color: gray; float: left"> <span
			class="input-group-addon" id="glyphicon-calendar"
			style="margin: 0; width: 43px; height: 35px; background-color: #F0F0F0; float: right"><span
				class="glyphicon glyphicon-calendar"></span></span>
		</span>
	</div>

	<div class="form-group col-md-6">
		<label for="applyTime">申请时间</label> <br />
		<div class="input-group clockpicker" data-placement="right" data-align="top" data-autoclose="true">
			<input type="text" class="form-control" id="applyTime" name="applyTime"> <span
				class="input-group-addon"> <span class="glyphicon glyphicon-time"></span>
			</span>
		</div>
	</div>

	<div class="form-group col-md-6">
		<label for="applyRoom"> 会议室<span id="materialreg" style="color: red; font-size: 12px;"></span>
		</label> <select class="form-control" id="applyRoom" name="applyRoom">
			<option value="A区会议室">A区会议室</option>
			<option value="B区会议室">B区会议室</option>
			<option value="C区会议室">C区会议室</option>
			<option value="D区会议室">D区会议室</option>
			<option value="E区会议室">E区会议室</option>
			<option value="F区会议室">F区会议室</option>
		</select>
	</div>

	<div class="form-group col-md-6">
		<label for="material"> 物资<span id="materialreg" style="color: red; font-size: 12px;"></span>
		</label> <input type="text" class="form-control" name="material" id="material" placeholder="输入物资">
	</div>

	<div class="form-group col-md-6">
		<label for="remark"> 备注<span id="remarkreg" style="color: red; font-size: 12px;"></span>
		</label> <input type="text" class="form-control" name="remark" id="remark" placeholder="输入备注">
	</div>
</div>
<script>
	var form_url = $.webapp.root + "/admin/system/boardroom/add.do";
	var flag = true;//防止重复提交
	$(function() {
		 $('.clockpicker').clockpicker();
		var orgtree = $.webapp.root + "/admin/system/org/tree.do";
		$.BOOT.autoselect("applyOrg", orgtree, {
			title : "选择职位",
			callback : function() {
		    var get = $.webapp.root + "/admin/system/boardroom/get.do";
				if ($('input[name=id]').val().length > 0) {
					$.post(get, {
						id : $('input[name=id]').val()
					}, function(result) {
						form_url = $.webapp.root+ "/admin/system/boardroom/update.do";
						$('#form_addBoardroom').form('load', result);
					}, 'json');
				} else {
				}
			}
		}); 
		$('.form_dateb').datetimepicker({
	        language:  'zh-CN',
	        weekStart: 1,
	        todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			minView: 2,
			forceParse: 0
	    });
		
	});
	
	
	$.BOOT.form("form_addBoardroom", {
		applyDate : {
			validators : {
				notEmpty : {
					message : '日期不能为空'
				}
			}
		},
		applyTime : {
			validators : {
				notEmpty : {
					message : '时间不能为空'
				}
			}
		},
		applyOrg : {
			validators : {
				notEmpty : {
					message : '部门不能为空'
				}
			}
		},
		applyName : {
			validators : {
				notEmpty : {
					message : '姓名不能为空'
				}
			}
		},
	}, function(params) {
			if(flag){
			$.post(form_url, params, function(result) {
				$.BOOT.toast1(result);
				$boardroom_table.bootstrapTable('refresh');
				modaladdBoardroom();
				history.go(0);
			}, 'json');
			flag = false;
		 }
	});
    $("#glyphicon-remove").click(function(){
    	$("#applyDate").val("");
    	$("#datereg").html("入职日期不能为空");
    	$("#save_addBoardroom").attr("disabled", true);
    });
    $("#glyphicon-calendar").click(function(){
    	  $("#datereg").html("");
    	  $("#save_addBoardroom").removeAttr("disabled"); 
    });
  
 </script>
