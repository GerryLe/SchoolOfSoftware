<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
	<%@taglib prefix="mvc" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<style>
<!--
.skin-menu .dropdown-menu>li>a {
	padding: 0px;
}
-->
</style>
<li class="dropdown"><a href="#"  data-toggle="dropdown">语言</a>
<ul class="dropdown-menu">
		<li style="float: left; width: 50%; padding: 5px;">
			<a style="font-size: 16px; color: block" href="?lang=zh_CN"><spring:message code="language.cn" /></a>
		</li>
		<li style="float: left; width: 50%; padding: 5px;">
			<a style="font-size:16px;color:block" href="?lang=zh_TW" ><spring:message code="language.tw" /></a>
		</li>
		<li style="float: left; width: 50%; padding: 5px;">
			<a style="font-size:16px;color:block" href="?lang=en_US" ><spring:message code="language.en" /></a>
		</li>
	</ul> 
<li class="dropdown notifications-menu skin-menu"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="type" /></a>
	<ul class="dropdown-menu list-unstyled clearfix">
		<li style="float:left; width: 50%; padding: 5px;"><a href="javascript:void(0);" data-skin="skin-green-light" style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover"><div>
					<span style="display:block; width: 20%; float: left; height: 7px;" class="bg-green-active"></span><span class="bg-green" style="display:block; width: 80%; float: left; height: 7px;"></span>
				</div>
				<div>
					<span style="display:block; width: 20%; float: left; height: 20px; background: #f9fafc;"></span><span style="display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;"></span>
				</div></a>
			<p class="text-center no-margin" style="font-size: 12px"><spring:message code="Lightgreen" /></p></li>
		<li style="float:left; width: 50%; padding: 5px;"><a href="javascript:void(0);" data-skin="skin-green" style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover"><div>
					<span style="display:block; width: 20%; float: left; height: 7px;" class="bg-green-active"></span><span class="bg-green" style="display:block; width: 80%; float: left; height: 7px;"></span>
				</div>
				<div>
					<span style="display:block; width: 20%; float: left; height: 20px; background: #222d32;"></span><span style="display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;"></span>
				</div></a>
			<p class="text-center no-margin"><spring:message code="green" /></p></li>
		<li style="float:left; width: 50%; padding: 5px;"><a href="javascript:void(0);" data-skin="skin-blue-light" style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover"><div>
					<span style="display:block; width: 20%; float: left; height: 7px; background: #367fa9;"></span><span class="bg-light-blue" style="display:block; width: 80%; float: left; height: 7px;"></span>
				</div>
				<div>
					<span style="display:block; width: 20%; float: left; height: 20px; background: #f9fafc;"></span><span style="display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;"></span>
				</div></a>
			<p class="text-center no-margin" style="font-size: 12px"><spring:message code="Lightblue" /></p></li>
		<li style="float:left; width: 50%; padding: 5px;"><a href="javascript:void(0);" data-skin="skin-blue" style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover"><div>
					<span style="display:block; width: 20%; float: left; height: 7px; background: #367fa9;"></span><span class="bg-light-blue" style="display:block; width: 80%; float: left; height: 7px;"></span>
				</div>
				<div>
					<span style="display:block; width: 20%; float: left; height: 20px; background: #222d32;"></span><span style="display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;"></span>
				</div></a>
			<p class="text-center no-margin"><spring:message code="blue" /></p></li>
		<li style="float:left; width: 50%; padding: 5px;"><a href="javascript:void(0);" data-skin="skin-purple-light" style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover"><div>
					<span style="display:block; width: 20%; float: left; height: 7px;" class="bg-purple-active"></span><span class="bg-purple" style="display:block; width: 80%; float: left; height: 7px;"></span>
				</div>
				<div>
					<span style="display:block; width: 20%; float: left; height: 20px; background: #f9fafc;"></span><span style="display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;"></span>
				</div></a>
			<p class="text-center no-margin" style="font-size: 12px"><spring:message code="Lightgreen" /></p></li>
		<li style="float:left; width: 50%; padding: 5px;"><a href="javascript:void(0);" data-skin="skin-purple" style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover"><div>
					<span style="display:block; width: 20%; float: left; height: 7px;" class="bg-purple-active"></span><span class="bg-purple" style="display:block; width: 80%; float: left; height: 7px;"></span>
				</div>
				<div>
					<span style="display:block; width: 20%; float: left; height: 20px; background: #222d32;"></span><span style="display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;"></span>
				</div></a>
			<p class="text-center no-margin"><spring:message code="green" /></p></li>
		<li style="float:left; width: 50%; padding: 5px;"><a href="javascript:void(0);" data-skin="skin-red-light" style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover"><div>
					<span style="display:block; width: 20%; float: left; height: 7px;" class="bg-red-active"></span><span class="bg-red" style="display:block; width: 80%; float: left; height: 7px;"></span>
				</div>
				<div>
					<span style="display:block; width: 20%; float: left; height: 20px; background: #f9fafc;"></span><span style="display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;"></span>
				</div></a>
			<p class="text-center no-margin" style="font-size: 12px"><spring:message code="Lightred" /></p></li>
		<li style="float:left; width: 50%; padding: 5px;"><a href="javascript:void(0);" data-skin="skin-red" style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover"><div>
					<span style="display:block; width: 20%; float: left; height: 7px;" class="bg-red-active"></span><span class="bg-red" style="display:block; width: 80%; float: left; height: 7px;"></span>
				</div>
				<div>
					<span style="display:block; width: 20%; float: left; height: 20px; background: #222d32;"></span><span style="display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;"></span>
				</div></a>
			<p class="text-center no-margin"><spring:message code="red" /></p></li>
		<li style="float:left; width: 50%; padding: 5px;"><a href="javascript:void(0);" data-skin="skin-yellow-light" style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover"><div>
					<span style="display:block; width: 20%; float: left; height: 7px;" class="bg-yellow-active"></span><span class="bg-yellow" style="display:block; width: 80%; float: left; height: 7px;"></span>
				</div>
				<div>
					<span style="display:block; width: 20%; float: left; height: 20px; background: #f9fafc;"></span><span style="display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;"></span>
				</div></a>
			<p class="text-center no-margin" style="font-size: 12px;"><spring:message code="Lightyellow" /></p></li>
		<li style="float:left; width: 50%; padding: 5px;"><a href="javascript:void(0);" data-skin="skin-yellow" style="display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)" class="clearfix full-opacity-hover"><div>
					<span style="display:block; width: 20%; float: left; height: 7px;" class="bg-yellow-active"></span><span class="bg-yellow" style="display:block; width: 80%; float: left; height: 7px;"></span>
				</div>
				<div>
					<span style="display:block; width: 20%; float: left; height: 20px; background: #222d32;"></span><span style="display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;"></span>
				</div></a>
			<p class="text-center no-margin"><spring:message code="yellow" /></p></li>
	</ul></li>
<script type="text/javascript">
	$(function() {
		var cla = $.BOOT.getCookie("sidebar");
		if (cla != "") {
			var skinbar = $(".sidebar-mini");
			skinbar.attr("class", "");
			skinbar.addClass("sidebar-mini fixed " + cla);
		}
	});
	$(document).on("click", ".list-unstyled li a", function() {
		var v = $(this).attr("data-skin");
		var skinbar = $(".sidebar-mini");
		skinbar.attr("class", "");
		skinbar.addClass("sidebar-mini fixed " + v);
		$.BOOT.setCookie("sidebar", v);
	});
</script>