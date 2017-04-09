<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<section class="content">
	<div class="form-group col-md-2">
		<label>部门</label><select class="form-control" id="orgId" name="orgId">
		</select>
	</div>
	<div class="form-group col-md-2"
		style="position: raletive; left: -20px;">
		<label>日期</label><select class="form-control" id="orderMonth"
			name="orderMonth">
			<option>选择日期</option>
		</select>
	</div>
	<div class="col-md-2" id="bt"
		style="position: raletive; top: 25px; left: -40px;">
		<button type="button" class="btn btn-primary " id="search_stationery">确定</button>
	</div>
	<table id="buystationeries_table"
		class="table-condensed table table-hover" data-row-style="rowStyle"
		data-side-pagination="server"></table>
</section>
<script>
	var orgId;
	var orderMonth;
	var $buystationeries_table;
	var url = $.webapp.root + "/admin/system/stationeries/datagridbuy.do";
	var params;
	$(function() {
		$.post($.webapp.root + "/admin/system/stationeries/tree.do", function(
				data) {
			for (var i = 0; i < data.length; i++) {
				$("#orderMonth").append(
						"<option value='"+data[i].orderMonth+"'>"
								+ data[i].orderMonth + "</option>");
			}
		}, "json");
		$.BOOT.autoselect("orgId", $.webapp.root + "/admin/system/org/tree.do",
				{
					title : "选择部门"
				});
		params = {
				columns : [ {
					field : 'stationeryName',
					title : '文具名称',
					sortable : true,
				}, {
					field : 'userName',
					title : '用户姓名',
				}, {
					field : 'orgName',
					title : '所属部门',
				}, {
					field : 'stationeryType',
					title : '文具类型',
				}, {
					field : 'stationeryUnit',
					title : '文具单位',
				}, {
					field : 'orderMonth',
					title : '订单日期',
					sortable : true,
				}, {
					field : 'amount',
					title : '数量',
				}, {
					field : 'price',
					title : '单价',
					sortable : true,
				}, {
					field : 'remark',
					title : '备注',
				}, ],
				paginationInfo : true,
				showExport : true
			};
		$buystationeries_table = $.BOOT.table("buystationeries_table", url,params );
	});
	function rowStyle(row, index) {
		if (row.status == "1") {
			return {
				classes : 'danger'
			};
		}
		return {};
	}
	$("#search_stationery").on("click", function() {
		orgId = $("#orgId option:selected").val();
		orderMonth = $("#orderMonth option:selected").val();
		url = $.webapp.root + "/admin/system/stationeries/datagridbuy.do?orgId="+orgId+"&orderMonth="+orderMonth;
		if (orgId == ""&&orderMonth != "选择日期") {
			$.BOOT.toast(false, "请选择部门");
			return;
		}
		if (orderMonth == "选择日期"&&orgId != "") {
			$.BOOT.toast(false, "请选择日期");
			return;
		}
		if (orgId == ""&&orderMonth == "选择日期"){
			window.location.reload();
			return;
		}
		params = {
			columns : [ {
				field : 'stationeryName',
				title : '文具名称',
			}, {
				field : 'stationeryType',
				title : '文具类型',
			}, {
				field : 'stationeryUnit',
				title : '文具单位',
			}, {
				field : 'price',
				title : '单价',
			}, {
				field : 'amountAll',
				title : '总数',
			}, {
				field : 'totalPrice',
				title : '总价格',
			} ],
			paginationInfo : true,
			showExport : true,
		};
		$buystationeries_table.bootstrapTable("destroy");
		$buystationeries_table = $.BOOT.table("buystationeries_table", url,params ); 
	})
</script>