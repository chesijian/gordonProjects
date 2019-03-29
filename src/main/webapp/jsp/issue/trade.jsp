<%@ page language="java" import="java.util.*,com.state.util.AuthoritiesUtil,com.state.util.VersionCtrl" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title></title>

	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/declare.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
    <Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/head.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css"> 
    <Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/ui-dialog.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/common.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css"> 
    <link href="${pageContext.request.contextPath }/bootstrap/css/bootstrap-btn.css?v=<%=VersionCtrl.getVesrsion()%>" rel="stylesheet">  
    <Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/zTreeStyle.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	<style type="text/css">  
		a:hover {
		    color: #ffa110 !important;
		}
		.pdl10 {
		    padding: 10px 10px;
		}
		.lmenu {
		    float: left;
		    width: 200px;
		    border: solid 1px #dcdcdb;
		    font-size: 14px;
		    border-right: 0px;
		    /* padding-bottom: 50px; */
		    overflow: auto;
		    height: 570px;
		    border-top-style: none;
	    }
		.bd1 {
		    border: solid 1px #dcdcdb;
		   /*  overflow:auto;width: 1190px;height: 570px;*/
		    border-top: none;
		    margin-bottom: 10px;
		    margin-bottom: 10px;
		    width: 1201px;
		    
		}
		.headline {
		    width: 1110px;
		    margin-top: 20px;
		     /* float:left;*/
		    text-align: left;
		    font-size: 18px;
		    font-weight: 600;
		}
		.content {
		    float:left;
		    margin-top: 5px;
		margin-bottom: 5px;
		    font-size: 17px;
		    font-weight: 600;
		}
		td{
		text-align: center;
		}
    </style> 
	<script src="${pageContext.request.contextPath }/js/jquery/jquery-1.8.3.min.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/common/common-public-1.0.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/charts/highcharts.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
    <script src="${pageContext.request.contextPath }/js/state/jqueryFormat.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
    <script src="${pageContext.request.contextPath }/js/state/jquery.ztree.core.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
	<script src="${pageContext.request.contextPath }/js/state/issue/trade.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	
	<script type="text/javascript">
		var trade = new trade();
		$(function(){
			trade.getTradeDatas();
			//$("#treeDemo li").live('click', function(){trade.getTradeDatas($(this));});
		});
	</script>
</head>
<body>	
	<div style="text-align: center">
		<div>
			<div>
				
			    <div id="dailyDataDiv" class="fl bd1" style="border-right:0px;">

					<div class="cl"></div>
					<div class="fl pdl10">
						<div align="center" style="width: 1181px;float: left;">
							<span style="font-size: 26px;font-weight: bold; ">富余可再生能源跨省区现货市场<span id="dateStr"></span>交易单</span>
						</div>
						<div>
						  <div class="headline">一、申报数据</div>
						  <div align="left" id="declareData">
						  	
						  </div>
						</div>
						
						<div >
						  <div class="headline">二、成交数据</div>
						  <div align="left" id="dealData">
						  
						  </div>
						</div>
						<div class="cl"></div>
						<div >
						  <div class="headline">三、通道成交结果</div>
						  <div align="left" id="pathData">
						  
						  </div>
						</div>
						<div >
						  <div class="headline">四、执行结果</div>
						  <div align="left" id="executeData">
						  
						  </div>
						</div>
					</div>
					<div class="cl"></div>
				</div>
			</div>
		</div>
	</div>
</body>	
<script type="text/javascript">
function zTreeOnClick(event, treeId, treeNode) {
	//var treeName=treeNode.name;
	
	//如果叶子则展示数据
	if(treeNode.id.charAt(0) != '-'){
		daily.getResultData(treeNode.id,treeNode);
		
	}else{
		//如果点击的是文件夹，则展开所有子节点
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		treeObj.expandNode(treeNode, true, true, true);
	}
	
};
</script>
</html>
