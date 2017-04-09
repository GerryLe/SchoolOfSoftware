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
    link.textContent = '需发送申请邮件给主管审批';
    tspan.appendChild(link);
    svg_label.appendChild(tspan);

    g.setNode(0,  { label: svg_label, labelType: 'svg' });
    g.setNode(1,  { label: "主管审批" });
    g.setNode(2,  { label: "转发邮件通知前台行政文员" });
    g.setNode(3,  { label: "前台行政文员向代理提供相关资料" });
    g.setNode(4,  { label: "代理排版后前台行发送邮件给同事对版核稿" });
    g.setNode(5,  { label: "确定核稿完成并安排印制名片" });
    g.setNode(6,  { label: "名片送到后，需有申请本人到前台签收领取" });
    
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
    edge_link.textContent = '通过审批';
    edge_tspan.appendChild(edge_link);
    svg_edge_2.appendChild(edge_tspan);
    
    svg_edge_6 = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    edge_tspan = document.createElementNS('http://www.w3.org/2000/svg','tspan');
    edge_tspan.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:space', 'preserve');
    edge_tspan.setAttribute('dy', '1em');
    edge_tspan.setAttribute('x', '1');
    edge_link = document.createElementNS('http://www.w3.org/2000/svg', 'a');
    edge_link.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', 'http://google.com/');
    edge_link.setAttribute('target', '_blank');
    edge_link.textContent = '需两至三天';
    edge_tspan.appendChild(edge_link);
    svg_edge_6.appendChild(edge_tspan);

    // 关联节点
    g.setEdge(0, 1);
    g.setEdge(1, 2, { labelType: "svg", label: svg_edge_2 });
    g.setEdge(2, 3);
    g.setEdge(3, 4);
    g.setEdge(4, 5);
    g.setEdge(5, 6, { labelType: "svg", label: svg_edge_6 });
   
    var render = new dagreD3.render();
    var svg = d3.select("svg"),
         svgGroup = svg.append("g");
    render(d3.select("svg g"), g);
    var xCenterOffset = (svg.attr("width") - g.graph().width) / 2;
    svgGroup.attr("transform", "translate(" + xCenterOffset + ", 20)");
    svg.attr("height", g.graph().height + 40);
</script>
