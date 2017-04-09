<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="net.sf.json.JSONObject;"  %>
<!-- Content Header (Page header) -->
<link rel="stylesheet" href="${webRoot}/template/resource/usermenu/base.css" />
<link rel="stylesheet" href="${webRoot}/template/resource/usermenu/Spacetree.css" />
<script src="${webRoot}/template/resource/usermenu/excanvas.js"></script>
<script src="${webRoot}/template/resource/usermenu/jit.js"></script>
<section class="content">
	<div id="menu-form-id"></div>
	<div id="treeview1" class=""></div>
<div id="infovis"  style="width:100%;height:800px;background:#edf2f9"></div>
 <div id="continer"></div>
</section>
<script type="text/javascript">
	
	 $(function() {
	var href = $.webapp.root + "/admin/system/org/getUserCurrentAuthMenu.do";
	$.post(href, {
		d : new Date().getTime()
	}, function(result) {
		maptree(result);
	}, 'json');
	});
	// var json7={"id":0,"parentId":0,"text":"laoban","name":"老板","children":[{"id":777,"parentId":0,"name":"xx部门","children":[{"id":783,"parentId":777,"name":"xx部门","children":[{"id":785,"parentId":783,"name":"xx部门"},{"id":786,"parentId":783,"name":"xx部门"}]},{"id":784,"parentId":777,"name":"xx部门","children":[]},{"id":792,"parentId":777,"name":"xx部门","children":[]},{"id":793,"parentId":777,"name":"xx部门","children":[{"id":794,"parentId":793,"name":"xx部门"}]}]},{"id":797,"parentId":0,"name":"xx部门","children":[{"id":798,"parentId":797,"name":"xx部门","children":[{"id":799,"parentId":798,"name":"xx部门"}]},{"id":802,"parentId":797,"name":"xx部门","children":[]}]},{"id":800,"parentId":0,"name":"xx部门","children":[]},{"id":803,"parentId":0,"name":"xx部门","children":[]}]};
     
	function maptree(json) {
        //创建一个 ST 实例
        var st = new $jit.ST({
            //渲染容器，DIV
            injectInto: 'infovis',
            //展示出拓扑图的动画时间
            duration: 300,
            //切换为TOP模式
            orientation: 'top',
            verticalAlign : 'top',
            //multitree:'top',
            //设置动画类型
            transition: $jit.Trans.Quart.easeInOut,
            //设置横向节点距离
            levelDistance: 80,
            //点击节点后下级几点展示几层
            levelsToShow: 4,
            //是否可以移动
            Navigation: {
                enable: true,
                panning:true
            },
            //节点类型样式设置，节点类型支持扩展
            Node: {
                height: 60,
                width: 80,
                type: 'rectangle',
                lineWidth: 2,
                overridable: true
            },
            //线条类型(bezier,line,arrow,quadratic:begin,quadratic:end)样式设置
            Edge: {
                type: 'bezier',
                lineWidth: 2,
                color: '#203b68',
                overridable: true
            },

            //计算节点位置前
            onBeforeCompute: function (node) {

            },
            //计算节点位置后
            onAfterCompute: function () {

            },

            //创建节点文本时触发事件
            onCreateLabel: function (label, node) {
                label.id = node.id;
                label.innerHTML = node.name;
                label.onclick = function () {
                    st.onClick(node.id);
                };
                var style = label.style;
                style.width = 80 + 'px';
                style.height = 60 + 'px';
                //style.lineHeight= 40 + 'px'; //设置文字行高
                style.cursor = 'pointer';
                style.color = '#000000';//'#c5c4c4';
                style.fontSize = '1.0em';
                style.textAlign = 'center';
                style.verticalAlign = 'top';//center、top、bottom
                //style.borderRadius=30+'px';
                style.background="url('/template/resource/images/meet3.jpg') no-repeat top";//添加节点背景图片
                //style.background=node.data.type=='F5'?"url('./icon/f5.png') no-repeat top":"url('./icon/server.png') no-repeat top";
            },


            //渲染节点触发事件
            onBeforePlotNode: function (node) {

            },


            //连线前触发事件，可以修改线条颜色等
            onBeforePlotLine: function (adj) {
                if (adj.nodeFrom.selected && adj.nodeTo.selected) {
                    adj.data.$color = "#203b68";
                    adj.data.$lineWidth = 5;
                }
                else {
                    delete adj.data.$color;
                    delete adj.data.$lineWidth;
                }
            }
        });

        //加载数据
        st.loadJSON(json);
        //计算布局
        st.compute();
        //整体渲染
        st.geom.translate(new $jit.Complex(-200, 0), "current");
        //展现根节点
        st.onClick(st.root);
    }

	
	
</script>