<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/template/modal.jsp"><jsp:param value="addEmploy" name="id" /><jsp:param
		value="申请表" name="title" /></jsp:include>
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
<svg id="svg-canvas" width=1300 height=600></svg>

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
    link.textContent = '当地招聘HR';
    tspan.appendChild(link);
    svg_label.appendChild(tspan);

    g.setNode(0,  { label: svg_label, labelType: 'svg' });
    g.setNode(1,  { label: "广州HR" });
    g.setNode(2,  { label: "Shadow>May>Ravic" });
    g.setNode(3,  { label: "当地HR电话通知候选人" });
    g.setNode(4,  { label: "当地HR发offer letter给候选人" });
    g.setNode(5,  { label: "当地HR发offer letter给候选人" });
    g.setNode(6,  { label: "当地HR邮件通知前程增人" });
    g.setNode(7,  { label: "发入职通知知会相关人员" });
    g.setNode(8,  { label: "1.录入staff DB 2.安排座位 3.准备入职资料（动态表、聘用通知书、职位申请书、保证书）" });
    g.setNode(9,  { label: "1.录入staff DB 2.安排座位 3.准备入职资料（动态表、聘用通知书、职位申请书、保证书、合同）" });
    g.setNode(10,  { label: "1.办理入职手续（入职资料两份） 2.熟悉场地 3.引领到座位" });
    g.setNode(11,  { label: "前程" });
    g.setNode(12,  { label: "1.办理入职手续（入职资料两份） 2.熟悉场地 3.引领到座位" });
    g.setNode(13,  { label: "签订前程合同" });
    g.setNode(14,  { label: "1.录入新员工资料（员工信息表、今目标） 2.员工资料扫描至广州备档" });
    
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
    edge_link.textContent = '邮件通知入选';
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
    edge_link.textContent = '邮件申请审批';
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
    edge_link.textContent = '通过审批';
    edge_tspan.appendChild(edge_link);
    svg_edge_3.appendChild(edge_tspan);
    
    svg_edge_4 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '外包';
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
    edge_link.textContent = '非外包';
    edge_tspan.appendChild(edge_link);
    svg_edge_5.appendChild(edge_tspan);
    
    svg_edge_8 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '外包';
    edge_tspan.appendChild(edge_link);
    svg_edge_8.appendChild(edge_tspan);
    
    svg_edge_9 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '非外包';
    edge_tspan.appendChild(edge_link);
    svg_edge_9.appendChild(edge_tspan);
    
    svg_edge_10 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '入职当天';
    edge_tspan.appendChild(edge_link);
    svg_edge_10.appendChild(edge_tspan);
    
    svg_edge_11 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '邮件资料';
    edge_tspan.appendChild(edge_link);
    svg_edge_11.appendChild(edge_tspan);
    
    svg_edge_12 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '通入职当天';
    edge_tspan.appendChild(edge_link);
    svg_edge_12.appendChild(edge_tspan);
    
    svg_edge_13 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '前程提供合同';
    edge_tspan.appendChild(edge_link);
    svg_edge_13.appendChild(edge_tspan); 

    g.setEdge(0, 1, { labelType: "svg", label: svg_edge_1 });
    g.setEdge(1, 2, { labelType: "svg", label: svg_edge_2 });
    g.setEdge(2, 3, { labelType: "svg", label: svg_edge_3 });
    g.setEdge(3, 4, { labelType: "svg", label: svg_edge_4 });
    g.setEdge(3, 5, { labelType: "svg", label: svg_edge_5 });
    g.setEdge(4, 6);
    g.setEdge(5, 7);
    g.setEdge(6, 7);
    g.setEdge(7, 8, { labelType: "svg", label: svg_edge_8 });
    g.setEdge(7, 9, { labelType: "svg", label: svg_edge_9 });
    g.setEdge(8, 10, { labelType: "svg", label: svg_edge_10 });
    g.setEdge(10, 11, { labelType: "svg", label: svg_edge_11 });
    g.setEdge(9, 12, { labelType: "svg", label: svg_edge_12 });
    g.setEdge(11, 13, { labelType: "svg", label: svg_edge_13 });
    g.setEdge(11, 14);
    g.setEdge(12, 14);
    // Create the renderer
    var render = new dagreD3.render();

    // Set up an SVG group so that we can translate the final graph.
    var svg = d3.select("svg"),
            svgGroup = svg.append("g");

    // Run the renderer. This is what draws the final graph.
    render(d3.select("svg g"), g);

    // Center the graph
    var xCenterOffset = (svg.attr("width") - g.graph().width) / 2;
    svgGroup.attr("transform", "translate(" + xCenterOffset + ", 20)");
    svg.attr("height", g.graph().height + 40);
</script>