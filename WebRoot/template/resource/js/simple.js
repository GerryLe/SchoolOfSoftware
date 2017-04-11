function sendFile(file, formid, id) {
	$(".note-toolbar.btn-toolbar").append('正在上传图片');
	var options = {
		url : '/admin/attach/no_summernote.do',
		type : 'post',
		dataType : 'json',
		success : function(data) {
			if (data == "")
				return;
			// 把图片放到编辑框中。editor.insertImage 是参数，写死。后面的http是网上的图片资源路径。
			// 网上很多就是这一步出错。
			$(".note-alarm").html("上传成功,请等待加载");
			setTimeout(function() {
				$(".note-alarm").remove();
			}, 3000);
		},
		error : function() {
			$(".note-alarm").html("上传失败");
			setTimeout(function() {
				$(".note-alarm").remove();
			}, 3000);
		}
	};
	$('#' + formid).ajaxSubmit(options);
};
$.BOOT = {
	obj : null,// 判断点击次数
	currPage : null,// 当前页面
	cachePage : [],// 缓存页面
	currRow : null,// 当前行
	jscache : [],
	canclick : function(c) {
		this.obj = $(c);
		var cc = $(c).attr("cc");
		if (cc == 'true') {
			return false;
		}
		$(c).attr("cc", "true");
		return true;
	},
	initclick : function() {
		if (this.obj != null) {
			$(this.obj).removeAttr("cc");
		}
	}
};
/**
 * 循环改变颜色
 */
$.BOOT.eachChange = function(c, cla1, cla2) {
	$(cla1).each(function() {
		$(this).removeClass(cla2);
	});
	$(c).addClass(cla2);
};
document.onclick = function() {
	$.BOOT.initclick();
};
/**
 * 组件select
 */
$.BOOT.select = function(id, json, title) {
	var select = $("#" + id);
	var recursion = function(data, deep) {
		for ( var n in data) {
			var text = "";
			for (var i = 0; i < deep; i++) {
				text += "&nbsp;&nbsp;";
			}
			text += data[n].class_name;
			select.append("<option value='" + data[n].class_id + "'>" + text
					+ "</option>");
			if (data[n].nodes) {
				recursion(data[n].nodes, deep + 1);
			}
			if (data[n].children) {
				recursion(data[n].children, deep + 1);
			}
		}
	};
	select.append("<option value=''>" + title + "</option>");
	recursion(json, 1);
};
/**
 * type:success,warning,danger,secondary
 */
$.BOOT.toast1 = function(result, callback) {
	var json = {
		text : result.msg,
		textAlign : 'center',
		position : 'bottom-center',
		allowToastClose : false,
		hideAfter : 1200,
		loader : false,
		stack : true,
	};
	if (callback) {
		json["afterHidden"] = callback;
	}
	if (!result.status) {
		json["bgColor"] = " #3c763d";
		json["bgColor"] = " #a94442";
	}
	$.toast(json);
};
$.BOOT.toast = function(status, msg, callback) {
	var json = {
		text : msg,
		textAlign : 'center',
		position : 'bottom-center',
		allowToastClose : false,
		hideAfter : 1500,
		loader : false,
		stack : true,
	};
	if (callback) {
		json["afterHidden"] = callback;
	}
	if (!status) {
		json["bgColor"] = " #3c763d";
		json["bgColor"] = " #a94442";
	}
	$.toast(json);
};
$.BOOT.alert = function(json, custom) {
	if (custom) {
		json["confirmButtonText"] = "确定";
		json["cancelButtonText"] = "取消";
	}
	json["confirmButtonColor"] = "#1abc9c";
	swal(json, function(isConfirm) {
		if (isConfirm) {
			if (json.call) {
				json.call();
			}
		}
	});
};
/**
 * 通知
 */
$.BOOT.notice = function() {
	var href = $.webapp.root + "/admin/system/message/list.do";
	$
			.post(
					href,
					{},
					function(result) {
						var ul = $("#notice-id");
						$("#notice-id-count").html(result.length);
						ul.append("<li><ul class='menu'>");
						for (var i = 0; i < Math.min(result.length, 10); i++) {
							var li = "<li><a href='#'>";
							li += "<i class='fa fa-circle text-aqua'></i>";
							li += result[i].content + "</a></li>";
							ul.append(li);
						}
						ul.append("</ul></li>");
						var href = "/admin/system/message/my_message_main_UI.do";
						ul
								.append("<li class='footer'><a href='#' onclick=\"$('#menu-wodexiaoxi').click();\">你有"
										+ result.length + "条未读消息,显示所有</a></li>");
					}, 'json');
};
/**
 * 禁用button
 */
