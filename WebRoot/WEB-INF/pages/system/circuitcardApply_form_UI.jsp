<%@page import="com.rosense.basic.util.LangUtils"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script src="${webRoot}/template/resource/js/city.js"></script>

<style>
.modal-dialog{
 width:45%;
 height:40%;
 text-align: center;

}
 .employ {
	border:5px;
	width:700px;
	height:500px;
	text-align: center;
	margin-left: 23px;
}
.employ tr td{
  border:1px solid black;
}
.trsolid{
    font-weight: bold;
 }
.trleft{
  text-align: left;
}

.trcenter{
  text-align: center;
}
 input{
  border: 0px;
  text-align: center;
  height: 100%;
} 
select{
  text-align: center;
  height: 80%;
}
.inputAge{
 border-top: 0px;
 border-left: 0px;
 border-right:0px;
 border-bottom: 1px;
 width: 30px;
}
</style>
<script>
    var form_url ="";
	var parent_box, person_type;
	var flag = true;//防止重复提交
	var flagphoto=false;
	var flag1=true;
    var idString=$('input[name=id]').val();
    var idselect=idString.substring(idString.length-6); 
    $(function() {
		//编辑，加载表单数据
		var orgtree = $.webapp.root + "/admin/system/org/tree.do";
		var ptree = $.webapp.root + "/admin/system/position/tree.do";
		var get = $.webapp.root + "/admin/system/circuitcard/get.do";
		var chargeget=$.webapp.root + "/admin/system/user/chargeTree.do";
		 $.BOOT.autoselectcharge("orgChargeName", chargeget, {
		title : "选择部门主管"
	    }); 
		 $.BOOT.autoselect("orgId", orgtree, {
			title : "选择原部门"
		});
		$.BOOT.autoselect("positionId", ptree, {
			title : "选择新职位",
			callback : function() {
				if ($('input[name=id]').val().length > 0) {
					if(idselect=="select"){
						$("#save_circuitcard").hide();
						$("select").attr("disabled", true);
						$("input,textarea,select").attr("readonly", "readonly");
						$('input[name=id]').val(idString.substring(0,idString.length-6));
					 }else{
						 $("#save_circuitcard").show();
					 }
						$.post(get, {
							id : $('input[name=id]').val()
						}, function(result) {
							form_url = $.webapp.root+ "/admin/system/circuitcard/update.do";
							$('#form_circuitcard').form('load', result);
						}, 'json');
					}else{
						urlget=$.webapp.root+ "/admin/system/user/selectCurUser.do";
						$.post(urlget, {}, function(result) {
							form_url = $.webapp.root + "/admin/system/circuitcard/add.do";
							$('#form_circuitcard').form('load', result);
						}, 'json');
					}
			}
		});  
		
		$('.form_date').datetimepicker({
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
	 $("#phone").blur(function(){
		 var flag=false;
	      var message = "";  
	      var myreg=/^1[3|4|5|8]\d{9}$/;
		  phone = this.value;
		  if(!myreg.test(phone)){
	        message = "请输入有效的手机号码！";
	      }else{
	    	  $("#phonereg").hide();
	    	  $("#save_transfer").removeAttr("disabled"); 
	    	  flag=true;
	      }
	      if(!flag){
	     //提示错误效果
	        $("#phonereg").html(message);
	        $("#save_circuitcard").attr("disabled", true);
	      }
	      
	}); 
	 
	 $.BOOT.form("form_circuitcard", {
		 name : {
			validators : {
				notEmpty : {
					message : '英文名不能为空'
				}
			}
		},
		chinaname : {
			validators : {
				notEmpty : {
					message : '中文名不能为空'
				}
			}
		},
		phone : {
			validators : {
				notEmpty : {
					message : '手机号码不能为空'
				}
			}
		},
 }, function(params) {
			 if(flag){
			$.post(form_url, params, function(result) {
				 $.BOOT.toast1(result); 
				 $person_table.bootstrapTable('refresh'); 
				modalcircuitcard();
			}, 'json');
			flag = false;
		 } 
	});  
	
</script>

<div class="row">
	<input type="hidden" name="id" value="${id}">
	<table class="employ">
	  <tr class="trsolid trcenter  form-group">
	  <td>中文名</td>
	  <td>英文名</td>
	  <td>性别</td>
	  <td>邮箱</td>
	  </tr>
	  
	  <tr class="trcenter form-group">
	  <td><input type="text" name="chinaname" id="chinaname" style="width: 100%" placeholder="输入中文名"/></td>
	  <td><input type="text" name="name" id="name" placeholder="输入英文名" style="width: 100%"/></td>
	  <td>
	     <select id="sex" name="sex" style="width: 100%;text-align: center;height: 100%">
			<option value="男">男</option>
			<option value="女">女</option>
		 </select>
	  </td>
	  <td><input type="email" name="email" id="email" placeholder="输入邮箱" style="width: 100%"/></td>
	  </tr>
	  
	  <tr class="trsolid trcenter form-group">
	  <td>手机号码<span id="phonereg" style="color: red; font-size: 12px;  display：none;"></span></td>
      <td>座机号码</td>
	  <td>部门</td>
	  <td>职位<span id="degreeReg" style="color: red; font-size: 12px;  display：none;"></span></td>
	  </tr>
	  
	  <tr class="trcenter form-group">
	  <td><input type="text" name="phone" id="phone" placeholder="输入手机号码" style="width: 100%"/></td>
	  <td><input type="text" name="telePhone" id="telePhone" placeholder="输入座机号码" style="width: 100%"/></td>
	  <td>
	     <select id="orgId" name="orgId" style="width: 100%;height: 100%"> </select>
	  </td>
	  <td>
		<select id="positionId" name="positionId" style="width: 100%;height: 100%"> </select>
	  </td>
	  </tr>
	  
	  <tr class="trcenter disable form-group">
	  <td class="trsolid">上级英文名</td>
	  <td>
	  <!-- <input type="text" name="orgChargeName" id="orgChargeName"  style="width: 100%"/> -->
	  <select id="orgChargeName" name="orgChargeName" style="width: 100%;height: 100%"> </select>
	  </td>
	  <td class="trsolid">审批结果</td>
	  <td><input readonly="readonly" type="text" name="orgChargeResult" id="orgChargeResult"  style="width: 100%"/></td>
	  </tr>
	  
	  <tr class="trsolid trleft disable">
	  <td colspan="4">
	      审批人备注:
	  </td>
	  </tr>
	  
	  <tr class="disable form-group">
	  <td colspan="4">
	   <textarea readonly="readonly"  name="orgChargeRemark" rows="6" style="width: 100%"></textarea>
	  </td>
	  </tr>
	  
	</table>
	</div>