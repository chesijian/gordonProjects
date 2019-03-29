<%@ page language="java" import="java.util.*,com.state.util.AuthoritiesUtil,com.state.util.VersionCtrl" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>申报单详情</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<head>
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/common.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/button.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/demo.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css"> 
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/declare.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	
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
            .circle  
{  
	border:1px;
	border-color:red;
    height: 20px;  
    width: 20px;  
    border-radius: 50px;  
}   
            .icon-ok {background-image: url(../img/icon/sok.gif) !important;}
        </style>  
        <!-- 
	<script src="${pageContext.request.contextPath }/js/jquery/jquery-1.10.2.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
   -->
   <link rel="stylesheet" href="${pageContext.request.contextPath }/js/jquery.tooltip/jquery.tooltip.css" type="text/css" />
    
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery/jquery-1.8.3.min.js"></script>
    <!--  -->
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.tooltip/jquery.tooltip.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/common/common-public-1.0.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	
	<script src="${pageContext.request.contextPath }/js/charts/highcharts.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/charts/highcharts-more.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	
	<script src="${pageContext.request.contextPath }/js/state/declare/declare_sheet.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<style type="text/css">
		.div_chart{
			width: 200px;
			height:200px;
			background-color: white;
		}
		.ui-dialog-close {
    position: relative;
    _position: absolute;
    float: right;
    top: 10px;
    right: 13px;
    _height: 26px;
    padding: 0 4px;
    font-size: 21px;
    font-weight: bold;
    line-height: 1;
    color: #000;
    text-shadow: 0 1px 0 #FFF;
    opacity: .2;
    filter: alpha(opacity=20);
    cursor: pointer;
    background: transparent;
    _background: #FFF;
    border: 0;
    -webkit-appearance: none;
}
.ui-dialog-close:hover,
.ui-dialog-close:focus {
    color: #000000;
    text-decoration: none;
    cursor: pointer;
    outline: 0;
    opacity: 0.5;
    filter: alpha(opacity=50);
}
	</style>
	
	<script type="text/javascript">
	//var selectData = ${dataInfo};
	//var statusData = ${statusData};
	var selectData;
	var statusData;
	
	
	</script>
</head>
<body>
<!-- 锁屏 -->
   <div class="popup" id="popupDiv">  
        <iframe frameborder="1" scrolling="no" height="100%" width="100%" class="fff"></iframe>  
        </div> 
	<div >
