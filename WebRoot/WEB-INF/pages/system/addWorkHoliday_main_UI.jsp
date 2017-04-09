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

<svg id="svg-canvas" width=900 height=600></svg>

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
    link.textContent = '进入OA系统申请假期/OT界面';
    tspan.appendChild(link);
    svg_label.appendChild(tspan);

    g.setNode(0,  { label: svg_label, labelType: 'svg' });
    g.setNode(1,  { label: "查询假期" });
    g.setNode(2,  { label: "申请假期类型" });
    g.setNode(3,  { label: "由系统发送邮件通知上级及HR" });
    g.setNode(4,  { label: "上级审批" });
    g.setNode(5,  { label: "填写加班日期及时间" });
    g.setNode(6,  { label: "由系统发送邮件通知上级及HR" });
    g.setNode(7,  { label: "上级审批" });
    g.setNode(8,  { label: "HR归档" });
    
    g.nodes().forEach(function(v) {
        var node = g.node(v);
        node.rx = node.ry = 5;
    });

    svg_edge_1 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '申请假期';
    edge_tspan.appendChild(edge_link);
    svg_edge_1.appendChild(edge_tspan);
     
    svg_edge_2 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '包括年假、司龄假、补休';
    edge_tspan.appendChild(edge_link);
    svg_edge_2.appendChild(edge_tspan);
    
    svg_edge_3 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '年假、司龄假、补休、婚假、产假、陪产假、其他';
    edge_tspan.appendChild(edge_link);
    svg_edge_3.appendChild(edge_tspan);
    
    svg_edge_48 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '通过审批';
    edge_tspan.appendChild(edge_link);
    svg_edge_48.appendChild(edge_tspan);
    
    svg_edge_5 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '申请OT';
    edge_tspan.appendChild(edge_link);
    svg_edge_5.appendChild(edge_tspan);

    svg_edge_78 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '通过审批';
    edge_tspan.appendChild(edge_link);
    svg_edge_78.appendChild(edge_tspan);
    
    // 关联节点
    g.setEdge(0, 1, { labelType: "svg", label: svg_edge_1 });
    g.setEdge(1, 2, { labelType: "svg", label: svg_edge_2 });
    g.setEdge(2, 3, { labelType: "svg", label: svg_edge_3 });
    g.setEdge(3, 4);
    g.setEdge(4, 8, { labelType: "svg", label: svg_edge_48 });
    g.setEdge(0, 5, { labelType: "svg", label: svg_edge_5 });
    g.setEdge(5, 6);
    g.setEdge(6, 7);
    g.setEdge(7, 8, { labelType: "svg", label: svg_edge_78 });
    
    var render = new dagreD3.render();
    var svg = d3.select("svg"),
         svgGroup = svg.append("g");
    render(d3.select("svg g"), g);
    var xCenterOffset = (svg.attr("width") - g.graph().width) / 2;
    svgGroup.attr("transform", "translate(" + xCenterOffset + ", 20)");
    svg.attr("height", g.graph().height + 40);
</script>
