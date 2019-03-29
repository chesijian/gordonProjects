<%@ page language="java"
	import="java.util.*,com.state.util.AuthoritiesUtil,com.state.util.VersionCtrl"
	pageEncoding="UTF-8" contentType="text/html; charset=utf-8"%>

<title></title>
<head>
<!-- 引入 Bootstrap -->
<script
	src="${pageContext.request.contextPath }/js/jquery/jquery-1.10.2.js?v=<%=VersionCtrl.getVesrsion()%>"
	type="text/javascript"></script>
<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/declare.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	
<link
	href="${pageContext.request.contextPath }/bootstrap/css/bootstrap.min.css?v=<%=VersionCtrl.getVesrsion()%>"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath }/bootstrap/plugin/js/table/bootstrap-table.min.css?v=<%=VersionCtrl.getVesrsion()%>"
	rel="stylesheet" />
	<style type="text/css">
	body,td{
font-size:11pt;
font-family:微软雅黑,宋体;
}
.th-inner {
    padding: 8px;
    line-height: 24px;
    vertical-align: top;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    font-size:10pt;
}
	</style>
<script
	src="${pageContext.request.contextPath }/bootstrap/js/bootstrap.min.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
<script
	src="${pageContext.request.contextPath }/bootstrap/plugin/js/table/bootstrap-table.min.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
<script
	src="${pageContext.request.contextPath }/bootstrap/plugin/js/table/bootstrap-table-zh-CN.min.js?v=<%=VersionCtrl.getVesrsion()%>"></script>

<script
	src="${pageContext.request.contextPath }/js/state/issue/clear.js?v=<%=VersionCtrl.getVesrsion()%>"></script>

<script type="text/javascript">
	
</script>
</head>
<body>


	<div id="myTabContent" class="tab-content" style="width: 1200px">
<div style="text-align: right;margin-top: 10px;margin-bottom: 10px;"><span>
<c:if test='<%=AuthoritiesUtil.isAllow("is_btn_ExportOut") %>'>
<a class='btn1' style="margin-right: 13px;" href='#' onclick='exportExcel();'>*导出</a>
</c:if>
</span></div>
				
		<table class="table table-hover" id="clearData" style="height: 110px;">

		<thead>
            <tr>
                
               <th data-field="Number"  style="min-width:15px; ">序号</th>
	                <th data-field="Interval" style="min-width:15px;">时段数</th>
	                <th data-field="SellerArea" >卖单区域</th>
	                <th data-field="Sellername" >卖单名称</th>
	                <th data-field="Sellersection" >卖单段数</th>
	                <th data-field="power_i" >申报单剩余电力</th>
	                <th data-field="price_i" >申报电价</th>
	                <th data-field="clearpower_i" >送端出清电力</th>
	                <th data-field="clearprice_i" >送端出清电价</th>
	                <th data-field="BuyerArea">买单区域</th>
	                <th data-field="Buyername" style="min-width:130px;width:130px;">买单名称</th>
	                <th data-field="Buyersection" >买单段数</th>
	                <th data-field="power_j" data-formatter="test(value);">申报剩余电力</th>
	                <th data-field="price_j">申报电价</th>
	                <th data-field="trans_loss">网损</th>
	                <th data-field="tanspower_j">受端电力</th>
	                <th data-field="clearpower_j" >受端出清电力</th>
	                <th data-field="clearprice_j">受端出清电价</th>
	                <th data-field="Pricediff" >价差</th>
	                <th data-field="SellCost" >送端费用</th>
	                <th data-field="TranCost" >输电费用</th>
	                <th data-field="LossCost" >网损折价</th>
	                <th data-field="BuyCost" >受端费用</th>
	                <th data-field="LineCost" >分段费用</th>
	                <th data-field="LineLossCost" >分段折价</th>
	                <th data-field="LinePower" >分段电力</th>
	                <th data-field="Trans_Path" style="min-width:230px;width:230px;">通道路径</th>
	                <th data-field="Corridorpower" >通道剩余容量</th>
	                <th data-field="Pri" >优先级</th>
            </tr>
            </thead>
		</table>


	</div>


</body>

</html>

