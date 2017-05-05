<%@page import="com.rosense.basic.util.LangUtils"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script src="${webRoot}/template/resource/js/city.js"></script>

<style>
<!--
.roleName{
height:13px;vertical-align:text-top;margin-top:0;width:30px;
}

 .modal-dialog{
 width:60%;
 height:70%;
 text-align: center;

} 
.employ {
	border:5px;
	width:610px;
	height:600px;
	text-align: center;
	margin-left: 23px;
} 
.employ tr td{
  border:1px solid black;
}
.trsolid{
    font-weight: bold;
    vertical-align: 
    border
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
-->
</style>
<script>
	var form_url = $.webapp.root + "/admin/system/teacher/add.do";
	var parent_box, person_type;
	var flag = true;//防止重复提交
	var flagphoto=false;
	var flagget=true;
    $(function() {
		//编辑，加载表单数据
		var orgtree = $.webapp.root + "/admin/system/org/tree.do";
		var ptree = $.webapp.root + "/admin/system/position/tree.do";
		var get = $.webapp.root + "/admin/system/teacher/get.do";
		 region_init("province","city","area"); 
		 $.get($.webapp.root + "/admin/system/role/datagrid.do",
					{},function(data){
						for (var i = 0; i < data.total; i++) {
							$("td#role").append('<input id="'+data.rows[i].name+'" type="checkbox" value="'+data.rows[i].id+'" class="roleName" onclick="role()"><label for="'+data.rows[i].name+'">'+data.rows[i].name+'</label>')
						}
					},"json")
	   /*  $.BOOT.autoselect("orgId", orgtree, {
			title : "选择部门"
		}); 
		 $.BOOT.autoselect("orgChildId", orgtree, {
			title : "选择子部门"
		});  */
		/* $.BOOT.autoselect("positionId", ptree, {
			title : "选择职位",
			callback : function() { */
				if ($('input[name=id]').val().length > 0) {
				
						$.post(get, {
							id : $('input[name=id]').val()
						}, function(result) {
							form_url = $.webapp.root+ "/admin/system/teacher/update.do";
							$('#form_addTea').form('load', result);
							//获取员工信息时，勾选已有角色
							/* var ids = $('input[name=role_ids]').val();
							var id = new Array();
							id = ids.split(",");
							for (var i = 0; i < id.length; i++) {
								$('input[value='+id[i]+']').attr("checked",'true')
							} */
						}, 'json');
					
				} else {
				}
		/* 	}
		});  */ 
		
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
		  $("#phonereg").show();
		  var flag=false;
	      var message = "";
	      var myreg=/^1[3|4|5|8]\d{9}$/;
		  phone = this.value;
		  if(!myreg.test(phone)){
		        message = "请输入有效的手机号码！";
		   }else{
	    	  $("#phonereg").hide();
	    	  $("#save_addusers").removeAttr("disabled"); 
	    	  flag=true;
	      }
	      if(!flag){
	     //提示错误效果
	        $("#phonereg").html(message);
	        $("#save_addusers").attr("disabled", true);
	      }
	      
	});  
	
	 $.BOOT.form("form_addTea", {
		 tea_no : {
			validators : {
				notEmpty : {
					message : '编号不能为空'
				}
			}
		},
		tea_name : {
			validators : {
				notEmpty : {
					message : '姓名不能为空'
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
				 $teacher_table.bootstrapTable('refresh'); 
				modaladdTea();
			}, 'json');
			flag = false;
		 } 
	}); 
	 //选中角色时生成隐藏的ids
	/*  function role(){
		 var temp = "";
		 $("input.roleName:checked").each(function(){
	 			temp += $(this).val()+",";
	 		})
	 	$("input[name=role_ids]").val(temp);
	 } */
	
</script>

	
<div class="row">
	<input type="hidden" name="id" value="${id}"/>
	<table class="employ">
	  <tr class="trsolid trcenter form-group">
	  <td>编号</td>
	  <td>姓名</td>
	  <td>性别</td>
	  <td>入职日期</td>
	  </tr>
	  
	  <tr class="trcenter form-group">
	  <td><input type="text" name="tea_no" id="tea_no" style="width: 100%" placeholder="输入编号"/></td>
	  <td><input type="text" name="tea_name" id="tea_name" placeholder="输入姓名" style="width: 100%"/></td>
	  <td><select id="sex" name="sex" style="width: 100%;text-align: center">
				<option value="男" >男</option>
				<option value="女">女</option>
			 </select>
	  </td>
	   <td><input class="date_class"  type="text"  id="entrance_date_Str" name="entrance_date_Str" readonly  style="float: left;width: 70%; height:30px">
		   <span class="input-group form_date date col-md-5 input-group-addon" data-date="" data-date-format="yyyy/mm/dd" data-link-field="entrance_date_Str" data-link-format="yyyy/mm/dd"  id="glyphicon-planComeDate-calendar" style="margin: 0;width: 30px; height:30px;background-color: #F0F0F0; float: right" ><span class="glyphicon glyphicon-calendar"></span></span>
       </td>
	  </tr>
	  
	  <tr class="trsolid trcenter form-group">
	  <td>联系电话手机<span id="phonereg" style="color: red; font-size: 12px;  display：none;"></span></td>
	  <td>短号</td>
	  <td>邮箱</td>
	  <td>出生年月</td>
	  </tr>
	  
	  <tr class="trcenter form-group">
	  <td><input type="text" name="phone" id="phone" placeholder="输入手机" style="width: 100%"/></td>
	   <td>
	     <input type="text" name="cornet" id="cornet" placeholder="输入短号" style="width: 100%"/>
	      <!-- <select class="form-control" id="grade" name="grade" style="width: 100%">
			<option value="学生">学生</option>
			<option value="教师">教师</option>
		  </select> -->
	  </td>
	  <td>
	     	<input type="text" name="email" id="email" placeholder="输入邮箱" style="width: 100%"/>
	  </td>
	  <td>
			<input class="date_class"  type="text"  id="birthday" name="birthday" readonly  style="float: left;width: 70%; height:30px">
		   <span class="input-group form_date date col-md-5 input-group-addon" data-date="" data-date-format="yyyy/mm/dd" data-link-field="birthday" data-link-format="yyyy/mm/dd"  id="glyphicon-planComeDate-calendar" style="margin: 0;width: 30px; height:30px;background-color: #F0F0F0; float: right" ><span class="glyphicon glyphicon-calendar"></span></span>
	  </td>
	 
	  </tr>
	 
	  <tr class="trsolid trcenter form-group">
	  <td colspan="2">地区</td>
	  <td colspan="2">民族</td>
	  </tr>
	  
	  <tr class="trcenter form-group">
	  <td colspan="2"><select name="province" id="province"  style="width: 45%"></select> 
	        <select name="city" id="city"  style="width: 45%"></select> 
	        <select id="area" name="area" style="display: none"></select></td>
	  <td colspan="2"><input type="text"  name="nation" id="nation" placeholder="输入民族" style="width: 100%;"/></td>
	  </tr> 
	 
	 <!--  <tr class="trsolid trcenter form-group">
	  <td colspan="4">角色</td>
	  </tr>
	  
	  <tr>
	  <td colspan="4" id="role">
		<input type="hidden" name="role_ids">
	  </td>
	  </tr>  -->
	 
	 
	  <tr class="trsolid trcenter form-group">
	  <td>政治面貌</td>
	  <td>贯籍</td>
	  <td>户籍地址<span id="degreeReg" style="color: red; font-size: 12px;  display：none;"></span></td>
	  <td>户口性质</td>
	  </tr>
	  
	  <tr class="trcenter form-group">
	  <td>
	      <select id="politicalFace" name="politicalFace" style="width: 100%;height: 100%">
			  <option value="共青团员">共青团员</option>
			  <option value="党员">党员</option>
			  <option value="群众">群众</option>
		  </select>
	  </td>
	  <td><input type="text" name="origin" id="origin" placeholder="贯籍" style="width: 100%" /></td>
	  <td><input type="text"  name="accountAddr" id="accountAddr" placeholder="户口地址" style="width: 100%;"/></td>
	 <td>
	     	<input type="text" name="accountPro" id="accountPro" placeholder="输入户口性质" style="width: 100%"/>
	  </td>
	  </tr>
	  

	  <tr class="trsolid trcenter form-group">
	   <td>身份证号码</td>
	  <td>联系人</td>
	  <td>联系人电话<span id="degreeReg" style="color: red; font-size: 12px;  display：none;"></span></td>
	   <td>银行卡号</td>
	  </tr>
	   
	  <tr class="trcenter form-group">
	   <td><input type="text"  name="idcard" id="idcard" placeholder="身份证号码" style="width: 100%;"/></td>
	  <td> <input type="text" style="width: 100%" name="contact" id="contact" placeholder="联系人"></td>
	  <td><input type="text" style="width: 100%" name="contactPhone" id="contactPhone" placeholder="联系人电话"></td>
	   <td>   
	     <input type="text" style="width: 100%" name="bankCard" id="bankCard" placeholder="银行卡">
	  </td>
	  </tr>
	  
	   <tr class="trsolid trcenter form-group">
	  <td colspan="4">个人资料</td>
	  </tr>
	   
	  <tr class="trcenter form-group">
	  <td colspan="4">   
	  <textarea rows="5" name="material" id="material" placeholder="资料" style="width: 100%"></textarea>
	  </td>
	  </tr>
	  
	 <!--  <tr class="trsolid trcenter form-group">
	  <td>毕业学校</td>
	  <td>毕业时间<span id="graduationReg" style="color: red; font-size: 12px;  display：none;"></span></td>
	  <td>试用到期</td>
	  <td>转正日期</td>
	  </tr>
	  
	  <tr class="trcenter form-group">
	  <td> 
	       <input type="text" style="width: 100%"  name="school" id="school" placeholder="输入毕业学校">
	  </td>
	  <td>
	        <input class="date_class"  type="text"  id="graduation" name="graduation" readonly  style="float: left;width: 70%; height:30px">
		    <span class="input-group form_date date col-md-5 input-group-addon" data-date="" data-date-format="yyyy/mm/dd" data-link-field="graduation" data-link-format="yyyy/mm/dd"  id="glyphicon-planComeDate-calendar" style="margin: 0;width: 30px; height:30px;background-color: #F0F0F0; float: right" ><span class="glyphicon glyphicon-calendar"></span></span>
	 </td>
	  <td>
	       <input class="date_class"  type="text"  id="probationEnd" name="probationEnd" readonly  style="float: left;width: 70%; height:30px">
		   <span class="input-group form_date date col-md-5 input-group-addon" data-date="" data-date-format="yyyy/mm/dd" data-link-field="probationEnd" data-link-format="yyyy/mm/dd"  id="glyphicon-planComeDate-calendar" style="margin: 0;width: 30px; height:30px;background-color: #F0F0F0; float: right" ><span class="glyphicon glyphicon-calendar"></span></span>
	  </td>
	  <td>   
	      <input class="date_class"  type="text"  id="becomeStaffDate" name="becomeStaffDate" readonly  style="float: left;width: 70%; height:30px">
		   <span class="input-group form_date date col-md-5 input-group-addon" data-date="" data-date-format="yyyy/mm/dd" data-link-field="becomeStaffDate" data-link-format="yyyy/mm/dd"  id="glyphicon-planComeDate-calendar" style="margin: 0;width: 30px; height:30px;background-color: #F0F0F0; float: right" ><span class="glyphicon glyphicon-calendar"></span></span>
	  </td>
	  </tr>
	  
	  <tr class="trsolid trcenter form-group">
	  <td>合同到期</td>
	  <td>合同开始计算日期<span id="graduationReg" style="color: red; font-size: 12px;  display：none;"></span></td>
	  <td>离职日期</td>
	  <td>合同期限</td>
	  </tr>
	  
	  <tr class="trcenter form-group">
	  <td> 
	        <input class="date_class"  type="text"  id="agreementEndDate" name="agreementEndDate" readonly  style="float: left;width: 70%; height:30px">
		    <span class="input-group form_date date col-md-5 input-group-addon" data-date="" data-date-format="yyyy/mm/dd" data-link-field="agreementEndDate" data-link-format="yyyy/mm/dd"  id="glyphicon-planComeDate-calendar" style="margin: 0;width: 30px; height:30px;background-color: #F0F0F0; float: right" ><span class="glyphicon glyphicon-calendar"></span></span>
	 </td>
	  <td>
	        <input class="date_class"  type="text"  id="agreementStartDate" name="agreementStartDate" readonly  style="float: left;width: 70%; height:30px">
		    <span class="input-group form_date date col-md-5 input-group-addon" data-date="" data-date-format="yyyy/mm/dd" data-link-field="agreementStartDate" data-link-format="yyyy/mm/dd"  id="glyphicon-planComeDate-calendar" style="margin: 0;width: 30px; height:30px;background-color: #F0F0F0; float: right" ><span class="glyphicon glyphicon-calendar"></span></span>
	 </td>
	  <td>
	       <input class="date_class"  type="text"  id="leaveDate" name="leaveDate" readonly  style="float: left;width: 70%; height:30px">
		   <span class="input-group form_date date col-md-5 input-group-addon" data-date="" data-date-format="yyyy/mm/dd" data-link-field="leaveDate" data-link-format="yyyy/mm/dd"  id="glyphicon-planComeDate-calendar" style="margin: 0;width: 30px; height:30px;background-color: #F0F0F0; float: right" ><span class="glyphicon glyphicon-calendar"></span></span>
	  </td>
	  <td>   
	        <input type="text" style="width: 100%"  name="agreementLimit" id="agreementLimit" placeholder="输入合同期限">
	   </td>
	  </tr>
	  
	   <tr class="trsolid trcenter form-group">
	  <td>合同签订次数</td>
	  <td>婚姻状况</td>
	  <td>生育状况</td>
	  <td>民族</td>
	  </tr>
	  
	  <tr class="trcenter form-group">
	  <td>
			<input type="text" name="agreementTimes" id="agreementTimes" placeholder="输入合同签订次数" style="width: 100%"/>
	 </td>
	  <td>
	       <select id="marriage" name="marriage" style="width: 100%;height: 100%">
			  <option value="已婚">已婚</option>
			  <option value="未婚">未婚</option>
			  <option value="再婚">再婚</option>
			  <option value="复婚">复婚</option>
		   </select>
	  </td>
	  <td>
		   <select id="bear" name="bear" style="width: 100%;height: 100%">
				<option value="已育">已育</option>
			    <option value="未育">未育</option>
		   </select>
	  </td>
	  <td>
	     	<input type="text" name="nation" id="nation" placeholder="输入民族" style="width: 100%"/>
	  </td>
	  </tr>
	  
	  <tr class="trsolid trcenter form-group">
	  <td>政治面面貌</td>
	  <td>籍贯</td>
	  <td>户籍地址</td>
	  <td>户口性质</td>
	  </tr>
	  
	  <tr class="trcenter form-group">
	  <td>
		   <select id="politicalFace" name="politicalFace" style="width: 100%;height: 100%">
			  <option value="共青团员">共青团员</option>
			  <option value="党员">党员</option>
			  <option value="群众">群众</option>
		   </select>
	 </td>
	  <td>
	       <input type="text" name="origin" id="origin" placeholder="输入贯籍" style="width: 100%"/>
	  </td>
	  <td>
		   <input type="text" name="accountAddr" id="accountAddr" placeholder="输入户籍地址" style="width: 100%"/>
	  </td>
	  <td>
	     	<input type="text" name="accountPro" id="accountPro" placeholder="输入户口性质" style="width: 100%"/>
	  </td>
	  </tr>
	  
	  
	  <tr class="form-group">
	      <td class=" trsolid">现住址</td>
	      <td  colspan="3">
	     	<input type="text" name="address" id="address" placeholder="输入现住址" style="width: 100%"/>
	     </td>
	  </tr>
	  
	  <tr class="form-group">
	      <td colspan="4">
	       <textarea rows="3" name="address" id="address" placeholder="输入现住址" style="width: 100%"></textarea>
	     </td>
	  </tr>
	  
	  <tr class="trsolid trcenter form-group">
	  <td colspan="2">身份证号码</td>
	  <td>专业</td>
	  <td>证书</td>
	  </tr>
	  
	  <tr class="trcenter form-group">
	  <td colspan="2">
	       <input type="text" name="idcard" id="idcard" placeholder="输入身份证号码" style="width: 100%"/>
	  </td>
	  <td>
		   <input type="text" name="profession" id="profession" placeholder="输入专业" style="width: 100%"/>
	  </td>
	  <td>
	     	<input type="text" name="certificate" id="certificate" placeholder="输入证书" style="width: 100%"/>
	  </td>
	  </tr>
	  
	  <tr class="trsolid trcenter form-group">
	  <td  colspan="2">联系人</td>
	  <td  colspan="2">联系人电话</td>
	  </tr>
	  
	  <tr class="trcenter form-group">
	  <td  colspan="2">
		   <input type="text" name="contact" id="contact" placeholder="输入联系人" style="width: 100%"/>
	 </td>
	  <td  colspan="2">
	       <input type="text" name="contactPhone" id="contactPhone" placeholder="输入联系人电话" style="width: 100%"/>
	  </td>
	  </tr>
	  
	  <tr class="form-group trsolid">
	      <td colspan="4">资料</td>
	  </tr>
	  
	  <tr class="form-group">
	      <td colspan="4">
	       <textarea rows="5" name="material" id="material" placeholder="输入资料" style="width: 100%"></textarea>
	     </td>
	  </tr>
	  
	  <tr class="form-group trsolid">
	      <td colspan="4">工作经历</td>
	  </tr>
	  
	  <tr class="form-group">
	      <td colspan="4">
	       <textarea rows="5" name="workOld" id="workOld" placeholder="输入工作经历" style="width: 100%"></textarea>
		   <input type="text" name="workOld" id="workOld" placeholder="输入工作经历" style="width: 100%"/>
	     </td>
	  </tr>
	  
	   <tr class="trsolid trcenter form-group">
	  <td>银行卡</td>
	  <td>培训经历</td>
	  <td>社保卡</td>
	  <td>公积金薄</td>
	  </tr>
	  
	  <tr class="trcenter form-group">
	  <td>
		   <input type="text" name="bankCard" id="bankCard" placeholder="输入银行卡" style="width: 100%"/>
	 </td>
	  <td>
	       <input type="text" name="train" id="train" placeholder="输入培训经历 " style="width: 100%"/>
	  </td>
	  <td>
		   <input type="text" name="securityCard" id="securityCard" placeholder="输入社保卡" style="width: 100%"/>
	  </td>
	  <td>
	     	<input type="text" name="fund" id="fund" placeholder="输入公积金薄" style="width: 100%"/>
	  </td>
	  </tr> -->
	  
	</table>

	</div>