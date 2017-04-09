//判断手机号码是否为有效
function vailPhone(){
      var phone = jQuery("#phone").val();
      var flag = false;
      var message = "";
      //var myreg = /^(((13[0-9]{1})|(14[0-9]{1})|(17[0]{1})|(15[0-3]{1})|(15[5-9]{1})|(18[0-9]{1}))+\d{8})$/;  
      var myreg=/^1[3|4|5|8]\d{9}$/;
      if(phone == ''){
        message = "手机号码不能为空！";
      }else if(phone.length !=11){
        message = "请输入有效的手机号码,为11位！";
      }else if(!myreg.test(phone)){
        message = "请输入有效的手机号码！";
      }else{
    	  $("#phonereg").attr("visible", false);
          flag = true;
      }
      if(!flag){
     //提示错误效果
        $("#phonereg").html("<i class=\"icon-error ui-margin-right10\"> <\/i>"+message);
        return vailPhone();
      }
      return flag;
    }