$.BOOT.btn = function(id, disabled) {
	if (disabled) {
		$("#" + id).attr("disabled", true);
	} else {
		$("#" + id).removeAttr("disabled");
	}
};
/**
 * 自动
 */
$.BOOT.autoselect = function(id, url, params) {
	params = params || {};
	$.post($.webapp.root + url, params, function(result) {
		$.BOOT.select(id, result, params.title ? params.title : "根目录");
		if (params.callback) {
			params.callback();
		}
	}, 'json');
};

/**
 * 座机分号
 */
$.BOOT.autoselectCourse = function(id, url, params) {
	params = params || {};
	$.post($.webapp.root + url, params, function(result) {
		$.BOOT.selectCourse(id, result, params.title ? params.title : "根目录");
		if (params.callback) {
			params.callback();
		}
	}, 'json');
};

/**
 * 座机组件select
 */
$.BOOT.selectCourse = function(id, json, title) {
	var select = $("#" + id);
	var recursion = function(data, deep) {
		for ( var n in data) {
			var text = "";
			for (var i = 0; i < deep; i++) {
				text += "&nbsp;&nbsp;";
			}
			text += data[n].course_name;
			select.append("<option value='" + data[n].course_no + "'>" + text
					+ "</option>");
			if (data[n].nodes) {
				recursion(data[n].nodes, deep + 1);
			}
			if (data[n].children) {
				recursion(data[n].children, deep + 1);
			}
		}
	};
	select.append("<option value=''>" + title + "</option>");
	recursion(json, 1);
};

/**
 * 部门主管
 */
$.BOOT.autoselectcharge = function(id, url, params) {
	params = params || {};
	$.post($.webapp.root + url, params, function(result) {
		$.BOOT.selectcharge(id, result, params.title ? params.title : "根目录");
		if (params.callback) {
			params.callback();
		}
	}, 'json');
};

/**
 * 组件selectcharge
 */
$.BOOT.selectcharge = function(id, json, title) {
	var select = $("#" + id);
	var recursion = function(data, deep) {
		for ( var n in data) {
			var text = "";
			for (var i = 0; i < deep; i++) {
				text += "&nbsp;&nbsp;";
			}
			text += data[n].name;
			select.append("<option value='" + data[n].name + "'>" + text
					+ "</option>");
			if (data[n].nodes) {
				recursion(data[n].nodes, deep + 1);
			}
			if (data[n].children) {
				recursion(data[n].children, deep + 1);
			}
		}
	};
	select.append("<option value=''>" + title + "</option>");
	recursion(json, 1);
};

/**
 * 加载菜单
 */
$.BOOT.loadMenu = function($tree_id, callback) {
	var href = $.webapp.root + "/admin/system/menu/tree.do";
	$.post(href, {}, function(result) {
		$('#' + $tree_id).empty();
		var tree_data = $.BOOT.menuList(result, "O");
		$('#' + $tree_id).data('jstree', false);
		var $tree = $('#' + $tree_id).jstree({
			'plugins' : [ "wholerow", "checkbox" ],
			'core' : {
				'data' : result,
				'themes' : {
					'name' : 'proton',
					'responsive' : true
				}
			}
		});
		if (callback) {
			callback($tree, tree_data);
		}
	}, 'json');
};
/**
 * 保存菜单
 */
$.BOOT.savePermits = function($tree, $value, type) {
	var principals = [];
	var principal = {
		"principalId" : $value,
		"principalType" : type
	};
	principals.push(principal);
	var resources = [];
	var nodes = $tree.jstree("get_checked"); // 使用get_checked方法
	$.each(nodes, function(i, n) {// 许可菜单
		var menu = {
			"menuId" : n,
		};
		resources.push(menu);
	});
	$.post($.webapp.root + "/admin/system/acl/no_grantPermits.do", {
		"principals" : JSON.stringify(principals),
		"resources" : JSON.stringify(resources)
	}, function(result) {
		if (result.status) {
			$.BOOT.toast(true, result.msg);
		} else {
			$.BOOT.toast(false, result.msg);
		}
	}, 'json');
};
/**
 * 还原菜单
 */
