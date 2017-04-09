<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/template/modal.jsp"><jsp:param value="addEmploy" name="id" /><jsp:param value="申请表" name="title" /></jsp:include>
<script src="${webRoot}/template/resource/js/d3js/dagre-d3.min.js"></script>
<script src="${webRoot}/template/resource/js/d3js/dagre-d3.js"></script>
<script src="${webRoot}/template/resource/js/d3js/demo.js"></script>
<script src="http://d3js.org/d3.v3.min.js" charset="utf-8"></script>
<style id="css">
  text {
        font-weight: 300;
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serf;
        font-size: 14px;
    }

    .node rect {
        stroke: #999;
        fill: #fff;
        stroke-width: 1.5px;
    }

    .edgePath path {
        stroke: #333;
        stroke-width: 1.5px;
    }
</style>

<svg id="svg-canvas" width=900  height=600></svg>

<section>
<p>
</section>

<script id="js">

 var g = new dagreD3.graphlib.Graph()
            .setGraph({})
            .setDefaultEdgeLabel(function() { return {}; });
    svg_label = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    tspan.setAttribute('dy', '1em');
    tspan.setAttribute('x', '1');
    link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    link.setAttribute('target', '_blank');
    link.textContent = '项目确定合作';
    tspan.appendChild(link);
    svg_label.appendChild(tspan);

    g.setNode(0,  { label: svg_label, labelType: 'svg' });
    g.setNode(1,  { label: "项目开启" });
    g.setNode(2,  { label: "PM发出招聘需求（填写人员需求表）" });
    g.setNode(3,  { label: "当地HR收到招聘需求后发出招聘申请（Shadow审批）" });
    g.setNode(4,  { label: "制定招聘计划" });
    g.setNode(5,  { label: "使用相关招聘渠道" });
    g.setNode(6,  { label: "收集简历，邀请面试" });
    g.setNode(7,  { label: "HR初试，项目负责人复试（填写面试申请表）" });
    g.setNode(8,  { label: "整理面试通过名单至项目和培训部" });
    g.setNode(9,  { label: "根据培训计划通知人员参加培训" });
    g.setNode(10,  { label: "人员参与培训" });
    g.setNode(11,  { label: "与培训部沟通招聘培训计划" });
    g.setNode(12,  { label: "整理面试人员名单录入人才库" });
    g.setNode(13,  { label: "入选面试结果反馈给项目和Vendor" });
    g.setNode(14,  { label: "回访面试缺席人员，短信通知面试通过人员" });
    g.setNode(15,  { label: "当天完成" });
    
    g.nodes().forEach(function(v) {
        var node = g.node(v);
        node.rx = node.ry = 5;
    });

    svg_edge_4 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '两天内';
    edge_tspan.appendChild(edge_link);
    svg_edge_4.appendChild(edge_tspan);
     
    svg_edge_5 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '两天内';
    edge_tspan.appendChild(edge_link);
    svg_edge_5.appendChild(edge_tspan);
   

    // 关联节点
    g.setEdge(0, 1);
    g.setEdge(1, 2);
    g.setEdge(2, 3);
    g.setEdge(3, 4, { labelType: "svg", label: svg_edge_4 });
    g.setEdge(4, 5, { labelType: "svg", label: svg_edge_5 });
    g.setEdge(5, 6);
    g.setEdge(6, 7);
    g.setEdge(7, 8);
    g.setEdge(8, 9);
    g.setEdge(9, 10);
    g.setEdge(4, 11);
    g.setEdge(7, 15);
    g.setEdge(15, 12);
    g.setEdge(15, 13);
    g.setEdge(15, 14);
    
   
    var render = new dagreD3.render();
    var svg = d3.select("svg"),
         svgGroup = svg.append("g");
    render(d3.select("svg g"), g);
    var xCenterOffset = (svg.attr("width") - g.graph().width) / 2;
    svgGroup.attr("transform", "translate(" + xCenterOffset + ", 20)");
    svg.attr("height", g.graph().height + 40);
</script>
