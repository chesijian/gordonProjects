<%@ page language="java" import="java.util.*,com.state.util.AuthoritiesUtil,com.state.util.VersionCtrl" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.state.com/state" prefix="state"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<state:override name="head">
	<title></title>
	<script src="${pageContext.request.contextPath }/js/state/jquery.ztree.core.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
    <Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/zTreeStyle.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
    <Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/issue.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css"> 
    <style>
		.lmenu {
	padding-bottom:50px;
	overflow:auto;
	float: left;
	width: 220px;
	border: solid 1px #dcdcdb;
	font-size: 14px;
	border-top: 0px;
	height:482px;
}

	</style>
	<script src="${pageContext.request.contextPath }/js/charts/highcharts.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/state/jqueryFormat.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<!-- 
	<script src="${pageContext.request.contextPath }/js/charts/modules/exporting.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	 -->
	<script src="${pageContext.request.contextPath }/js/state/settleMent.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script type="text/javascript">
		var issue = new Issue();
		$(function(){
			$('.menu1').find('a[name=计量清算]').attr('class', 'menufocus');
			var area = $('#area').val();
			if(<%=AuthoritiesUtil.isAllow_Issue_Btn() %>){
				// var content="<span><a class='btn1' href='#' onclick='issue.issueData();'>出清审核</a></span><span><a class='btn1' href='#' onclick='issue.jsData();'>结算审核</a></span>";
				 //$('#lbtn').append(content);
				 //var content1="<div class='fl mne'><a href='${pageContext.request.contextPath }/match/init' name='输电通道'>输电通道</a></div>";
				 //var content1="<div class='fl mne'><a id='a_3' href='javascript:;' onclick='issue.loadTree(3);return false;' name='出清信息'>出清过程</a></div>";
				 //var content2="<div class='fl mne'><a id='a_2' href='javascript:;' onclick='issue.loadTree(2,\"pathResult\");return false;' name='输电通道'>费用信息</a></div>";
				 //var content5="<div class='fl mne'><a id='a_5' href='javascript:;' onclick='issue.loadTree(5,\"pathResult\");return false;' name='通道信息'>通道信息</a></div>";
				 //var content3="<div class='fl mne'><a id='a_4' href='javascript:;' onclick='issue.loadTree(4);return false;' name='地图展示'>地图展示</a></div>";
				 
				 //$('#limitconrightt').append(content1);
				 //$('#limitconrightt').append(content2);
				 //$('#limitconrightt').append(content5);
				 //$('#limitconrightt').append(content3);
			}
			
			//issue.loadTree(2,"result");
			issue.loadTree(2,"pathResult")
		});
	</script>