$.BOOT.getPermits = function($tree, $tree_data, id, type) {
	$tree.jstree('uncheck_all');
	$.post($.webapp.root + "/admin/system/acl/getPermits.do", {
		"principalId" : id,
		"principalType" : type
	}, function(result) {
		$.each(result.menus, function(i, p) {
			$tree.jstree('open_node', p.menuId);
			$tree.jstree('check_node', p.menuId);
		});
		if ($tree_data) {
			$.each($tree_data, function(i, p) {
				$tree.jstree('uncheck_node', p.id);
			});
		}
		$.each(result.opers, function(i, p) {
			$tree.jstree('open_node', p.operMenuId);
			$tree.jstree('check_node', p.operMenuId);
		});
	}, 'json');
};
/**
 * 处理菜单
 */
$.BOOT.menuList = function(rs, type) {
	var menuList = [];
	var recursion = function(data, parent) {
		for ( var d in data) {
			var menu = data[d];
			if (menu.children && menu.children.length > 0) {
				recursion(menu.children);
			} else {
				if (menu.type == type) {
					var m = {};
					m.id = menu.id;
					menuList.push(m);
				}
			}
		}
	};
	recursion(rs[0].children);
	return menuList;
};
/**
 * 定位菜单
 */
$.BOOT.posmenu = function() {
	var p = $("#index-p-id").val();
	if (p != '') {
		$.BOOT.page(p);
		$(".sidebar-menu .treeview").each(function() {
			$(this).removeClass("active");
		});
		$(".nav-menu").each(function() {
			var click = $(this).attr("onclick");
			if (click && click.indexOf(p) != -1) {
				$(this).parent().parent().parent().addClass("active");
			}
		});
	}
};
$.BOOT.initmenu = function() {
	$(document)
			.on(
					"click",
					".sidebar-menu .nav-menu",
					function() {
						var url = $(this).attr("data-url");
						var name = $(this).attr("data-name");
						var id = $(this).attr("data-id");
						if (url != "null") {
							var has = false;
							$(".simple-tabs-li").removeClass("active");
							$("#simle-tab_content .tab-pane").removeClass(
									"active");
							$(".simple-tabs-li").each(function() {
								var tid = $(this).attr("data-id");
								if (id == tid) {
									has = true;
								}
							});
							if (has) {
								var liid = "li-" + id;
								$("#" + liid).addClass("active");
								$("#" + id).addClass("active");
								return;
							}
							var tab = $("#simple-tabs");
							var html = '<li id="li-'
									+ id
									+ '" class="active simple-tabs-li" data-id="'
									+ id + '">';

							html += '<div class="btn-group" >';
							html += '<a href="#'
									+ id
									+ '"  class="btn btn-default btn-tab" data-toggle="tab">'
									+ name + '</a>';
							html += '<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">';
							html += '<span class="caret"></span>';
							html += '<span class="sr-only">Toggle Dropdown</span>';
							html += '</button>';
							html += '<ul class="dropdown-menu dropdown-menu-right">';
							html += '<li><a href="#" class="menu-close">关闭</a></li>';
							html += '<li><a href="#" class="menu-refresh">刷新</a></li>';
							html += '<li><a href="#" class="menu-other">关闭其他</a></li>';
							html += '</ul>';
							html += '</div>';

							html += '</li>';
							tab.append(html);
							var tabContent = $("#simle-tab_content");
							var inner = $.webapp.getInner();
							html = '<div class="tab-pane fade in active" id="'
									+ id + '">';
							html += '<iframe id="frame-' + id
									+ '" class="iframe-item" width="'
									+ (inner.width - 230) + '" height="'
									+ (inner.height - 92) + '" src="' + url
									+ '"></iframe>';
							html += '</div>';
							tabContent.append(html);
						}
					});
	$(document).on("click", ".simple-tabs-li .menu-close", function() {
		var obj = $(this).parents(".simple-tabs-li");
		var id = obj.attr("data-id");
		$("#" + id).remove();
		obj.remove();
		$(".simple-tabs-li").first().addClass("active");
		$("#simle-tab_content .tab-pane").first().addClass("active in");
	});
	$(document).on("click", ".simple-tabs-li .menu-other", function() {
		var obj = $(this).parents(".simple-tabs-li");
		var dataid = obj.attr("data-id");
		$(".simple-tabs-li").each(function() {
			var tdataid = $(this).attr("data-id");
			if (dataid != tdataid)
				$(this).remove();
		});
		$("#simle-tab_content .tab-pane").each(function() {
			var tdataid = $(this).attr("id");
			if (dataid != tdataid)
				$(this).remove();
		});
		$(".simple-tabs-li").first().addClass("active");
		$("#simle-tab_content .tab-pane").first().addClass("active in");
	});
	$(document).on("click", ".simple-tabs-li .menu-refresh", function() {
		var obj = $(this).parents(".simple-tabs-li");
		var id = obj.attr("data-id");
		$(".simple-tabs-li").removeClass("active");
		$("#simle-tab_content .tab-pane").removeClass("active");
		$("#" + id).addClass("active in");
		$("#li-" + id).addClass("active in");
		$("#frame-" + id).attr("src", $("#frame-" + id).attr("src"));
	});
};
/**
 * 菜单导航
 */
