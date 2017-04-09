<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<section class="container" style="min-height: 550px;">
	<div id="filter-bar">
		<div class="btn-toolbar">
			<div class="hidden-xs" style="cursor: pointer;">
				<form id="form-bbs" class="navbar-form navbar-left" role="search" style="padding-left: 5px;">
					<a class="btn btn-primary" href="/publish.html" style="margin-right: 10px;"><i class="fa fa-edit"></i> 发现</a>
					<div class="input-group">
						<input type="text" class="form-control" placeholder="搜索帖子" id="title" name="title"> <span class="input-group-btn">
							<button type="button" class="btn" onclick="searchBbs();">
								<i class="fa fa-search" style="color: white;"></i>&nbsp;
							</button>
						</span>
					</div>
				</form>
			</div>
		</div>
	</div>
	<table id="bbs_table" class="table-condensed table table-hover" data-row-style="rowStyle" data-side-pagination="server"></table>
</section>
<script type="text/javascript">
	var $bbs_table;
	$(function() {
		$bbs_table = $.BOOT.table("bbs_table", "/bbs/datagridmanager.do", {
			columns : [ {
				field : 'title',
				title : '标题',
				formatter : function(value, row) {
					return buildtitle(row);
				}
			}, {
				field : 'createDate',
				title : '时间'
			}, {
				field : 'id',
				title : '操作',
				formatter : function(value, row) {
					return buildhtml(row);
				}
			} ],
			paginationInfo : true,
		});
	});
	function searchBbs() {
		$bbs_table.bootstrapTable('refresh', {
			query : {
				title : encodeURI($("#title").val())
			}
		});
	}
	function buildtitle(row) {
		var html = "<a target='_blank' href='/bbs/"+row.id+".html'>"
				+ row.title + "</a>";
		return html;
	}
	function buildhtml(row) {
		var html = "";
		html += "<button class='btn btn-danger btn-xs' onclick=\"deletebbs('"
				+ row.id + "');\">删除</button> ";
		if (!row.recommend) {
			html += "<button class='btn btn-primary btn-xs' onclick=\"recommend('"
					+ row.id + "',true);\">推荐</button> ";
		} else {
			html += "<button class='btn btn-primary btn-xs' onclick=\"recommend('"
					+ row.id + "',false);\">取消推荐</button> ";
		}
		return html;
	}
	function recommend(id, recommend) {
		var form_url = "/bbs/no_recommend.do";
		$.post(form_url, {
			id : id,
			recommend : recommend
		}, function(result) {
			$.BOOT.toast1(result);
			$bbs_table.bootstrapTable('refresh');
		}, 'json');
	}
	function deletebbs(id) {
		var form_url = "/bbs/no_delete.do";
		var json = {
			title : "",
			text : "确定删除吗",
			showCancelButton : true,
			type : 'warning',
			call : function() {
				$.post(form_url, {
					id : id
				}, function(result) {
					$.BOOT.toast1(result);
					$bbs_table.bootstrapTable('refresh');
				}, 'json');
			}
		};
		$.BOOT.alert(json, true);
	}
</script>