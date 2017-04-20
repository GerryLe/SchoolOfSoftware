<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>

<style type="text/css">
body {
	background: #d2d6de;
	background-image: url("/template/images/bg.gif");
}
</style>
<div class="login-box">
	<div class="login-logo">
		<b>登录</b>
	</div>
	<div class="login-box-body">
		<p class="login-box-msg" id="tool-tip" style="color: red;"></p>
		<form id="defaultForm" method="post">
			<div class="form-group has-feedback">
				<input type="text" name="account" value="admin" class="form-control"
					placeholder="用户名"> <span
					class="glyphicon glyphicon-user form-control-feedback"></span>
			</div>
			<div class="form-group has-feedback">
				<input type="password" value="123456" name="password" class="form-control"
					placeholder="密码"> <span
					class="glyphicon glyphicon-lock form-control-feedback"></span>
			</div>
			<div class="form-group has-feedback select"  id="grade">
				<input type="radio" value="student" name="grade" checked="checked" style="margin-left: 30px"> 学生
				<input type="radio" value="teacher" name="grade" style="margin-left: 30px"> 教师
				<input type="radio" value="admin" name="grade" style="margin-left: 30px"> 管理员
			</div>
			<div class="row">
				<div class="col-xs-12">
					<button type="submit" class="btn btn-primary btn-block btn-flat">登录</button>
				</div>
			</div>
		</form>
	</div>
</div>
<script type="text/javascript">
	$.BOOT.form("defaultForm", {
		name : {
			validators : {
				notEmpty : {
					message : '用户名不能为空'
				}
			}
		},
		password : {
			validators : {
				notEmpty : {
					message : '密码不能为空'
				}
			}
		}
	}, function(params) {
		var hrefLogin="/system/login/login.do";
		if($("input:radio:checked").val()=="teacher"){
			hrefLogin="/system/login/loginTeacher.do";
		}
		$.post(hrefLogin, params, function(result) {
			var href = "window.location.replace('/admin/index/index.do')";
			if (result.status) {
				setTimeout(href, 500);
			} else {
				$("#tool-tip").html(result.msg);
			}
		}, 'json');
	});
</script>
<!--[if lte IE 10]><script>window.location.href='http://cdn.dmeng.net/upgrade-your-browser.html?referrer='+location.href;</script><![endif]-->