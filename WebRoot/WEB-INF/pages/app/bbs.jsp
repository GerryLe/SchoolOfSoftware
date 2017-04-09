<%@page import="com.rosense.module.common.web.servlet.WebContextUtil"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script src="${webRoot}/template/resource/plugins/tools/table.js"></script>
<style>
<!--
.bbsinbox {
	
}

.media {
	background-color: white;
	margin-bottom: 5px;
	margin-top: 0px;
	padding: 5px 0;
}

.media-object {
	width: 64px;
	height: 64px;
}

.media-body,.media-left,.media-right {
	display: table-cell;
	vertical-align: top;
	padding-left: 5px;
}

.bbsheader {
	
}

.aw-nav-tabs {
	position: relative;
	z-index: 1;
	min-height: 63px;
	min-height: 32px\0;
	margin: 0;
	padding: 30px 0px 0;
	border-color: #e6e6e6
}

.aw-nav-tabs.active>li {
	float: right
}

.aw-nav-tabs>li {
	margin: 0 4px 0 0;
	font-size: 14px
}

.aw-nav-tabs>li>a {
	line-height: 30px;
	padding: 0 13px;
	background-color: #fff;
	border: none;
	cursor: pointer;
}

.aw-nav-tabs>li>a:hover {
	border: none;
	border-bottom: 2px solid #155faa;
	background-color: #fff;
	color: #333
}

.aw-nav-tabs>li.active a {
	line-height: 30px;
	background-color: #fff;
	border: none;
	border-bottom: 2px solid #155faa;
	font-weight: 700;
	color: #333
}

.aw-nav-tabs>li.active a:hover,.aw-nav-tabs>li.active a:focus,.aw-nav-tabs>li>a:focus
	{
	border: none;
	border-bottom: 2px solid #155faa;
	background-color: #fff
}

.aw-nav-tabs .hidden-xs {
	position: absolute;
	top: 16px;
	left: 0px;
	margin: 0;
	font-size: 20px
}

.aw-tabs {
	height: 38px;
	line-height: 38px;
	padding: 0 20px;
	border-bottom: 1px solid #e6e6e6;
	text-align: center
}

.aw-tabs li.active a {
	color: #333;
	font-weight: 700
}

.aw-tabs ul li {
	display: inline
}

.aw-tabs ul li a {
	margin: 0 10px
}

.aw-main-content {
	padding: 0 0 20px;
	border: 1px solid #e6e6e6;
	border-radius: 4px 4px 0 0;
}

.aw-explore-list {
	padding: 0 20px;
}

.aw-common-list {
	width: 100%;
	height: auto;
	overflow: hidden;
}

.aw-common-list .aw-item {
	position: relative;
	z-index: 0;
	min-height: 45px;
	padding: 14px 0 14px 50px;
}

.aw-common-list .aw-item>.aw-user-name {
	position: absolute;
	left: 0;
	top: 18px;
}

.aw-user-name,.aw-topic-name {
	font-size: 12px;
	line-height: 20px;
	font-weight: 400;
}

.aw-user-name img,.aw-topic-name img {
	width: 40px;
	height: 40px;
	border-radius: 4px;
}

.aw-common-list .aw-question-content h4 {
	margin: 0 0 3px;
	padding-right: 20px;
	font-size: 14px;
	word-wrap: break-word;
}

.aw-common-list .aw-question-content p {
	margin: 0;
	color: #999;
}

.aw-common-list>div+div {
	border-top: 1px solid #F5F5F5;
}

h1,h2,h3,h4,h5,h6 {
	margin-top: 0;
	font-size: 100%;
	line-height: 1.7;
}

.aw-common-list .aw-question-content>span,.aw-common-list .aw-question-content p span
	{
	font-size: 12px;
	font-weight: 400;
	color: #999;
}

.aw-common-list .aw-item .aw-question-content .aw-user-name {
	color: #666;
	margin-right: 5px;
}
-->
</style>
<div class="bbsheader aw-main-content">
	<ul class="nav nav-tabs aw-nav-tabs active hidden-xs">
		<li><a href="javascript:;" data-name="remarks">最多评论</a></li>
		<li><a href="javascript:;" data-name="views">最多阅读</a></li>
		<li class="active"><a href="javascript:;" data-name="createDate">最新发布</a></li>
		<div class="hidden-xs" style="cursor: pointer;">
			<form class="navbar-form navbar-left" role="search">
				<a class="btn btn-primary" href="/app/bbs/publish.html"><i class="fa fa-edit"></i> 发帖</a>
				<div class="input-group">
					<input type="text" class="form-control" placeholder="搜索帖子" id="search-name"> <span class="input-group-btn">
						<button type="button" class="btn" onclick="searchBbs();">
							<i class="fa fa-search" style="color: white;"></i>&nbsp;
						</button>
					</span>
				</div>
			</form>
		</div>
	</ul>
	<div class="aw-mod aw-explore-list">
		<div id="bbsinbox" class="aw-common-list"></div>
		<div id="bbspage"></div>
	</div>
</div>
<script type="text/javascript">
	var table = null;
	$(function() {
		table = cloneTable({
			id : "bbsinbox",
			pid : "bbspage",
			url : "/app/bbs/datagrid.do",
			buildRow : function(row) {
				var html = "";
				if(row.title=='undefined'||row.title==undefined){
					return;
				}
				html += '<div class="aw-item ">';
				html += '<a class="aw-user-name hidden-xs" href="javascript:;">';
				html += '<img src="/common/photo.do?id=' + row.userId
						+ '" alt=""></a>	';
				html += '<div class="aw-question-content">';
				html += '<h4>';
				html += '<a target="_blank" href="/app/bbs/view/'+row.id+'.html">' + row.title;
				html += '</a></h4>';
				html += '<a target="_blank" href="/app/bbs/view/'
						+ row.id
						+ '.html?answer=true" class="pull-right text-color-999">回复</a>';
				html += '<p>';
				html += '<span class="text-color-999">' + row.remarks
						+ '个回复 • ' + row.views + ' 次浏览 • ' + row.formatDate;
				html += '</p>';
				html += '</div>';
				html += '</div>';
				html += '';
				html += '';
				$("#bbsinbox").append(html);
			},
			noData : function() {
				$("#bbsinbox").append(
						"<div style='text-align:center;'>没有查询到帖子</div>");
			}
		});
	});
	$(document).on("click", ".aw-nav-tabs li a", function() {
		$(".aw-nav-tabs li").removeClass("active");
		$(this).parent().addClass("active");
		var name = $(this).attr("data-name");
		table.params["filter"] = name;
		table.loadData();
	});
	function searchBbs() {
		table.params["title"] = $("#search-name").val();
		table.initData();
	}
	function viewbbss(id) {
		location.href = "/bbss/view/" + id + ".do";
	}
</script>