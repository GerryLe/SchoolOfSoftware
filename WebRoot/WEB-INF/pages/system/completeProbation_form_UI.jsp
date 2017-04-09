<%@page import="com.rosense.basic.util.LangUtils"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script src="${webRoot}/template/resource/js/city.js"></script>

<style>
.modal-dialog{
 width:45%;
 height:60%;
 text-align: center;

}
 .employ {
	border:5px;
	width:700px;
	height:850px;
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
    var form_url = $.webapp.root + "/admin/system/user/add.do";
	var parent_box, person_type;
	var flag = true;//防止重复提交
	var flagphoto=false;
	var flag1=true;
    $(function() {
		//编辑，加载表单数据
		$(".modal-title").html("转正信息");
		var orgtree = $.webapp.root + "/admin/system/org/tree.do";
		var ptree = $.webapp.root + "/admin/system/position/tree.do";
		var get = $.webapp.root + "/admin/system/probation/get.do";
		
		/* region_init("province","area","city"); */
		
		  $.BOOT.autoselect("orgId", orgtree, {
			title : "选择部门"
		});
		$.BOOT.autoselect("orgChildId", orgtree, {
			title : "选择子部门"
		});  
		$.BOOT.autoselect("positionId", ptree, {
			title : "选择职位",
			callback : function() {
				if ($('input[name=id]').val().length > 0) {
					  var idAll=$('input[name=id]').val();
					  var id=idAll.substring(0,idAll.length-2)
					  var resultFlag=idAll.substring(idAll.length-2);
						$.post(get, {
							id : id
						}, function(result) {
							 if(resultFlag==2||resultFlag==23){
								   $("select").attr("disabled", true);
								   $("input,textarea,select").attr("readonly", "readonly");
								   $("textarea[name=orgChargeRemark]").removeAttr("readonly");
							 }else if(resultFlag==4||resultFlag==43){
								   $("select").attr("disabled", true);
								   $("input,textarea,select").attr("readonly", "readonly");
								   $("textarea[name=orgHeadRemark]").removeAttr("readonly");
							    }else{ 
								$("select").attr("disabled", true);
								$("input,textarea,select").attr("readonly", "readonly");
								
							}
							 form_url = $.webapp.root+ "/admin/system/probation/update.do";
							 $('#form_addremark').form('load', result);
						}, 'json');
					 } else {
				}
			}
		});  
		 /* 隐藏"保存"按钮  */
		$("#save_addremark").hide();
	});
    
    $.BOOT.click("#permit_addremark", function(c) {
		form_url = $.webapp.root + '/admin/system/probation/true.do';
	});
	$.BOOT.click("#refuse_addremark", function(c) {
		form_url = $.webapp.root + '/admin/system/probation/false.do';
	}); 
 	 $.BOOT.form("form_addremark", {
	},function(params) {
		if(flag){
		$.post(form_url, params, function(result) {
			$.BOOT.toast1(result);
			$person_table.bootstrapTable('refresh');
			modaladdremark();
		}, 'json');
		flag=false
	}
	}); 
    
	 $("#phone").blur(function(){
		 var flag=false;
	      var message = "";
	      //var myreg = /^(((13[0-9]{1})|(14[0-9]{1})|(17[0]{1})|(15[0-3]{1})|(15[5-9]{1})|(18[0-9]{1}))+\d{8})$/;  
	      var myreg=/^1[3|4|5|8]\d{9}$/;
		  phone = this.value;
	      if(phone.length !=11){
	        message = "请输入有效的手机号码,为11位！";
	      }else if(!myreg.test(phone)){
	        message = "请输入有效的手机号码！";
	      }else{
	    	  $("#phonereg").hide();
	    	  $("#save_addremark").removeAttr("disabled"); 
	    	  flag=true;
	      }
	      if(!flag){
	     //提示错误效果
	        $("#phonereg").html(message);
	        $("#save_addremark").attr("disabled", true);
	      }
	      
	}); 
	
</script>

<div class="row">
	<input type="hidden" name="id" value="${id}">
	<table class="employ form-group">
	  <tr class="trsolid trcenter">
	  <td>账号</td>
	  <td>中文名</td>
	  <td>英文名</td>
	  <td>手机<span id="phonereg" style="color: red; font-size: 12px;  display：none;"></span></td>
	  </tr>
	  
	  <tr class="trcenter">
	  <td><input type="text" name="account" id="account" style="width: 100%" placeholder="输入账号"/></td>
	  <td><input type="text" name="chinaname" id="chinaname" style="width: 100%" placeholder="输入中文名"/></td>
	  <td><input type="text" name="name" id="name" placeholder="输入英文名" style="width: 100%"/></td>
	  <td><input type="text" name="phone" id="phone" placeholder="输入手机" style="width: 100%"/></td>
	  </tr>
	  
	  <tr class="trsolid trcenter">
	  <td>性别</td>
	  <td>年龄</td>
	  <td>地区</td>
	  <td>邮箱</td>
	  </tr>
	  
	  <tr class="trcenter">
	  <td>
		     <select id="sex" name="sex" style="width: 100%;text-align: center">
				<option value="男">男</option>
				<option value="女">女</option>
			 </select>
		
	  </td>
	  <td>
	     	<input type="text" name="age" id="age" placeholder="输入年龄" style="width: 100%"/>
	  </td>
	 <!--  <td>
			<select name="province" id="province"  style="width: 45%"></select> 
	        <select name="area" id="area"  style="width: 45%"></select> 
	        <select id="city" name="city" style="display: none"></select>
	  </td> -->
	   <td>
	        <select id="area" name="area" style="width: 100%">
			<option value="GZ">GZ</option>
			<option value="SH">SH</option>
			<option value="BJ">BJ</option>
			<option value="XG">XG</option>
		  </select>
	  </td>
	   <td><input type="email" name="email" id="email" placeholder="输入邮箱" style="width: 100%" /></td>
	  </tr>
	 
	  <tr class="trsolid trcenter">
	  <td>部门</td>
	  <td>部门2</td>
	  <td>职位</td>
	  <td>职位英文</td>
	  </tr>
	  
	  <tr class="trcenter">
	  <td> <select id="orgId" name="orgId"  style="width: 100%"> </select></td>
	  <td><select id="orgChildId" name="orgChildId" style="width: 100%"> </select></td>
	  <td><select  id="positionId" name="positionId" style="width: 100%"></select></td>
	  <td><input type="text"  name="positionEng" id="positionEng" placeholder="输入职位英文"style="width: 100%" /></td>
	  </tr> 
	  
	  <tr class="trsolid trcenter">
	  <td>学历<span id="degreeReg" style="color: red; font-size: 12px;  display：none;"></span></td>
	  <td>入职日期<span id="degreeReg" style="color: red; font-size: 12px;  display：none;"></span></td>
	  <td>试用期限</td>
	  <td>转正日期</td>
	  </tr>
	  
	  <tr class="trcenter">
	   <td>
	       <select  id="degree" name="degree" style="width: 100%">
				<option value="MBA">MBA</option>
				<option value="EMBA">EMBA</option>
				<option value="博士研究生">博士研究生</option>
				<option value="硕士研究生">硕士研究生</option>
				<option value="大学本科">大学本科</option>
				<option value="专科">专科</option>
				<option value="中专">中专</option>
				<option value="职高">职高</option>
				<option value="技校">技校</option>
				<option value="高中">高中</option>
				<option value="初中">初中</option>
				<option value="小学">小学</option>
				<option value="其他">其他</option>
		 </select>
	  </td>
	  <td>
	       <input class="date_class"  type="text"  id="employmentStr" name="employmentStr" readonly  style="float: left;width: 70%; height:30px">
		    <span class="input-group form_datec date col-md-5 input-group-addon" data-date="" data-date-format="yyyy/mm/dd" data-link-field="employmentStr" data-link-format="yyyy/mm/dd"  id="glyphicon-planComeDate-calendar" style="margin: 0;width: 30px; height:30px;background-color: #F0F0F0; float: right" ><span class="glyphicon glyphicon-calendar"></span></span>
	  </td>
	  <td>   
	     <input type="text" style="width: 100%" name="probationLimit" id="probationLimit" placeholder="输入试用期限">
	  </td>
	   <td>   
	      <input class="date_class"  type="text"  id="becomeStaffDate" name="becomeStaffDate" readonly  style="float: left;width: 70%; height:30px">
		   <span class="input-group form_datec date col-md-5 input-group-addon" data-date="" data-date-format="yyyy/mm/dd" data-link-field="becomeStaffDate" data-link-format="yyyy/mm/dd"  id="glyphicon-planComeDate-calendar" style="margin: 0;width: 30px; height:30px;background-color: #F0F0F0; float: right" ><span class="glyphicon glyphicon-calendar"></span></span>
	  </td>
	  </tr>
	  
	  <tr class="trcenter">
	  <td class="trsolid">部门主管</td>
	  <td><input type="text" name="orgChargeName" id="orgChargeName"  style="width: 100%"/></td>
	  <td class="trsolid">审批结果</td>
	  <td><input type="text" name="orgChargeResult" id="orgChargeResult"  style="width: 100%"/></td>
	  </tr>
	  
	  <tr class="trsolid trleft">
	  <td colspan="4">
	      理由:
	  </td>
	  </tr>
	  
	  <tr>
	  <td colspan="4">
	   <textarea  name="orgChargeRemark" id="orgChargeRemark" rows="6" style="width: 100%"></textarea>
	  </td>
	  </tr>
	  
	  <tr class="trcenter">
	  <td class="trsolid">部门负责人</td>
	  <td><input type="text" name="orgHeadName" id="orgHeadName"  style="width: 100%"/></td>
	  <td class="trsolid">审批结果</td>
	  <td><input type="text" name="orgHeadResult" id="orgHeadResult"  style="width: 100%"/></td>
	  </tr>
	  
	  <tr class="trsolid trleft">
	  <td colspan="4">
	      理由:
	  </td>
	  </tr>
	  
	  <tr>
	  <td colspan="4">
	   <textarea  name="orgHeadRemark" id="orgHeadRemark" rows="6" style="width: 100%"></textarea>
	  </td>
	  </tr>
	  
	</table>
	</div>