$.BOOT.navmenu = function(id, func) {
	$.BOOT.initmenu();
	var href = $.webapp.root + "/system/login/getCurrentAuthMenu.do";
	$.post(href, {
		d : new Date().getTime()
	}, function(result) {
		result = eval(result);
		var ul = $("<ul class='sidebar-menu'></ul>");
		var aTag = function(menu, pull) {
			var a = "<a href='javascript:;' id='menu-"+ menu.alias
			        + "' class='nav-menu' data-name='" + menu.text 
					+ "' data-id='" + menu.id + "' data-url='" + menu.href
					+ "'";
			a += " > <i class='fa ";
			a += menu.iconCls;
			a += "' style='color:";
			a += menu.color;
			a += ";'></i> <span>";
			a += menu.text;
			a += "</span>";
			if (cache_json[menu.alias])
				a += "<small class='label pull-right bg-green'>"
						+ cache_json[menu.alias] + "</small>";
			if (pull)
				a += "<i class='fa fa-angle-left pull-right'></i>";
			a += "</a>";
			return a;
		};
		var flag = "active";// active
		var recursion = function(data, parent) {
			for ( var d in data) {
				var menu = data[d];
				if (menu.children && menu.children.length > 0) {
					var li = $("<li class=' treeview'></li>");
					li.addClass(flag);
					flag = "";
					var ul = $("<ul class='treeview-menu'></ul>");
					$(li).append(aTag(menu, true)).append(ul).appendTo(parent);
					recursion(menu.children, ul);
				} else {
					$("<li class='treeview'></li>").append(aTag(menu, false))
							.appendTo(parent);
				}
			}
		};
		recursion(result[0].children, ul);
		for (var i = 0; i < 2; i++) {
			$("<li class='treeview'></li>").append(
					"<a href='#'><span>&nbsp;</span></a>").appendTo(ul);
		}
		var section = $("#" + id);
		section.append(ul);
		if (func) {
			func();
		}
	}, 'json');
};
var formjson = [];
/**
 * 表单验证
 */
$.BOOT.form = function(id, json, success, beforeinit) {
	try {
		if (formjson[id]) {
			return;
		}
		formjson[id] = true;
	} catch (e) {
		console.log(e);
	}
	$('#' + id).bootstrapValidator({
		fields : json
	}).on('success.form.bv', function(e) {
		e.preventDefault();
		if (beforeinit) {
			beforeinit();
		}
		var $form = $(e.target);
		success($form.serialize());
	});
};
/**
 * 加载内容
 */
$.BOOT.page = function(id, url, func) {
	var href = url;
	$("#" + id).empty().append(
			"<i class='fa fa-spinner fa-pulse' id='fa-pulse-id'><i>");
	$("#fa-pulse-id").css("margin-top", $.webapp.getHeight() / 2 - 150);
	$("#" + id).css("text-align", "center");
	$("body").removeClass('sidebar-open');
	$.post(href, {}, function(result) {
		var po = $("#" + id);
		po.css("text-align", "left");
		po.empty().append(result);
		if (func) {
			func();
		}
	}, 'html');
};
/**
 * 需要用这种绑定事件
 */
$.BOOT.click = function(selector, callback) {
	$(selector).each(function(c) {
		$(c).attr("onclick", "");
	});
	$(document).on("click", selector, function() {
		if ($.BOOT.canclick(this) && callback)
			callback(this);
	});
};

/**
 * 注销
 */
