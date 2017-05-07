<%@page import="com.rosense.basic.util.cons.Const"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
	<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script src="${webRoot}/template/resource/procedure/jquery.wheelmenu.js"></script>
<link rel="stylesheet" type="text/css" href="${webRoot}/template/resource/procedure/wheelmenu.css" />

<style>
<!--
.notifications-menu li {
	white-space: nowrap;
	text-overflow: ellipsis;
	-o-text-overflow: ellipsis;
	overflow: hidden;
}
.wrapperr {
	/* height: auto !important;  */
	height:800px;
	width:2000px;
	display: table-cell; 
    vertical-align:middle; 
 	text-align:center; 
	margin:0 auto;
    overflow: hidden; 
}


a {
	text-decoration: none;
	 vertical-align: middle;
}

.pointer {

	color: #34495e;
	font-family: 'Pacifico', cursive;
	font-size: 22px;
	margin-top: 20px;
} 

 .wheel-button, .wheel-button:visited {
    text-align:center;
    display: table-cell; 
    vertical-align:middle; 
    padding:0px;
	line-height: 100px;
	background: #df4727;
	border-radius: 100px;
	background-color:#ecf0f5;
	border:0px;
	width:180px;
	height: 180px;
	margin:0 auto;
	display: block;
	box-shadow: 0 1px 2px rgba(0, 0, 0, 0.25);
	-moz-box-shadow: 0 1px 2px rgba(0, 0, 0, 0.25);
	-webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, 0.25);
}

.wheel-button:hover {
	color: white;
} 

.wheel-button.ne {
	border-color: white;
	background: #1ABC9C;
	color: #34FFFF;
	position: absolute;
	bottom: 10px;
	left: 10px;
}

.wheel-button.nw {
	border-color: white;
	background-color: #E67E22;
	color: #FFFC44;
	position: absolute;
	bottom: 10px;
	right: 10px;
}

.wheel-button span, .wheel span {

	position: relative;
	-moz-transition: all 1s ease;
	-webkit-transition: all 1s ease;
	-o-transition: all 1s ease;
	transition: all 1s ease;
	display: block;
} 

.wheel-button.active span {
	transform: rotate(135deg);
	-ms-transform: rotate(135deg); 
	-webkit-transform: rotate(135deg); 
}

.wheel li a, .wheel li a:visited {
	background: rgba(0, 0, 0, 0.65);
	border-radius: 50px;
	font-size: 14px; 
	font-weight:bold;
	text-align: center;
	padding-top:30px;
	width: 108px;
	height: 108px;
	border: 1px solid black;
	box-shadow: 0 1px 2px rgba(0, 0, 0, 0.25), inset 0 1px 1px
		rgba(255, 255, 255, 0.5);
	-moz-box-shadow: 0 1px 2px rgba(0, 0, 0, 0.25), inset 0 1px 1px
		rgba(255, 255, 255, 0.5);
	-webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, 0.25), inset 0 1px 1px
		rgba(255, 255, 255, 0.5);
	color: white;
	-moz-transition: all 0.25s ease;
	-webkit-transition: all 0.25s ease;
	-o-transition: all 0.25s ease;
	transition: all 0.25s ease;
	/* padding: 10px */
}

.wheel li a:hover {
	background: rgba(0, 0, 0, 0.8);
}
-->
</style>
<div class="wrapper">
	<header class="main-header" id="main-header">
		<!-- Logo -->
		<a href="javascript:void;" class="logo"> <span class="logo-mini"><b>OA</b></span> <span class="logo-lg"><b>Ling-Nan OA</b></span>
		</a>
		<nav class="navbar navbar-static-top" role="navigation">
			<a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button"> </a> <input type="hidden"  id="index-p-id" value="${p}">
			<div class="navbar-custom-menu">
			 
				<ul class="nav navbar-nav">
					<!-- Notifications: style can be found in dropdown.less -->
					<jsp:include page="../../template/skin.jsp"></jsp:include>
					<li class="dropdown notifications-menu"><a href="#" class="dropdown-toggle" data-toggle="dropdown"> <i id="shake-bell-id" class="fa fa-bell-o shake "></i> <span>&nbsp;</span><span class="label label-warning " style="right: 10px;" id="notice-id-count">0</span>
					</a>
						<ul class="dropdown-menu" id="notice-id">
						</ul></li>
					<!-- User Account: style can be found in dropdown.less -->
					<li class="dropdown user user-menu"><a href="#" class="dropdown-toggle" data-toggle="dropdown"> 
					
					<img id="photoloat" src="/common/photo.do?id=${userId}" class="user-image" style="border: 2px solid white;" alt="User Image"> <span class="hidden-xs">${userName}</span> <span>&nbsp;</span>
					</a>
						<ul class="dropdown-menu">
							<!-- User image -->
							<li class="user-header"><img id="photoloat" style="cursor: pointer;" src="/common/photo.do?id=${userId}" class="img-circle" alt=""> <%-- <p>${score}积分</p> --%>
								<p>${userName}</p></li>
							<li class="user-footer">
								<div class="pull-left">
									<a href="#" class="btn btn-default btn-flat" onclick="$('#menu-gerenziliao').click();"><spring:message code="Thepersonaldata" /></a>
								</div>
								<div class="pull-right">
									<a href="#" class="btn btn-default btn-flat" id="logout_btn_id" onclick="$.BOOT.logout('login.jsp');"><spring:message code="Thecancellation" /></a>
								</div>
							</li>
						</ul>
					</li>
				</ul>
			</div>
		</nav>
	</header>
	<!-- Left side column. contains the logo and sidebar -->
	<aside class="main-sidebar">
		<!-- sidebar: style can be found in sidebar.less -->
		<section class="sidebar" id="sidebar-menu" style="overflow-y:auto; "></section>
		<!-- /.sidebar -->
	</aside>
	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<ul id="simple-tabs" class="nav nav-tabs">
		</ul>
		<div id="simle-tab_content" class="tab-content">
		   <div class="wrapperr">
			<a href="#wheel" class="wheel-button">
				<span style="width:180px; height:180px; border-radius:50%; overflow:hidden;"><img alt="点击" src="${webRoot}/template/resource/procedure/images/lingnan.png" width="180px" height="180" border-radius="50%" style="vertical-align: middle;"></span>
			</a>
			<div class="pointer">Ling-Nan</div>
		</div>
	   </div> 
	</div>
	<div class="control-sidebar-bg"></div>
</div>
<script type="text/javascript">
	var cache_json = "${cache}".replace(/=/g, ":");
	cache_json = eval("(" + cache_json + ")");
	$(function() {
		
		$.BOOT.notice();
		$.BOOT.navmenu("sidebar-menu", function() {
			$.BOOT.posmenu();
		});
		var noticeCount = $("#notice-id-count").val() + 0;
		if (noticeCount > 0) {
			$("#shake-bell-id").addClass("shake-constant");
		}
		$('body').oneTime('3s', 'S', function() {
			$("#shake-bell-id").removeClass("shake-constant");
		});
		$("#sidebar-menu").css("height", $.webapp.getInner().height - 50);
		
		
		$(".wheel-button").wheelmenu({
	        trigger: "hover",
	        animation: "fly",
	        animationSpeed: "fast"
	      });
	});
	//点击菜单隐藏图标
	 $("#sidebar-menu").click(function(){
		 $(".wrapperr").hide();
	});  
	
	
	 $("#photoloat").click(function(){
		  location.reload();
	});  
	</script>