<%@page import="com.rosense.basic.util.LangUtils"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="container">
		<input type="hidden" name="id" value="${id}">
	<div class="row">
		<div class="form-group col-md-3">
			<label>文具名称</label>
			<input type="text" name="stationeryName" class="form-control">
		</div>
		<div class="form-group col-md-3">
			<label>文具单位</label>
			<input type="text" name="stationeryUnit" class="form-control" >
		</div>
	</div>
	<div class="row">
		<div class="form-group col-md-3">
			<label>文具型号</label>
			<input type="text" name="stationeryType" class="form-control" >
		</div>
		<div class="form-group col-md-3">
			<label>文具单价/元</label>
			<input type="text" name="price" class="form-control"  onkeyup="value=value.replace(/[^\d.]/g,'')">
		</div>
	</div>
	<div class="row">
		<div class="form-group col-md-6">
			<label>备注</label>
			<input type="text" name="remark" class="form-control">
		</div>
	</div>
</div>
<script>
	var flag=true;
	var form_url = $.webapp.root + "/admin/system/stationeries/addstationeries.do";

	$(function(){
		var href = $.webapp.root + "/admin/system/stationeries/getstationeries.do";
		if ($('input[name=id]').val().length > 0) {
			$.post(href, {
				id : $('input[name=id]').val()
			}, function(result) {
				form_url = $.webapp.root+ "/admin/system/stationeries/updatestationeries.do";
				$('#form_addstationeries').form('load', result);
			}, 'json');
		}
	})
	$.BOOT.form("form_addstationeries", {
		stationeryName :{
			validators :{
				notEmpty :{
					message : '文具名称不能为空 '
				}
			}
		},price :{
			validators :{
				notEmpty :{
					message : '单价不能为空 '
				}
			}
		},
	}, function(params) {
		if(flag){
		$.post(form_url, params, function(result) {
			$.BOOT.toast1(result);
			$stationeries_table.bootstrapTable('refresh');
			modaladdstationeries();
		}, 'json');
		flag=false
	}
	});
	
	
</script>