$.BOOT.logout = function(page) {
	$.post($.webapp.root + "/system/login/logout.do", null, function(result) {
		if (result.status) {
			window.location.replace($.webapp.root + '/' + page);
		} else {
			$.BOOT.toast(false, '安全退出失败，请联系管理员！');
		}
	}, 'json');
};
/**
 * 分组按钮
 */
$.BOOT.groupbtn1 = function(id, json) {
	var html = "";
	var back = true;
	for ( var d in json) {
		var data = json[d];
		if (data.type == "s") {
			html += "<li role='separator' class='divider'></li>";
		} else {
			html += "<li ";
			if (data.click) {
				html += " onclick=\"" + data.click + "\"";
			}
			html += "><a href='#'>" + data.text + "</a></li>";
			if (data.type == "ss") {
				back = false;
			}
		}
	}
	if (back) {
		if (json.length > 0)
			html += "<li role='separator' class='divider'></li>";
		html += "<li><a href='#' onclick='$.BOOT.history();'>后退</a></li>";
	}
	return html;
};
$.BOOT.groupbtn = function(id, json) {
	return "<div class='btn-group'>" + $.BOOT.groupbtn2(id, json) + "</div>";
};
$.BOOT.groupbtn2 = function(id, json) {
	var html = " <button type='button'  class='btn btn-default fa fa-cogs dropdown-toggle'";
	// event.stopPropagation();
	html += "data-toggle='dropdown' onclick=\"$('.dropdown-toggle').dropdown(); \" aria-haspopup='true' aria-expanded='false'>";
	html += " <span class='caret'></span> </button>";
	html += "<ul class='dropdown-menu dropdown-menu-right'>";
	for ( var d in json) {
		var data = json[d];
		if (data.type == "s") {
			html += "<li role='separator' class='divider'></li>";
		} else {
			html += "<li val='" + id + "' class='" + data.cla
					+ "'><a href='#'>" + data.text + "</a></li>";

		}
	}
	html += "</ul>";
	return html;
};

$.BOOT.simplebtn = function(id, json) {
	return "<div class='btn-group'>" + $.BOOT.simplebtn2(id, json) + "</div>";
};
$.BOOT.simplebtn2 = function(id, json) {
	var html = " <button type='button'  class='btn btn-default fa fa-cogs dropdown-toggle' disabled='disabled'";
	// event.stopPropagation();
	html += "data-toggle='dropdown'  aria-haspopup='true' aria-expanded='false'>";
	html += " <span class='caret'></span> </button>";
	return html;
};


/**
 * 获取值
 */
function getOptionValue(val, defVal) {
	if (val == undefined) {
		return defVal;
	}
	return val;
}
/**
 * 表格
 */
$.BOOT.table = function(id, href, opts) {
	if (opts.shows == undefined) {
		opts.shows = true;
	}
	var ps = {
		method : 'get',
		url : $.webapp.root + href,
		cache : getOptionValue(opts.cache, false),// 缓存
		striped : getOptionValue(opts.striped, true),// 使表格带有条纹
		pagination : getOptionValue(opts.pagination, true),// 显示分页工具栏
		pageSize : getOptionValue(opts.pageSize, 10),// 默认显示数量
		pageList : [ 10, 25, 50, 100, 200 ],
		showColumns : getOptionValue(opts.showColumns, opts.shows),// 显示列过滤
		showRefresh : getOptionValue(opts.showRefresh, opts.shows),// 显示查询
		showToggle : false,// 显示交换显示
		showExport : getOptionValue(opts.showExport, false),// 显示交换显示
		exportTypes : [ 'csv', 'excel' ],
		exportDataType : 'all',
		smartDisplay : getOptionValue(opts.smartDisplay, true),// 智能显示分页
		singleSelect : getOptionValue(opts.singleSelect, false),// 复选框只能选择一条记录
		cardView : getOptionValue(opts.cardView, false),// 卡片显示
		paginationInfo : getOptionValue(opts.paginationInfo, true),// 卡片显示
		paginationHAlign : getOptionValue(opts.paginationHAlign, 'right'),// 卡片显示
		minimumCountColumns : 2,
		height : getOptionValue(opts.height, 'auto'),// 表格高度
		columns : opts.columns,// 列字段,
		queryParams : function(params) {
			return params;
		},
		onDblClickRow:getOptionValue(opts.onDblClickRow, false),
		search : getOptionValue(opts.search, false),
		searchAlign : getOptionValue(opts.searchAlign, 'left')
	};
	if (opts.toolbar) {
		ps.toolbar = toolbar;
	}
	var table = $('#' + id).bootstrapTable(ps).on('load-success.bs.table',
			function(e, data) {
				if (opts.nodata) {
					$(".no-records-found td").each(function(i, c) {
						$(c).text(opts.nodata);
					});
				}
			}).on('page-change.bs.table', function(e, size, number) {
		if (opts.nodata) {
			$(".no-records-found td").each(function(i, c) {
				$(c).text(opts.nodata);
			});
		}
	});
	table.initRow = function($ele, val) {
		if (table.$last_ele != null) {
			$(table.$last_ele).css("background-color", "");
		}
		table.$last_ele = $ele;
		table.$value = val;
		$($ele).css("background-color", "#00a65a");
	};
	return table;
};
/**
 * 到指定位置
 */
