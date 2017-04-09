function cloneTable(jsondata) {
	var $table = {
		id : null,// 数据div id
		pid : null,// 分页div id
		offset : 0,// 偏移量
		limit : 15,// 数量
		url : null,// 数据加载地址
		params : null,
		getParams:null,//参数
		initData : function() {
			this.offset = 0;
			$("#" + this.id)
					.html(
							"<div class='ccenter'>正在加载数据<i class='fa fa-spinner fa-spin'></i></div>");
			this.loadData();
		},
		loadData : function() {
			var ps = {};
			if(this.getParams){
				ps = this.getParams();
			}
			var p = this.params;
			p.limit = this.limit;
			p.offset = (this.offset * this.limit);
			if(ps!=null){
				for(var i in ps){
					p[i] = ps[i];
				}
			}
			$.post(this.url, p, function(datas) {
				$table.dealDatas(datas);
				if($table.callback){
					$table.callback();
				}
				
			}, 'json');
		},
		dealDatas : function(datas) {
			var total = datas.total;
			$("#" + this.id).empty();
			for ( var i in datas.rows) {
				this.buildRow(datas.rows[i]);
			}
			if (datas.rows.length == 0) {
				if (this.noData) {
					this.noData();
				}
			}
			if (this.endData) {
				this.endData();
			}

			var pages = Math.ceil(total / this.limit);
			var nav = "<div class='col-md-12 '><nav style='text-align: right;padding: 5px;'>";
			nav += "<ul class='pagination'>";
			var startPages = Math.max(0, this.offset - 5);
			var endPages = Math.min(pages, this.offset + 5);
			for (var i = startPages; i < endPages; i++) {
				var active = (this.offset == i) ? "active" : "";
				nav += "<li class='" + active + "' val='" + (i)
						+ "'><a href='javascript:;'>" + (i + 1) + "</a></li>";
			}
			nav += "</ul></nav></div>";
			if (pages > 1) {
				$("#" + this.pid).empty();
				$("#" + this.pid).append(nav);
			}
		},
		callback :null,
		buildRow : null,
		noData : null,
		endData : null,
		changePage : function(p) {
			if (p == -1) {
				this.offset = this.offset - 1;
			} else if (p == -2) {
				this.offset = this.offset + 1;
			} else {
				this.offset = p;
			}
			this.loadData();
		}
	};
	$table.url = jsondata.url;
	$table.limit = jsondata.limit || $table.limit;
	$table.id = jsondata.id;
	$table.pid = jsondata.pid;
	$table.callback = jsondata.callback;
	$table.buildRow = jsondata.buildRow;
	$table.noData = jsondata.noData;
	$table.endData = jsondata.endData;
	$table.getParams = jsondata.getParams;
	$table.params = jsondata.params || {};
	$table.loadData();

	$(document).on("click", "#" + jsondata.pid + " .pagination li", function() {
		var val = $(this).attr("val");
		$table.changePage(parseInt(val));
	});

	return $table;
}
