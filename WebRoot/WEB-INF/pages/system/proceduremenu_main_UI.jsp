<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<jsp:include page="/template/modal.jsp"><jsp:param value="addprocedure" name="id" /><jsp:param
		value="" name="title" /></jsp:include>
<html>
<head>
<meta charset="utf-8">
<script src="${webRoot}/template/resource/procedure/jquery.wheelmenu.js"></script>
<link rel="stylesheet" type="text/css" href="${webRoot}/template/resource/procedure/wheelmenu.css" />
<style>

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
</style>

<script>
	  $(document).ready(function(){
			$(".wheel-button").wheelmenu({
        trigger: "hover",
        animation: "fly",
        animationSpeed: "fast"
      });
			
		});
	  
	 $(document).on("click",".item",function(){
		  window.open($.webapp.root + $(this).children("a")[0].getAttribute('href'), 'newwindow','height=700,width=1000,top=180,left=300,toolbar=no,menubar=no,resizable=no,localtion=no,status=no,scrolbars=yes,text-align=center,padding-left=20'
		  
		  );
	  });  
		
	</script>
</head>

	<div class="wrapperr">
		<!-- <div class="main"> -->
			<a href="#wheel" class="wheel-button">
			<span style="width:180px; height:180px; border-radius:50%; overflow:hidden;"><img alt="点击" src="${webRoot}/template/resource/procedure/images/can-dao.png" width="180px" height="180" border-radius="50%" style="vertical-align:middle;"></span>
				<%-- <img alt="点击" src="${webRoot}/template/resource/procedure/images/can-dao.png" style="width:120px; height:120px; border-radius:50%; overflow:hidden;"> --%>
			</a>
			<div class="pointer">Can-dao流程图</div>
			<ul id="wheel" class="wheel" data-angle="all">
				<li class="item">
					<a href="/admin/system/procedure/entry_main_UI.do" target="kk">内部PRC入职流程图</a>
				</li>
				<li class="item">
					<a href="/admin/system/procedure/leave_main_UI.do" target="kk">内部PRC离职流程图</a>
				</li>
				<li class="item">
					<a href="/admin/system/procedure/employLeave_main_UI.do" target="kk">员工离职</a>
				</li>
				<li class="item">
					<a href="/admin/system/procedure/employEntry_main_UI.do" target="kk">PRC员工入职</a>
				</li>
				<li class="item">
					<a href="/admin/system/procedure/becomestaff_main_UI.do" target="kk">内部PRC转正流程图</a>
				</li>
				<li class="item">
					<a href="/admin/system/procedure/ reassign_main_UI.do" target="kk">内部PRC转调流程图</a>
				</li>
				<li class="item">
					<a href="/admin/system/procedure/suAddWorkHoliday_main_UI.do" target="kk">加班与假期申请及审批流程（日常）</a>
				</li>
				<!-- <li class="item">
					<a href="/admin/system/procedure/otherHoliday_main_UI.do" target="kk">病假、婚假、产假（陪产假）申请流程</a>
				</li>
				<li class="item">
					<a href="/admin/system/procedure/addWorkHoliday_main_UI.do" target="kk">加班与假期申请及审批流程</a>
				</li> -->
				<li class="item">
					<a href="/admin/system/procedure/procecar_main_UI.do" target="kk">预订出差人员机票、车票流程</a>
				</li>
				<li class="item">
					<a href="/admin/system/procedure/proceroom_main_UI.do" target="kk">预订出差人员住宿酒店流程</a>
				</li>
				<li class="item">
					<a href="/admin/system/procedure/suStationery_main_UI.do" target="kk">文具采购流程（日常）</a>
				</li>
				<li class="item">
					<a href="/admin/system/procedure/stationery_main_UI.do" target="kk">文具采购流程</a>
				</li>
				<li class="item">
					<a href="/admin/system/procedure/circuitcard_main_UI.do" target="kk">印制名片流程</a>
				</li>
				<li class="item">
					<a href="/admin/system/procedure/check_main_UI.do" target="kk">每月考勤统计流程</a>
				</li>
				<li class="item">
					<a href="/admin/system/procedure/comeAndLeaCard_main_UI.do" target="kk">入、离职员工工卡管理流程</a>
				</li>
				<li class="item">
					<a href="/admin/system/procedure/seal_main_UI.do" target="kk">盖章流程（</a>
				</li>
				<li class="item">
					<a href="/admin/system/procedure/place_main_UI.do" target="kk">场地管理流程</a>
				</li>
			</ul>

		</div>
		<!-- <iframe id="frame1" src="" width="100%" height="900px" name="kk" 
			style="margin-top: 10px; float: right; frameborder: no; border: 0;"> </iframe> -->
	<!-- </div> -->

</html>
