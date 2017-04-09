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
<svg id="svg-canvas" width=800  height=600></svg>
</div>
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
    link.textContent = '员工离职';
    tspan.appendChild(link);
    svg_label.appendChild(tspan);

    g.setNode(0,  { label: svg_label, labelType: 'svg' });
    g.setNode(1,  { label: "部门离职面谈" });
    g.setNode(2,  { label: "部门负责人审批" });
    g.setNode(3,  { label: "Shadow>May>Ravic审批" });
    g.setNode(4,  { label: "请示邮箱处理" });
    g.setNode(5,  { label: "邮件通知前程减员" });
    g.setNode(6,  { label: "HR准备离职资料（动态表、离职申请书、辞职书）" });
    g.setNode(7,  { label: "HR准备离职资料（动态表、离职申请书、前程&餐道辞职书）" });
    g.setNode(8,  { label: "离职员工填写《离职报告》" });
    g.setNode(9,  { label: "离职面谈" });
    g.setNode(10,  { label: "1.staff DB  2.办理离职手续（工作、物资等交换）" });
    g.setNode(11,  { label: "开具离职证明（有需要的开具公积金提取表）" });
    g.setNode(12,  { label: "通知前程开具相关证明文件" });
    g.setNode(13,  { label: "邮件辞职书" });
    g.setNode(14,  { label: "MIS删除或保留邮箱" });
    
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
    edge_link.textContent = '正式员工提前1个月邮件申请 试用期内、实习员工提前2周邮件申请';
    edge_tspan.appendChild(edge_link);
    svg_edge_1.appendChild(edge_tspan);
     
    svg_edge_3 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '主管以级别离职 通过审批';
    edge_tspan.appendChild(edge_link);
    svg_edge_3.appendChild(edge_tspan);
    
    svg_edge_24 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '一般员工 通过审批';
    edge_tspan.appendChild(edge_link);
    svg_edge_24.appendChild(edge_tspan);
    
    svg_edge_34 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '通过审批';
    edge_tspan.appendChild(edge_link);
    svg_edge_34.appendChild(edge_tspan);
    
    svg_edge_5 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '外包';
    edge_tspan.appendChild(edge_link);
    svg_edge_5.appendChild(edge_tspan);
    
    svg_edge_6 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '非外包';
    edge_tspan.appendChild(edge_link);
    svg_edge_6.appendChild(edge_tspan);
    
    svg_edge_10 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '离职当前';
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
    edge_link.textContent = '非外包';
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
    edge_link.textContent = '外包 发送邮件';
    edge_tspan.appendChild(edge_link);
    svg_edge_12.appendChild(edge_tspan);
    
    svg_edge_14 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '邮箱处理  HR发邮件';
    edge_tspan.appendChild(edge_link);
    svg_edge_14.appendChild(edge_tspan);
    

    // 关联节点
    g.setEdge(0, 1, { labelType: "svg", label: svg_edge_1 });
    g.setEdge(1, 2);
    g.setEdge(2, 3, { labelType: "svg", label: svg_edge_3 });
    g.setEdge(3, 4, { labelType: "svg", label: svg_edge_34 });
    g.setEdge(2, 4, { labelType: "svg", label: svg_edge_24 });
    g.setEdge(4, 5, { labelType: "svg", label: svg_edge_5 });
    g.setEdge(4, 6, { labelType: "svg", label: svg_edge_6 });
    g.setEdge(5, 7);
    g.setEdge(6, 8);
    g.setEdge(7, 8);
    g.setEdge(8, 9);
    g.setEdge(9, 10, { labelType: "svg", label: svg_edge_10 });
    g.setEdge(10, 11, { labelType: "svg", label: svg_edge_11 });
    g.setEdge(10, 12, { labelType: "svg", label: svg_edge_12 });
    g.setEdge(12, 13);
    g.setEdge(11, 14, { labelType: "svg", label: svg_edge_14 });
    g.setEdge(13, 14);
   
    var render = new dagreD3.render();
    var svg = d3.select("svg"),
         svgGroup = svg.append("g");
    render(d3.select("svg g"), g);
    var xCenterOffset = (svg.attr("width") - g.graph().width) / 2;
    svgGroup.attr("transform", "translate(" + xCenterOffset + ", 20)");
    svg.attr("height", g.graph().height + 40);
</script>
