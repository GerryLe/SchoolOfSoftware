<%@page import="com.rosense.basic.util.LangUtils"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<div class="row">
<svg id="svg-canvas" width=900  height=600></svg>
</div>
<section>
	<p>
</section>

<script>
$(function(){
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
    link.textContent = '员工离职';
    tspan.appendChild(link);
    svg_label.appendChild(tspan);

    g.setNode(0,  { label: svg_label, labelType: 'svg' });
    g.setNode(1,  { label: "部门负责人审批" });
    g.setNode(2,  { label: "Shadow>May>Ravic审批" });
    g.setNode(3,  { label: "HR录入离职日期，档案调设至离职状态" });
    g.setNode(4,  { label: "归档（HR后台可查看）" });
    g.setNode(5,  { label: "Shadow" });
    
    g.nodes().forEach(function(v) {
        var node = g.node(v);
        node.rx = node.ry = 5;
    });

    svg_edge_2 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '主管以上级别离职';
    edge_tspan.appendChild(edge_link);
    svg_edge_2.appendChild(edge_tspan);
     
    svg_edge_23 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '通过审批';
    edge_tspan.appendChild(edge_link);
    svg_edge_23.appendChild(edge_tspan);
    
    svg_edge_5 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '抄送May&Ravic 一般员工';
    edge_tspan.appendChild(edge_link);
    svg_edge_5.appendChild(edge_tspan);
    
    svg_edge_53 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '通过审批';
    edge_tspan.appendChild(edge_link);
    svg_edge_53.appendChild(edge_tspan);
   

    g.setEdge(0, 1);
    g.setEdge(1, 2, { labelType: "#svg-canvas", label: svg_edge_2 });
    g.setEdge(2, 3, { labelType: "#svg-canvas", label: svg_edge_23 });
    g.setEdge(3, 4);
    g.setEdge(1, 5, { labelType: "#svg-canvas", label: svg_edge_5 });
    g.setEdge(5, 3, { labelType: "#svg-canvas", label: svg_edge_53 });
    // Create the renderer
    var render = new dagreD3.render();

    // Set up an SVG group so that we can translate the final graph.
    var svg = d3.select("#svg-canvas"),
            svgGroup = svg.append("g");

    // Run the renderer. This is what draws the final graph.
    render(d3.select("#svg-canvas g"), g);

    // Center the graph
    var xCenterOffset = (svg.attr("width") - g.graph().width) / 2;
    svgGroup.attr("transform", "translate(" + xCenterOffset + ", 20)");
    svg.attr("height", g.graph().height + 40);
});
   // alert(document.getElementById("svg-canvas").innerHTML);
</script>