</state:override>
<state:override name="content">			
	<div>
	    <div style="text-align: center">
	    <div id="limitconrightt" class="limitconrightt" style="text-align: center;width:1200px;margin: auto;">
	    		<c:if test='<%=AuthoritiesUtil.isAllow("Li_Menu_Tdxe") %>'>
						<div class="fl mne"><a href="${pageContext.request.contextPath }/metre/init" name="计量数据" >计量数据</a></div>
				</c:if>
				<c:if test='<%=AuthoritiesUtil.isAllow("Li_Menu_Tdxe") %>'>
						<div class="fl mne"><a href="${pageContext.request.contextPath }/settleMent/init" name="清算数据"  style="color:#D1B664;fontWeight:bold">清算数据</a></div>
				</c:if>
		<div id="btngetPlan" class="rlplan">
		</div>
	</div>
	    <%-- <div id="limitconrightt" class="limitconrightt" style="text-align: center;width:1200px;margin: auto;">
			  <c:if test='<%=AuthoritiesUtil.isAllow("Issue_Menu_fbxx") %>'>
				<div class="fl mne"><a id="a_1" href='javascript:;' onclick='issue.loadTree(1,"result");return false;' name="发布信息" style="color:#D1B664;fontWeight:bold">发布信息11</a></div>
			  </c:if>
			  <c:if test='<%=AuthoritiesUtil.isAllow("Issue_Menu_Clear") %>'>
				<div class='fl mne'><a id='a_3' href='javascript:;' onclick='issue.loadTree(3);return false;' name='出清信息'>出清过程</a></div>
			  </c:if>
			  <c:if test='<%=AuthoritiesUtil.isAllow("Issue_Menu_Cost") %>'>
				<div class='fl mne'><a id='a_2' href='javascript:;' onclick='issue.loadTree(2,"pathResult");return false;' name='输电通道'>费用信息</a></div>
			  </c:if>
			  <c:if test='<%=AuthoritiesUtil.isAllow("Issue_Menu_Map") %>'>
				<div class='fl mne'><a id='a_4' href='javascript:;' onclick='issue.loadTree(4);return false;' name='地图展示'>地图展示</a></div>
			  </c:if>
			  
			  <div id="btngetPlan" class="rlplan" style="padding-right: 3px;">
			  <c:if test='<%=AuthoritiesUtil.isAllow("Issue_Btn_cqsh") %>'>
					<a class='btn1' style='margin:0px;color:#06938e;' href='#' onclick='issue.issueData();'>出清审核</a>
				</c:if>
				<c:if test='<%=AuthoritiesUtil.isAllow("Issue_Btn_Export") %>'>
					<a class='btn1' style='margin-right:0px;color:#06938e;' href='#' onclick='issue.exportData();'>导出</a>
				</c:if>
				<c:if test='<%=AuthoritiesUtil.isAllow("Issue_Btn_ExportLocal") %>'>
					<a class='btn1' style='margin-right:10px;color:#06938e;' href='#' onclick='issue.localExportData();'>本地导出</a>
				</c:if>
				<c:if test='<%=AuthoritiesUtil.isAllow("Issue_Btn_ExportDaily") %>'>
					<a class='btn1' style='margin:0px;color:#06938e;' href='#' onclick='issue.exportExcel();'>导出日报</a>
				</c:if>
				</div>
		</div> --%>
		<div class="mid" id="result">
			<div class="contop" style="padding:0px 10px;">
				<!-- <div class="fl"><span class="xmenu">发布</span></div> -->
				<div id="lbtn" class="rl"><!-- <span><a class="btn1" href="#" onclick="issue.issueData();">出清审核</a></span><span><a class="btn1" href="#" onclick="issue.issueData();">结算审核</a></span> --></div>
				<div class="cl"></div>
			</div>
			<div>
			    
			    <div class="lmenu zTreeDemoBackground left">
					<ul id="treeDemo"  class="ztree">
						
					</ul>
				</div>
				
				<div id="IssueDataDiv" class="fl bd1" style="padding-top:10px;;border:solid 0px;width:968px;">
					
					<div class="fl conrightt2" style="width:928px;text-align: left;padding-top: 0px;display:none;"><span>总电量:</span><span name="sumValue" class="avenum">0</span><span class="avenum">&nbsp;MWh</span>|<span class="pdl30">平均电力:</span><span name="avgValue" class="avenum">0</span><span class="avenum">&nbsp;MW</span></div>
					
					<div class="fl pdl10">
						<table width="968" height="302" cellpadding="0" cellspacing="0">
							<thead>
								<th width="10%" class="cgr">时刻</th>
								<th width="7%" class="cgr">0时</th>
								<th width="7%" class="cgr">1时</th>
								<th width="7%" class="cgr">2时</th>
								<th width="7%" class="cgr">3时</th>
								<th width="7%" class="cgr">4时</th>
								<th width="7%" class="cgr">5时</th>
								<th width="7%" class="cgr">6时</th>
								<th width="7%" class="cgr">7时</th>
								<th width="7%" class="cgr">8时</th>
								<th width="7%" class="cgr">9时</th>
								<th width="7%" class="cgr">10时</th>
								<th width="7%" class="cgr">11时</th>
							</thead>
							<tr>
								<td width="10%" class="cgr">15分</td>
								<td width="7.5%"><input readonly="true" name="h01" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h05" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h09" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h13" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h17" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h21" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h25" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h29" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h33" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h37" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h41" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h45" class="datainput"></td>
							</tr>
							<tr class="bgh">
								<td width="10%" class="cgr">30分</td>
								<td width="7.5%"><input readonly="true" name="h02" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h06" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h10" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h14" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h18" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h22" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h26" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h30" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h34" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h38" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h42" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h46" class="datainput"></td>
							</tr>
							<tr>
								<td width="10%" class="cgr">45分</td>
								<td width="7.5%"><input readonly="true" name="h03" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h07" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h11" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h15" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h19" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h23" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h27" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h31" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h35" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h39" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h43" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h47" class="datainput"></td>
							</tr>
							<tr class="bgh">
								<td width="10%" class="cgr">00分</td>
								<td width="7.5%"><input readonly="true" name="h04" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h08" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h12" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h16" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h20" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h24" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h28" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h32" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h36" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h40" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h44" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h48" class="datainput"></td>
							</tr>
							<tr>
								<td width="10%" class="cgr">时刻</td>
								<td width="7%" class="cgr">12时</td>
								<td width="7%" class="cgr">13时</td>
								<td width="7%" class="cgr">14时</td>
								<td width="7%" class="cgr">15时</td>
								<td width="7%" class="cgr">16时</td>
								<td width="7%" class="cgr">17时</td>
								<td width="7%" class="cgr">18时</td>
								<td width="7%" class="cgr">19时</td>
								<td width="7%" class="cgr">20时</td>
								<td width="7%" class="cgr">21时</td>
								<td width="7%" class="cgr">22时</td>
								<td width="7%" class="cgr">23时</td>
							</tr>
							<tr class="bgh">
								<td width="10%" class="cgr">15分</td>
								<td width="7.5%"><input readonly="true" name="h49" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h53" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h57" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h61" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h65" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h69" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h73" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h77" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h81" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h85" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h89" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h93" class="datainput"></td>
							</tr>
							<tr>
								<td width="10%" class="cgr">30分</td>
								<td width="7.5%"><input readonly="true" name="h50" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h54" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h58" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h62" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h66" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h70" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h74" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h78" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h82" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h86" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h90" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h94" class="datainput"></td>
							</tr>
							<tr class="bgh">
								<td width="10%" class="cgr">45分</td>
								<td width="7.5%"><input readonly="true" name="h51" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h55" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h59" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h63" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h67" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h71" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h75" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h79" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h83" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h87" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h91" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h95" class="datainput"></td>
							</tr>
							<tr>
								<td width="10%" class="cgr">00分</td>
								<td width="7.5%"><input readonly="true" name="h52" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h56" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h60" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h64" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h68" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h72" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h76" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h80" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h84" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h88" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h92" class="datainput"></td>
								<td width="7.5%"><input readonly="true" name="h96" class="datainput"></td>
							</tr>
						</table>
					</div>
					<div class="cl"></div>
					<div class="cchart"></div>
					
				</div>
			</div>
		</div>
		
		<div class="mid" id="clear" style="height:80%;width: 1220px;">
		<iframe id="frame" src="${pageContext.request.contextPath }/issue/initClear" style="width: 100%;height: 100%;border:0"></iframe>
		</div>
		<div class="mid" id="map" style="height:80%;width: 1220px;">
		<iframe id="frame_map" src="${pageContext.request.contextPath }/issue/initMap" style="width: 100%;height: 100%;border:0"></iframe>
		</div>
		
	</div>
	
</state:override>
<script type="text/javascript">
function zTreeOnClick(event, treeId, treeNode) {
	var treeName=treeNode.name;
	/******2016.08.17修改新界面******start******/
	//alert(treeNode.id);
	//如果叶子则展示数据
	if(treeNode.id.charAt(0) != '-'){
		//alert(treeNode.id);
		issue.getResultData(treeNode.id);
	}else{
		//如果点击的是文件夹，则展开所有子节点
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		treeObj.expandNode(treeNode, true, true, true);
	}
	/*
	if(treeName=="申报单汇总" || treeName=="费用汇总"){
		issue.getCountData(treeNode.name);
	}else if(treeName=="全部汇总"){
		//issue.getAreaData("");
	}else if(treeName=="各省份汇总"){
		issue.getAreaData("");
	}else if(!treeName.match(/\d+/g)){
		issue.getAreaData(treeNode.name);
	}else if(treeName.match(/\d+/g)){
		issue.getIssueData(treeNode.name);
	}
	*/
	 /******2016.08.17修改新界面******end******/
	
};
</script>
</HEAD>
<%@ include file="/common/block/block.jsp" %>

