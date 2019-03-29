<%@ page language="java" import="java.util.*,com.state.util.AuthoritiesUtil,com.state.util.VersionCtrl" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title></title>
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/demo.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css"> 
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/declare.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	 <!-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/ui/themes/icon.css?v=<%=VersionCtrl.getVesrsion()%>">-->
    <Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/head.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css"> 
    <Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/ui-dialog.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/common.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css"> 
	
    <link href="${pageContext.request.contextPath }/bootstrap/css/bootstrap-btn.css?v=<%=VersionCtrl.getVesrsion()%>" rel="stylesheet">                    
	
	<style type="text/css">  
            .divv  
            {  
                position:absolute;  
                top:30%;  
                left:30%;  
                width:80px;  
                height:60px;  
                border:1px solid #666;  
                background-color:#9CF;  
                text-align:center;  
                display:none;  
                z-index:300;  
            }  
            .popup  
            {  
                border:1px solid red;  
                position:absolute;  
                top:0px;  
                left:0px;  
                width:100%;  
                height:100%;  
                background-color:#000;  
                filter:alpha(opacity=45);  
                opacity:0.45;  
                display:none;  
                z-index:200;  
            }  
            .fff  
            {  
                border:1px solid blue;  
                position:relative;  
                background-color:#000;  
            } 
            .lmenu{
            height: 614px;
            } 
            .bd1 {
    border-top: 0px;
}
.rlplan {
    padding-right: 20px;
}
.conrightt1 a {
	margin: 2px 10px;
}
.conrightt2 {
    padding: 0px 0px 0px 12px;
}
.cchart {
    height: 241px;
    width: 966px;
    border: solid 1px #dcdcdb;
    margin: 10px;
}
        </style> 
	<script src="${pageContext.request.contextPath }/js/jquery/jquery-1.8.3.min.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
    <script src="${pageContext.request.contextPath }/js/jquery/jquery.json.min.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/common/common-public-1.0.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/state/dialog/dialog-min.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/state/dialog/dialog-plus.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/state/dialog/sea.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	
	<script src="${pageContext.request.contextPath }/js/charts/highcharts.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
    <script src="${pageContext.request.contextPath }/js/state/jqueryFormat.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/state/measure.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	
	<script type="text/javascript">
		var limitLine = new LimitLine();
		$(function(){
			if(<%=!AuthoritiesUtil.isAllow_LineL_Input() %>){
				$("input[class='datainput']").attr('disabled','disabled');
			}
			
			$('.menu1').find('a[name=计量清算]').attr('class', 'menufocus');
			limitLine.getLimitLine();
			$("#limitLineMenu li").live('click', function(){limitLine.getAllLimitLineData($(this), '日内计划值');});
			$("#limitLineMenu li").live('blur', function(){limitLine.finishChangeLimitLineName();});
			$("#limitLineDataDiv input").live("click", function(e){limitLine.changeData();});
			
		})
	</script>
</head>
<body>	


	<div style="text-align: center">
		<div class="mid">
			<div>
				<div class="lmenu">
					<ul id="limitLineMenu">
						
					</ul>
				</div>
			    <div id="limitLineDataDiv" class="fl bd1" style="display:none;">
					<div class="conrightt1">
						<div class="fl conrightt2"><span>总电量:</span><span name="sumValue" class="avenum">0</span><span class="avenum">&nbsp;MWh</span>|<span class="pdl30">平均电力:</span><span name="avgValue" class="avenum">0</span><span class="avenum">&nbsp;MW</span></div>
<!-- 						<div class="fl mne"><a href="#" name="日内计划值" onclick="limitLine.getLimitLineDataByLimitLineType('日内计划值');">计量</a></div> -->
						<div id="btnLoad" class="rl">
						<c:if test='<%=AuthoritiesUtil.isAllow("LineL_Btn") %>'>
						<a class='btn1' style='margin:10px;color:#06938e;' href='#' onclick='limitLine.getLimitAndPlanValue();'><span style="color: #06938e;">获取电量数据</span></a>
						</c:if>
						</div>
					</div>
					<div class="cl"></div>
					<div class="cl"></div>
					<div class="fl pdl10">
						<table width="968" height="302" cellpadding="0" cellspacing="0">
							<thead>
								<th width="16%" class="cgr">时刻</th>
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
								<td width="16%" class="cgr">15分</td>
								<td width="7%"><input name="h01" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h05" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h09" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h13" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h17" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h21" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h25" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h29" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h33" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h37" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h41" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h45" class="datainput" oninput="OnInput (event)"></td>
							</tr>
							<tr class="bgh">
								<td width="16%" class="cgr">30分</td>
								<td width="7%"><input name="h02" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h06" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h10" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h14" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h18" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h22" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h26" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h30" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h34" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h38" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h42" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h46" class="datainput" oninput="OnInput (event)"></td>
							</tr>
							<tr>
								<td width="16%" class="cgr">45分</td>
								<td width="7%"><input name="h03" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h07" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h11" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h15" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h19" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h23" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h27" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h31" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h35" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h39" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h43" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h47" class="datainput" oninput="OnInput (event)"></td>
							</tr>
							<tr class="bgh">
								<td width="16%" class="cgr">00分</td>
								<td width="7%"><input name="h04" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h08" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h12" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h16" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h20" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h24" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h28" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h32" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h36" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h40" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h44" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h48" class="datainput" oninput="OnInput (event)"></td>
							</tr>
							<tr>
								<td width="16%" class="cgr">时刻</td>
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
								<td width="16%" class="cgr">15时</td>
								<td width="7%"><input name="h49" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h53" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h57" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h61" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h65" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h69" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h73" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h77" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h81" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h85" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h89" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h93" class="datainput" oninput="OnInput (event)"></td>
							</tr>
							<tr>
								<td width="16%" class="cgr">30分</td>
								<td width="7%"><input name="h50" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h54" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h58" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h62" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h66" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h70" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h74" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h78" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h82" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h86" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h90" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h94" class="datainput" oninput="OnInput (event)"></td>
							</tr>
							<tr class="bgh">
								<td width="16%" class="cgr">45分</td>
								<td width="7%"><input name="h51" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h55" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h59" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h63" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h67" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h71" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h75" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h79" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h83" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h87" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h91" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h95" class="datainput" oninput="OnInput (event)"></td>
							</tr>
							<tr>
								<td width="16%" class="cgr">00分</td>
								<td width="7%"><input name="h52" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h56" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h60" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h64" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h68" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h72" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h76" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h80" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h84" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h88" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h92" class="datainput" oninput="OnInput (event)"></td>
								<td width="7%"><input name="h96" class="datainput" oninput="OnInput (event)"></td>
							</tr>
						</table>
					</div>
					<div class="cl"></div>
					<div class="cchart"></div>
				</div>
			</div>
		</div>
	</div>
</body>	
</html>