$.BOOT.go = function(top) {
	if (!top) {
		try {
			top = $(this.currRow).offset().top - 200;
		} catch (e) {
		}
	}
	if (top > 0) {
		$('body').animate({
			scrollTop : top + 'px'
		}, 200);
	}
};
/**
 * 回到顶部
 */
$.BOOT.top = function(top) {
	$('html,body').animate({
		scrollTop : '0px'
	}, 200);
};
/**
 * 高亮表格当前行
 * 
 * @param c
 */
$.BOOT.selectRow = function($ele) {
	if (this.currRow != null) {
		$(this.currRow).css("background-color", "");
	}
	this.currRow = $ele;
	$($ele).css("background-color", "#00a65a");
};
$.BOOT.setCookie =function(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
};
//获取cookie
$.BOOT.getCookie = function(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
    }
    return "";
};
(function(window, $, undefined) {
	$.webapp = {};

	$.webapp.getHeight = function() {
		return this.getInner().height - $("#main-header").height();
	};

	/**
	 * 跨浏览器获取视口大小
	 */
	$.webapp.getInner = function() {
		if (typeof window.innerWidth != 'undefined') {
			return {
				width : window.innerWidth,
				height : window.innerHeight
			};
		} else {
			return {
				width : document.documentElement.clientWidth,
				height : document.documentElement.clientHeight
			};
		}
	};

	/**
	 * 跨浏览器获取Style
	 */
	$.webapp.getStyle = function(element, attr) {
		if (typeof window.getComputedStyle != 'undefined') {// W3C
			return window.getComputedStyle(element, null)[attr];
		} else if (typeof element.currentStyle != 'undeinfed') {// IE
			return element.currentStyle[attr];
		}
	};

	/**
	 * 将表达序列号成对象
	 */
	$.webapp.serializeObject = function(formId, flag) {
		var o = {};
		$.each($(formId).serializeArray(),
				function(index) {
					if (undefined == flag || flag == false) {
						if (this['value'] != undefined
								&& this['value'].length > 0) {
							if (o[this['name']]) {
								o[this['name']] = o[this['name']] + ","
										+ this['value'];
							} else {
								o[this['name']] = this['value'];
							}
						}
					} else {
						if (o[this['name']]) {
							o[this['name']] = o[this['name']] + ","
									+ this['value'];
						} else {
							o[this['name']] = this['value'];
						}
					}
				});
		return o;
	};

	/**
	 * 接受URL的参数
	 */
	$.webapp.request = {
		QueryString : function(val) {
			var uri = window.location.search;
			var re = new RegExp("" + val + "=([^&?]*)", "ig");
			return ((uri.match(re)) ? (uri.match(re)[0].substr(val.length + 1))
					: null);
		}
	};

})(window, jQuery);
String.prototype.toDate = function() {
	if (this == null) {
		return new Date();
	}
	return new Date(Date.parse(this.replace(/-/g, "/")));
};
Number.prototype.toRate = function(p) {
	var v = this * 100;
	v = v.toFixed(p);
	return v;
};
Date.prototype.Format = function(fmt, date) { // author: meizz
	var obj = this;
	if (date) {
		obj = date;
	}
	var o = {
		"M+" : obj.getMonth() + 1, // 月份
		"d+" : obj.getDate(), // 日
		"h+" : obj.getHours(), // 小时
		"m+" : obj.getMinutes(), // 分
		"s+" : obj.getSeconds(), // 秒
		"q+" : Math.floor((obj.getMonth() + 3) / 3), // 季度
		"S" : obj.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (obj.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
};