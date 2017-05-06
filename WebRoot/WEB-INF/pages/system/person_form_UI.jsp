<%@page import="com.rosense.basic.util.LangUtils"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script src="${webRoot}/template/resource/js/city.js"></script>

<style>
<!--
.roleName{
height:13px;vertical-align:text-top;margin-top:0;width:30px;
}

.modal-dialog{
 width:45%;
 height:85%;
 text-align: center;

}
 .employ {
	border:5px;
	width:700px;
	height:1150px;
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
	var form_url = $.webapp.root + "/admin/system/user/add.do";
	var parent_box, person_type;
	var flag = true;//防止重复提交
	var flagphoto=false;
	var flagget=true;
    $(function() {
		//编辑，加载表单数据
		var orgtree = $.webapp.root + "/admin/system/org/tree.do";
		var ptree = $.webapp.root + "/admin/system/position/tree.do";
		var get = $.webapp.root + "/admin/system/user/get.do";
			/* region_init("province","area","city"); */
		 $.get($.webapp.root + "/admin/system/role/datagrid.do",
					{},function(data){
						
						for (var i = 0; i < data.total; i++) {
							$("td#role").append('<input id="'+data.rows[i].name+'" type="checkbox" value="'+data.rows[i].id+'" class="roleName" onclick="role()"><label for="'+data.rows[i].name+'">'+data.rows[i].name+'</label>')
						}
					},"json")
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
				
						$.post(get, {
							id : $('input[name=id]').val()
						}, function(result) {
						   if(flagget){
								var extentree = $.webapp.root + "/admin/system/exten/gettree.do?locateid="+result.locateid;
								/* $.BOOT.autoselectexten("callerid", extentree, {
									title : "选择分机号"
								}); */
								 flagget=-false;
							}
							form_url = $.webapp.root+ "/admin/system/user/update.do";
							$('#form_addusers').form('load', result);
							//获取员工信息时，勾选已有角色
							var ids = $('input[name=role_ids]').val();
							var id = new Array();
							id = ids.split(",");
							for (var i = 0; i < id.length; i++) {
								$('input[value='+id[i]+']').attr("checked",'true')
							}
						}, 'json');
					
				} else {
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
    //根据副部门筛选对应子部门
    function orgChange(){
    	 $("#orgChildId").html("");
    	  var selectId = $('#orgId>option:selected');
          selectId.html(function(){
        	  var orgchildtree = $.webapp.root + '/admin/system/org/tree.do?pid='+this.value;
    		  $.BOOT.autoselect("orgChildId", orgchildtree, {
    	 			title : "选择子部门"
    	 	}); 
      })
    }
    
    function extenChange(){
    	var objS = document.getElementById("locateid");
    	var objtree = document.getElementById("callerid");
        objtree.options.length = 0; 
        var locateid = objS.options[objS.selectedIndex].value;
        var extentree = $.webapp.root + "/admin/system/exten/tree.do?locateid="+locateid;
		$.BOOT.autoselectexten("callerid", extentree, {
			title : "选择分机号"
		});   
    }
    
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
	
	 $.BOOT.form("form_addusers", {
		 account : {
			validators : {
				notEmpty : {
					message : '账号不能为空'
				}
			}
		},
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
		/* employmentStr : {
			validators : {
				notEmpty : {
					message : '入职时间不能为空'
				}
			}
		}, */
 }, function(params) {
	    
			 if(flag){
			$.post(form_url, params, function(result) {
				 $.BOOT.toast1(result); 
				 $person_table.bootstrapTable('refresh'); 
				modaladdusers();
			}, 'json');
			flag = false;
		 } 
	}); 
	 //选中角色时生成隐藏的ids
	 function role(){
		 var temp = "";
		 $("input.roleName:checked").each(function(){
	 			temp += $(this).val()+",";
	 		})
	 	$("input[name=role_ids]").val(temp);
	 }
	
	
   /*  $("#glyphicon-employmentStr-remove").click(function(){
    	$("#employmentStr").val("");
    	$("#employmentStrReg").html("入职时间不能为空");
    	$("#save_addusers").attr("disabled", true);
    });
    $("#glyphicon-employmentStr-calendar").click(function(){
    	  $("#datereg").html("");
    	  $("#save_addusers").removeAttr("disabled"); 
    });
    
    $("#glyphicon-becomeStaffDate-remove").click(function(){
    	$("#becomeStaffDate").val("");
    	$("#becomeStaffDateReg").html("转正时间不能为空");
    	$("#save_addusers").attr("disabled", true);
    });
    $("#glyphicon-becomeStaffDate-calendar").click(function(){
  	  $("#becomeStaffDateReg").html("");
  	  $("#save_addusers").removeAttr("disabled"); 
    });
    
    $("#glyphicon-agreementStartDate-remove").click(function(){
    	  $("#agreementStartDate").val(""); 
      });
    
    $("#glyphicon-agreementEndDate-remove").click(function(){
    	$("#agreementEndDate").val("");
    });
    $("#glyphicon-birthday-remove").click(function(){
    	$("#birthday").val("");
    	$("#birthdayReg").html("出生日期不能为空");
    	$("#save_addusers").attr("disabled", true);
    });
    $("#glyphicon-birthday-calendar").click(function(){
    	  $("#birthdayReg").html("");
    	  $("#save_addusers").removeAttr("disabled"); 
    });
    
    $("#glyphicon-securityDate-remove").click(function(){
    	$("#securityDate").val("");
    });
    
    $("#glyphicon-fundDate-remove").click(function(){
    	$("#fundDate").val("");
    });
    
    $("#glyphicon-probationEnd-remove").click(function(){
    	$("#probationEnd").val("");
    });
    $("#glyphicon-leaveDate-remove").click(function(){
    	$("#leaveDate").val("");
    });
    $("#glyphicon-graduation-remove").click(function(){
    	$("#graduation").val("");
    }); */
</script>

	
<div class="row">
	<input type="hidden" name="id" value="${id}"/>
	<table class="employ">
	  <tr class="trsolid trcenter form-group">
	  <td>账号</td>
	  <td>中文名</td>
	  <td>英文名</td>
	  <td>手机<span id="phonereg" style="color: red; font-size: 12px;  display：none;"></span></td>
	  </tr>
	  
	  <tr class="trcenter form-group">
	  <td><input type="text" name="account" id="account" style="width: 100%" placeholder="输入账号"/></td>
	  <td><input type="text" name="chinaname" id="chinaname" style="width: 100%" placeholder="输入中文名"/></td>
	  <td><input type="text" name="name" id="name" placeholder="输入英文名" style="width: 100%"/></td>
	  <td><input type="text" name="phone" id="phone" placeholder="输入手机" style="width: 100%"/></td>
	  </tr>
	  
	  <tr class="trsolid trcenter form-group">
	  <td>性别</td>
	  <td>年龄</td>
	  <td>地区</td>
	  <td>是否在职</td>
	  </tr>
	  
	  <tr class="trcenter form-group">
	  <td>
		     <select id="sex" name="sex" style="width: 100%;text-align: center">
				<option value="男" >男</option>
				<option value="女">女</option>
			 </select>
		
	  </td>
	  <td>
	     	<input type="text" name="age" id="age" placeholder="输入年龄" style="width: 100%"/>
	  </td>
	  <td>
			<!-- <select name="province" id="province"  style="width: 45%"></select> 
	        <select name="area" id="area"  style="width: 45%"></select> 
	        <select id="city" name="city" style="display: none"></select> -->
	        <select class="form-control" id="area" name="area" style="width: 100%">
			<option value="GZ">GZ</option>
			<option value="SH">SH</option>
			<option value="BJ">BJ</option>
			<option value="XG">XG</option>
		  </select>
	  </td>
	  <td>
	      <select class="form-control" id="job" name="job" style="width: 100%">
			<option value="在职">在职</option>
			<option value="离职">离职</option>
			<option value="停薪留职">停薪离职</option>
		  </select>
	  </td>
	  </tr>
	 
	  <tr class="trsolid trcenter form-group">
	  <td>部门</td>
	  <td>部门2</td>
	  <td>职位</td>
	  <td>职位英文</td>
	  </tr>
	  
	  <tr class="trcenter form-group">
	  <td> <select id="orgId" name="orgId" style="width: 100%;height: 100%" onchange="orgChange()"> </select></td>
	  <td><select id="orgChildId" name="orgChildId"style="width: 100%;height: 100%"> </select></td>
	  <td><select  id="positionId" name="positionId"style="width: 100%;height: 100%"></select></td>
	  <td><input type="text"  name="positionEng" id="positionEng" placeholder="输入职位英文" style="width: 100%;display: none;"/></td>
	  </tr> 
	 
	  <tr class="trsolid trcenter form-group">
	  <td colspan="4">角色</td>
	  </tr>
	  
	  <tr>
	  <td colspan="4" id="role">
		<input type="hidden" name="role_ids">
	  </td>
	  </tr> 
	 
	 
	  <tr class="trsolid trcenter form-group">
	  <td>级别</td>
	  <td>邮箱</td>
	  <td>学历<span id="degreeReg" style="color: red; font-size: 12px;  display：none;"></span></td>
	  <td>出生年月</td>
	  </tr>
	  
	  <tr class="trcenter form-group">
	  <td>
	      <select class="form-control" id="grade" name="grade" style="width: 100%">
				<option value="STAFF">STAFF</option>
				<option value="MGT">MGT</option>
		  </select>
	  </td>
	  <td><input type="email" name="email" id="email" placeholder="输入邮箱" style="width: 100%" /></td>
	  <td>
	       <select  id="degree" name="degree" style="width: 100%;height: 100%">
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
	        <input class="date_class"  type="text"  id="birthday" name="birthday" readonly  style="float: left;width: 70%; height:30px">
		    <span class="input-group form_date date col-md-5 input-group-addon" data-date="" data-date-format="yyyy/mm/dd" data-link-field="birthday" data-link-format="yyyy/mm/dd"  id="glyphicon-planComeDate-calendar" style="margin: 0;width: 30px; height:30px;background-color: #F0F0F0; float: right" ><span class="glyphicon glyphicon-calendar"></span></span>
	  </td>
	  </tr>
	  
	  
	 <!--  <tr class="trcenter form-group">
	  <td colspan="2" class="trsolid">座机分号</td>
	  <td colspan="2"> 
	      <select class="form-control" id="locateid" name="locateid" style="width: 49%;float: left" onchange="extenChange()">
			<option value="020">广州</option>
			<option value="021">上海</option>
			<option value="010">北京</option>
			<option value="00852">香港</option>
		  </select>
		   <select class="form-control" id="callerid" name="callerid" style="width: 49%;float: left"> </select>
	   </td>
	  </tr> -->
	  
	  <tr class="trsolid trcenter form-group">
	  <td>社保日期</td>
	  <td>公积金</td>
	  <td>入职日期<span id="degreeReg" style="color: red; font-size: 12px;  display：none;"></span></td>
	  <td>试用期限</td>
	  </tr>
	   
	  <tr class="trcenter form-group">
	  <td> 
	        <input class="date_class"  type="text"  id="securityDate" name="securityDate" readonly  style="float: left;width: 70%; height:30px">
		    <span class="input-group form_date date col-md-5 input-group-addon" data-date="" data-date-format="yyyy/mm/dd" data-link-field="securityDate" data-link-format="yyyy/mm/dd"  id="glyphicon-planComeDate-calendar" style="margin: 0;width: 30px; height:30px;background-color: #F0F0F0; float: right" ><span class="glyphicon glyphicon-calendar"></span></span>
      </td>
	  <td>
	        <input class="date_class"  type="text"  id="fundDate" name="fundDate" readonly  style="float: left;width: 70%; height:30px">
		    <span class="input-group form_date date col-md-5 input-group-addon" data-date="" data-date-format="yyyy/mm/dd" data-link-field="fundDate" data-link-format="yyyy/mm/dd"  id="glyphicon-planComeDate-calendar" style="margin: 0;width: 30px; height:30px;background-color: #F0F0F0; float: right" ><span class="glyphicon glyphicon-calendar"></span></span>
      </td>
	  <td>
	       <input class="date_class"  type="text"  id="employmentStr" name="employmentStr" readonly  style="float: left;width: 70%; height:30px">
		    <span class="input-group form_date date col-md-5 input-group-addon" data-date="" data-date-format="yyyy/mm/dd" data-link-field="employmentStr" data-link-format="yyyy/mm/dd"  id="glyphicon-planComeDate-calendar" style="margin: 0;width: 30px; height:30px;background-color: #F0F0F0; float: right" ><span class="glyphicon glyphicon-calendar"></span></span>
	  </td>
	  <td>   
	     <input type="text" style="width: 100%" name="probationLimit" id="probationLimit" placeholder="输入试用期限">
	  </td>
	  </tr>
	  
	  <tr class="trsolid trcenter form-group">
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
	  
	  <!-- <tr class="form-group">
	      <td colspan="4">
	       <textarea rows="3" name="address" id="address" placeholder="输入现住址" style="width: 100%"></textarea>
	     </td>
	  </tr> -->
	  
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
		   <!-- <input type="text" name="workOld" id="workOld" placeholder="输入工作经历" style="width: 100%"/> -->
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
	  </tr>
	  
	</table>

	</div>