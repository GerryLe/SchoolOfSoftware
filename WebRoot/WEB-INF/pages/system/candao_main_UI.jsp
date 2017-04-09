<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<jsp:include page="/template/modal.jsp"><jsp:param value="addroles" name="id" /><jsp:param value="编辑角色" name="title" /></jsp:include>
<section class="content">
	<div id="user-form-id"></div>
	<div id="filter-bar"></div>
	<table id="user_table" class="table-condensed table table-hover" data-row-style="rowStyle" data-side-pagination="server"></table>
</section>
<script type="text/javascript">
	$(function() {
		alert("正在开发！");
	});
</script>