<!-- 	----- -->
		<div class="mid" >
			<!-- 	
			<div class="contop">
				<div class="fl" style="display: none;"><span class="xmenu">选择电价<select id="select_price" onchange="change(this.value);"></select></span>
				</div>
				<div id="btnData" class="rl">
				</div> 
				   <div class="cl"></div>
			    </div> 
			<div>
			-->
			<div class="fl pdl10" style="margin-top: 10px;padding-top: 0px;">
						<table width="975" height="302" cellpadding="0" cellspacing="0" style="margin-top: 0px;padding-top: 0px;">
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
								<td width="7%" align="center"><div id="h01" class="circle"></div></td>
								<td width="7%" align="center"><div id="h05" class="circle"></div></td>
								<td width="7%" align="center"><div id="h09" class="circle"></div></td>
								<td width="7%" align="center"><div id="h13" class="circle"></div></td>
								<td width="7%" align="center"><div id="h17" class="circle"></div></td>
								<td width="7%" align="center"><div id="h21" class="circle"></div></td>
								<td width="7%" align="center"><div id="h25" class="circle"></div></td>
								<td width="7%" align="center"><div id="h29" class="circle"></div></td>
								<td width="7%" align="center"><div id="h33" class="circle"></div></td>
								<td width="7%" align="center"><div id="h37" class="circle"></div></td>
								<td width="7%" align="center"><div id="h41" class="circle"></div></td>
								<td width="7%" align="center"><div id="h45" class="circle"></div></td>
							</tr>
							<tr class="bgh">
								<td width="16%" class="cgr">30分</td>
								<td width="7%" align="center"><div id="h02" class="circle"></div></td>
								<td width="7%" align="center"><div id="h06" class="circle"></div></td>
								<td width="7%" align="center"><div id="h10" class="circle"></div></td>
								<td width="7%" align="center"><div id="h14" class="circle"></div></td>
								<td width="7%" align="center"><div id="h18" class="circle"></div></td>
								<td width="7%" align="center"><div id="h22" class="circle"></div></td>
								<td width="7%" align="center"><div id="h26" class="circle"></div></td>
								<td width="7%" align="center"><div id="h30" class="circle"></div></td>
								<td width="7%" align="center"><div id="h34" class="circle"></div></td>
								<td width="7%" align="center"><div id="h38" class="circle"></div></td>
								<td width="7%" align="center"><div id="h42" class="circle"></div></td>
								<td width="7%" align="center"><div id="h46" class="circle"></div></td>
							</tr>
							<tr>
								<td width="16%" class="cgr">45分</td>
								<td width="7%" align="center"><div id="h03" class="circle"></div></td>
								<td width="7%" align="center"><div id="h07" class="circle"></div></td>
								<td width="7%" align="center"><div id="h11" class="circle"></div></td>
								<td width="7%" align="center"><div id="h15" class="circle"></div></td>
								<td width="7%" align="center"><div id="h19" class="circle"></div></td>
								<td width="7%" align="center"><div id="h23" class="circle"></div></td>
								<td width="7%" align="center"><div id="h27" class="circle"></div></td>
								<td width="7%" align="center"><div id="h31" class="circle"></div></td>
								<td width="7%" align="center"><div id="h35" class="circle"></div></td>
								<td width="7%" align="center"><div id="h39" class="circle"></div></td>
								<td width="7%" align="center"><div id="h43" class="circle"></div></td>
								<td width="7%" align="center"><div id="h47" class="circle"></div></td>
							</tr>
							<tr class="bgh">
								<td width="16%" class="cgr">00分</td>
								<td width="7%" align="center"><div id="h04" class="circle"></div></td>
								<td width="7%" align="center"><div id="h08" class="circle"></div></td>
								<td width="7%" align="center"><div id="h12" class="circle"></div></td>
								<td width="7%" align="center"><div id="h16" class="circle"></div></td>
								<td width="7%" align="center"><div id="h20" class="circle"></div></td>
								<td width="7%" align="center"><div id="h24" class="circle"></div></td>
								<td width="7%" align="center"><div id="h28" class="circle"></div></td>
								<td width="7%" align="center"><div id="h32" class="circle"></div></td>
								<td width="7%" align="center"><div id="h36" class="circle"></div></td>
								<td width="7%" align="center"><div id="h40" class="circle"></div></td>
								<td width="7%" align="center"><div id="h44" class="circle"></div></td>
								<td width="7%" align="center"><div id="h48" class="circle"></div></td>
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
								<td width="16%" class="cgr">15分</td>
								<td width="7%" align="center"><div id="h49" class="circle"></div></td>
								<td width="7%" align="center"><div id="h53" class="circle"></div></td>
								<td width="7%" align="center"><div id="h57" class="circle"></div></td>
								<td width="7%" align="center"><div id="h61" class="circle"></div></td>
								<td width="7%" align="center"><div id="h65" class="circle"></div></td>
								<td width="7%" align="center"><div id="h69" class="circle"></div></td>
								<td width="7%" align="center"><div id="h73" class="circle"></div></td>
								<td width="7%" align="center"><div id="h77" class="circle"></div></td>
								<td width="7%" align="center"><div id="h81" class="circle"></div></td>
								<td width="7%" align="center"><div id="h85" class="circle"></div></td>
								<td width="7%" align="center"><div id="h89" class="circle"></div></td>
								<td width="7%" align="center"><div id="h93" class="circle"></div></td>
							</tr>
							<tr>
								<td width="16%" class="cgr">30分</td>
								<td width="7%" align="center"><div id="h50" class="circle"></div></td>
								<td width="7%" align="center"><div id="h54" class="circle"></div></td>
								<td width="7%" align="center"><div id="h58" class="circle"></div></td>
								<td width="7%" align="center"><div id="h62" class="circle"></div></td>
								<td width="7%" align="center"><div id="h66" class="circle"></div></td>
								<td width="7%" align="center"><div id="h70" class="circle"></div></td>
								<td width="7%" align="center"><div id="h74" class="circle"></div></td>
								<td width="7%" align="center"><div id="h78" class="circle"></div></td>
								<td width="7%" align="center"><div id="h82" class="circle"></div></td>
								<td width="7%" align="center"><div id="h86" class="circle"></div></td>
								<td width="7%" align="center"><div id="h90" class="circle"></div></td>
								<td width="7%" align="center"><div id="h94" class="circle"></div></td>
							</tr>
							<tr class="bgh">
								<td width="16%" class="cgr">45分</td>
								<td width="7%" align="center"><div id="h51" class="circle"></div></td>
								<td width="7%" align="center"><div id="h55" class="circle"></div></td>
								<td width="7%" align="center"><div id="h59" class="circle"></div></td>
								<td width="7%" align="center"><div id="h63" class="circle"></div></td>
								<td width="7%" align="center"><div id="h67" class="circle"></div></td>
								<td width="7%" align="center"><div id="h71" class="circle"></div></td>
								<td width="7%" align="center"><div id="h75" class="circle"></div></td>
								<td width="7%" align="center"><div id="h79" class="circle"></div></td>
								<td width="7%" align="center"><div id="h83" class="circle"></div></td>
								<td width="7%" align="center"><div id="h87" class="circle"></div></td>
								<td width="7%" align="center"><div id="h91" class="circle"></div></td>
								<td width="7%" align="center"><div id="h95" class="circle"></div></td>
							</tr>
							<tr>
								<td width="16%" class="cgr">00分</td>
								<td width="7%" align="center"><div id="h52" class="circle"></div></td>
								<td width="7%" align="center"><div id="h56" class="circle"></div></td>
								<td width="7%" align="center"><div id="h60" class="circle"></div></td>
								<td width="7%" align="center"><div id="h64" class="circle"></div></td>
								<td width="7%" align="center"><div id="h68" class="circle"></div></td>
								<td width="7%" align="center"><div id="h72" class="circle"></div></td>
								<td width="7%" align="center"><div id="h76" class="circle"></div></td>
								<td width="7%" align="center"><div id="h80" class="circle"></div></td>
								<td width="7%" align="center"><div id="h84" class="circle"></div></td>
								<td width="7%" align="center"><div id="h88" class="circle"></div></td>
								<td width="7%" align="center"><div id="h92" class="circle"></div></td>
								<td width="7%" align="center"><div id="h96" class="circle"></div></td>
							</tr>
						</table>
					</div>
				<div class="cchart" style="width:975px;heigth:152px;"></div>
			</div>
		</div>
	</div>
	
</body>
</html>

