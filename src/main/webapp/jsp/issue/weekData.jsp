<%@ page language="java" import="java.util.*,com.state.util.VersionCtrl" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 

<!DOCTYPE html>
<html>
<head>
<title>导出周报信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style type="text/css">
.icon-copy {background-image: url(../img/icon/copy.gif) !important;}

</style>
<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/button.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/demo.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css"> 
<style type="text/css">
.table {
	margin: 0;
	padding: 0;
	border: 0px solid #dcdcdb;
	border-right: 1px solid #dcdcdb;
	border-bottom: 1px solid #dcdcdb;
	border-left: 0px;
}

.table th {
	height: 40px;
	border-left: 1px solid #dcdcdb;
	background-color: #ebebeb;
	color:#068F81;
}

.table tr {
	height: 30px;
}

.table td {
	border-left: 1px solid #dcdcdb;
	border-top: 1px solid #dcdcdb;
}

</style>
<!-- 引入 Bootstrap -->
<link href="${pageContext.request.contextPath }/bootstrap/css/bootstrap.min.css?v=<%=VersionCtrl.getVesrsion()%>" rel="stylesheet">
<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/common.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
<script src="${pageContext.request.contextPath }/js/jquery/jquery-1.8.3.min.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/common/common-public-1.0.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
<script src="${pageContext.request.contextPath }/js/My97DatePicker/WdatePicker.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
<script src="${pageContext.request.contextPath }/js/charts/highcharts.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
<script src="${pageContext.request.contextPath }/js/charts/modules/exporting.js"></script> 
<script src="${pageContext.request.contextPath }/js/state/issue/weekData.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
</head>
<body>

	<div class="box-body" style="margin-top: 10px; border: none;">
		<div style="margin-top: 0px;">
			 <div class="input-append" style="margin-top: 0px;text-align: center;">
		       <span>开始日期：<input type="text" class="Wdate" id="startTime" readonly="readonly" style="color:#0C0B0B;width: 110px;height:20px;" onfocus="WdatePicker({isShowWeek:true});" /></span>
		       <span>结束日期：<input type="text" class="Wdate" id="endTime" readonly="readonly" style="color:#0C0B0B;width: 110px;height:20px;" onfocus="WdatePicker({isShowWeek:true});" /></span>
		       <button type="button" id="selbtn" class="btn btn-default" style="padding:0px 15px;height:24px;margin-bottom:2px;" onclick="loadWeekData('older')">查询</button> 
		       <button type="button" id="selbtn" class="btn btn-default" style="padding:0px 15px;height:24px;margin-bottom:2px;" onclick="exportWeekReport()">导出周报</button> 
		       <button type="button" id="selbtn" class="btn btn-default" style="padding:0px 15px;height:24px;margin-bottom:2px;" onclick="exportWeekReportExcel()">导出申报情况</button> 
		       <button type="button" id="selbtn" class="btn btn-default" style="padding:0px 15px;height:24px;margin-bottom:2px;" onclick="loadWeekData('new')">重新计算</button> 
			</div>
		</div>
		<div id="content" class="form-actions" style="text-align: center;margin-top: 30px;">
			  
			<table>
				<tr>
					<td align="center" style="font-size: 25px"><span id="year"></span>年富余可再生能源跨省区现货市场</td>
				</tr>
				<tr>
					<td align="center" style="font-size: 20px">运行情况第<span name="weekNum"></span>周周报</td>
				</tr>
				<tr>
					<td align="left" style="font-size: 20px">一、跨区现货市场总体运行情况 </td>
				</tr>
				<tr>
					<td align="left" style="font-size: 15px">第<span name="weekNum"></span>周，跨区现货市场启动运行天数<span name="days"></span>天，跨区通道累计可用电量<span name="ablePower"></span>MWh，买方累计交易申报电量<span name="buySumElectricity"></span>MWh，卖方累计交易申报电量<span name="saleSumElectricity"></span>MWh，累计交易成交电量<span name="dealPower"></span>MWh，累计交易执行电量<span name="executePower"></span>MWh（模拟运行），跨区通道平均利用率<span name="alitAvg"></span>%。各跨区通道利用率如下图所示： </td>
				</tr>
				<tr>
					<td><div id="pictrue_cchart" style="width: 100%;height: 420px;margin: 0 auto;"></div></td>
				</tr>
				<tr>
					<td align="left" style="font-size: 20px">二、跨区现货市场交易报价情况 </td>
				</tr>
				<tr>
					<td align="left" style="font-size: 18px">1. 送端交易报价情况 </td>
				</tr>
				<tr>
					<td align="left" style="font-size: 15px">第<span name="weekNum"></span>周，累计受理送端交易单<span name="saleSum"></span>单，平均申报电力<span name="saleAvgPower"></span>MW，平均申报电价<span name="saleAvgPrice"></span>元/MWh。各送端交易成员报价情况如下表所示： </td>
				</tr>
				<tr>
					<td align="left" style="font-size: 18px"><div id="table1"></div></td>
				</tr>
				<tr>
					<td align="left" style="font-size: 18px">2.受端交易报价情况 </td>
				</tr>
				<tr>
					<td align="left" style="font-size: 15px">第<span name="weekNum"></span>周，累计受理受端交易单<span name="buySum"></span>单，平均申报电力<span name="buyAvgPower"></span>MW，平均申报电价<span name="buyAvgPrice"></span>元/MWh。各送端交易成员报价情况如下表所示： </td>
				</tr>
				<tr>
					<td align="left" style="font-size: 18px"><div id="table2"></div> </td>
				</tr>
				<tr>
					<td align="left" style="font-size: 18px">三、跨区现货市场交易成交情况 </td>
				</tr>
				<tr>
					<td align="left" style="font-size: 15px">第<span name="weekNum"></span>周，累计成交电量<span name="dealPower"></span>MWh，平均出清电价<span name="dealPrice"></span>元/MWh，各交易通道详细成交情况如下表所示：</td>
				</tr>
				<tr>
					<td align="left" style="font-size: 18px"><div id="table3"></div></td>
				</tr>
				<tr>
					<td align="left" style="font-size: 20px">四、跨区现货市场交易执行情况  </td>
				</tr>
				<tr>
					<td align="left" style="font-size: 18px">1. 交易执行总体情况 </td>
				</tr>
				<tr>
					<td align="left" style="font-size: 15px">第<span name="weekNum"></span>周，累计执行交易电量<span name="executePower"></span>MWh，交易执行完成率0%，原因是模拟运行，未实际投运。各通道执行情况如表所示： </td>
				</tr>
				<tr>
					<td align="left" style="font-size: 18px"><div id="table4"></div></td>
				</tr>
				<tr>
					<td align="left" style="font-size: 18px">2. 执行情况偏差分析</td>
				</tr>
				<tr>
					<td align="left" style="font-size: 18px"><div id="table5"></div></td>
				</tr>
			</table>
		</div>

	</div>

</body>
</html>