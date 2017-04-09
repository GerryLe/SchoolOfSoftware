<%@page import="com.rosense.basic.util.LangUtils"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script src="${webRoot}/template/resource/js/city.js"></script>
<style>
<!--
.modal-dialog{
 width:40%;
 height:80%;
 text-align: center;

}
 .employ {
	border:5px;
	width:600px;
	height:550px;
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
	 var form_url = $.webapp.root + "/admin/system/employ/add.do";
	var employ_box, employ_type;
	var flag = true;//防止重复提交
	$(function() {
		//编辑，加载表单数据
		        var get = $.webapp.root + "/admin/system/employ/get.do";
				if ($('input[name=id]').val().length > 0) {
					$.post(get, {
						id : $('input[name=id]').val()
					}, function(result) {
						form_url = $.webapp.root+ "/admin/system/employ/update.do";
						if(result.applyName!=result.userName){
							$("input,textarea").attr("disabled", true);
							$("#save_addEmploy").hide();
						}
						else{
							
						}
						$('#form_addEmploy').form('load', result);
					}, 'json');
				} else {
				} 
				$("#save_addEmploy").show();
				
				$('.form_date2').datetimepicker({
			        language:  'zh-CN',
			        weekStart: 1,
			        todayBtn:  1,
					autoclose: 1,
					todayHighlight: 1,
					startView: 2,
					minView: 2,
					pickerPosition:'bottom-left',
					forceParse: 0
			    });
				$('.form_date1').datetimepicker({
			        language:  'zh-CN',
			        weekStart: 1,
			        todayBtn:  1,
					autoclose: 1,
					todayHighlight: 1,
					startView: 2,
					minView: 2,
					pickerPosition:'top-right',
					forceParse: 0
			    });
	});
      $.BOOT.form("form_addEmploy", {}, function(params) {
			if(flag){
			$.post(form_url, params, function(result) {
				$.BOOT.toast1(result);
				$employ_table.bootstrapTable('refresh');
				modaladdEmploy();
			}, 'json');
			flag = false;
		 }
	}); 
      
</script>
<div class="row">
	<input type="hidden" name="id" value="${id}">
	<label style="margin-left: 250px;font-size: 18px;">招聘申请表</label>
	<table class="employ">
	  <tr class="trsolid trcenter">
	  <td>申请人</td>
	  <td>申请地点、部门</td>
	  <td>申请对外职位</td>
	  <td>申请公司内部级别</td>
	  </tr>
	  
	  <tr class="trcenter">
	  <td><input type="text" name="applyName" id="applyName" style="width: 100%"/></td>
	  <td><input type="text" name="applyAreaAndorg" style="width: 100%"/></td>
	  <td><input type="text" name="applyPosition" style="width: 100%"/></td>
	  <td><input type="text" name="applyGenre" style="width: 100%"/></td>
	  </tr>
	  
	  <tr class="trsolid trcenter">
	  <td>申请薪金</td>
	  <td>申请补贴</td>
	  <td>申请人数</td>
	  <td>预计入职日期</td>
	  </tr>
	  
	  <tr class="trcenter">
	  <td><input type="text" name="applyPayment" style="width: 100%"/></td>
	  <td><input type="text" name="applySubsidy" style="width: 100%"/></td>
	  <td><input type="text" name="applyNumber" style="width: 100%"/></td>
	  <td>
	      <input class="date_class"  type="text"  id="planComeDate" name="planComeDate" readonly  style="float: left;width: 70%; height:30px">
		     <span class="input-group form_date2 date col-md-5 input-group-addon" data-date="" data-date-format="yyyy/mm/dd" data-link-field="planComeDate" data-link-format="yyyy/mm/dd"  id="glyphicon-planComeDate-calendar" style="margin: 0;width: 30px; height:30px;background-color: #F0F0F0; float: right" ><span class="glyphicon glyphicon-calendar"></span></span>
	  </td>
	  </tr>
	  
	  <tr class="trsolid trleft">
	  <td colspan="4">
	     申请预算:&nbsp;&nbsp;&nbsp;<input type="radio" name="applyBudget">预算内 &nbsp;&nbsp;&nbsp;  <input type="radio" name="budget">预算外 &nbsp;&nbsp;&nbsp;<input type="radio" name="budget">其他&nbsp;&nbsp;&nbsp;<input type="radio" name="budget">超预算&nbsp;&nbsp;(<input name="extbudget" style="width: 30px;"/>)&nbsp;&nbsp;% 
	  </td>
	  </tr>
	  
	  <tr class="trsolid trleft">
	  <td colspan="4">
	     申请理由:
	  </td>
	  </tr>
	  
	  <tr>
	  <td colspan="4">
	   <textarea  name="reason" rows="10" style="width: 100%"></textarea>
	  </td>
	  </tr>
	  
	  <tr class="trsolid trleft">
	  <td colspan="4">
	     增补人员主要负责工作:
	  </td>
	  </tr>
	  
	  <tr>
	  <td colspan="4">
	   <textarea  name="duty" rows="10" style="width: 100%"></textarea>
	  </td>
	  </tr>
	  
	  <tr class="trsolid trleft">
	  <td colspan="4">
	     任职资格:
	  </td>
	  </tr>
	  
	  <tr class="trleft">
	  <td colspan="2">
	     性别:&nbsp;&nbsp;&nbsp;<input type="radio" name="sex" value="男">男&nbsp;&nbsp;&nbsp;
	     <input type="radio" name="sex" value="女"/>女 &nbsp;&nbsp;&nbsp;
	     <input type="radio" name="sex" value="不限"/>不限
	   </td>
	   <td colspan="2">
	     年龄：<input class="inputAge" type="text" name="minAge" style="width:20px"/>岁至<input class="inputAge" type="text" name="maxAge" style="width:20px"/>岁
	  </td>
	  </tr>
	  
	  <tr class="trleft">
	  <td colspan="4">
	    学历:&nbsp;&nbsp;&nbsp;
	     <input type="radio" name="degree" value="高中">高中&nbsp;&nbsp;&nbsp;
	     <input type="radio" name="degree" value="大专">大专&nbsp;&nbsp;&nbsp;
	     <input type="radio" name="degree" value="本科">本科&nbsp;&nbsp;&nbsp;
	     <input type="radio" name="degree" value="本科以上">本科以上&nbsp;&nbsp;&nbsp;
	     <input type="radio" name="degree" value="不限">不限
	  </td>
	  </tr>
	  
	  <tr class="trleft">
	  <td>
	     专业要求:
	  </td>
	  <td colspan="3">
	     <input type="text" name="profession" style="width: 100%">
	  </td>
	  </tr>
	  
	  <tr class="trsolid trleft">
	  <td colspan="4">
	     所需工作经验:（几年工作经验，有哪些方面的经验）
	  </td>
	  </tr>
	  
	  <tr>
	  <td colspan="4">
	     <textarea  name="workOld" rows="10" style="width: 100%"></textarea>
	  </td>
	  </tr>
	  
	  <tr class="trsolid trleft">
	  <td>
	     其他要求：
	  </td>
	  <td colspan="3">
	       <input type="text" name="otherRequire" style="width: 100%">
	  </td>
	  </tr>
	  
	  <tr class="trleft">
	  <td>
	    部门主管:
	  </td>
	  <td>
	   直接上司: 
	  </td>
	  <td>
	    当地总经理:
	  </td>
	  <td>
	    当地总裁&集团HRA总监&CRO:
	  </td>
	  </tr>
	  
	  <tr>
	  <td>
	   <input type="text" name="orgCharge" style="width: 100%">
	  </td>
	  <td>
	    <input type="text" name="superior" style="width: 100%">
	  </td>
	  <td>
	    <input type="text" name="manager" style="width: 100%">
	  </td>
	  <td>
	     <input type="text" name="boss">
	  </td>
	  </tr>
	  
	  <tr  class="trleft">
	  <td>
	     日期: <input type="text" name="orgChargeDate" style="width: 100px">
	  </td>
	  <td>
	    日期: <input type="text" name="superiorDate" style="width: 100px">   
	  </td>
	  <td>
	    日期: <input type="text" name="managerDate" style="width: 100px">     
	  </td>
	  <td>
	    日期: <input type="text" name="bossDate" style="width: 100px">    
	  </td>
	  </tr>
	  
	   <tr>
	    <td></td><td></td><td></td><td></td>
	  </tr>
	  
	  <tr class="trsolid trleft">
	    <td colspan="4">人力资源管理部分填写</td>
	  </tr>
	  
	 <tr>
	    <td rowspan="2"> 受聘雇员: <input type="text" name="employName"></td>
	    <td rowspan="2"> 职位: <input type="text" name="positionName"></td>
	    <td colspan="2"> 招聘方法: </td>
	  </tr>
	  
	   
	  <tr>
	    <td  colspan="2">  
	     <input type="checkbox" name="methods" value="网络">网络
	     <input type="checkbox" name="methods" value="报纸广告">报纸广告
	     <input type="checkbox" name="methods" value="学校招聘">学校招聘
	     <input type="checkbox" name="methods" value="内部推荐">内部推荐
	     <input type="checkbox" name="methods" value="人才交流中">人才交流中
	    </td>
	  </tr>
	  
	 <tr>
	   <td rowspan="2" style="text-align: left">到职日期:
	      
	         <input  class="date_class"  type="text"  id="employComeDate" name="employComeDate" readonly  style="width: 40%; height:30px;border-bottom: 1px;">
		     <span class="input-group form_date1 date col-md-5 input-group-addon" data-date="" data-date-format="yyyy/mm/dd" data-link-field="employComeDate" data-link-format="yyyy/mm/dd"  id="glyphicon-employComeDate-calendar" style="margin: 0;width: 30px; height:30px;background-color: #F0F0F0; float: right" >
		         <span class="glyphicon glyphicon-calendar"></span>
		     </span>
	   </td>
	    <td> 替补: <input type="text" name="replaces" style="width: 100px"></td>
	    <td rowspan="2"  colspan="2"> 
	        <input type="checkbox" name="may" value="猎头">猎头
	        <input type="checkbox" name="may" value="其他">其他
	     </td>
	 </tr> 
	  
	  <tr>
	    <td> 新增: <input type="text" name="adds" style="width: 100px"></td>
	  </tr> 
	  
	</table>
	</div>