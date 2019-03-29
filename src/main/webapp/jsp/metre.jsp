<%@ page language="java" import="java.util.*,com.state.util.AuthoritiesUtil,com.state.util.VersionCtrl" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.state.com/state" prefix="state"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<state:override name="head">
	<title></title>
	<!-- 
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/declare.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	 -->
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/demo.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css"> 
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
        </style> 
	<script src="${pageContext.request.contextPath }/js/charts/highcharts.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
    <script src="${pageContext.request.contextPath }/js/state/jqueryFormat.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<!-- 
	highchart导出
	<script src="${pageContext.request.contextPath }/js/charts/modules/exporting.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	 -->
	 <script src="${pageContext.request.contextPath }/js/state/metre.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	
	<script type="text/javascript">
		var limitLine = new LimitLine();
		$(function(){
			var area = $('#area').val();
			if(<%=!AuthoritiesUtil.isAllow_LineL_Input() %>){
				$("input[class='datainput']").attr('disabled','disabled');
			}else{
				
				 var content= "<a class='btn1' style='margin:0px;color:#06938e;' href='#' onclick='limitLine.updateLimitLine();'>保存</a>";
				  var content1 = "<a class='btn1' style='margin:10px;color:#06938e;' href='#' onclick='limitLine.getLimitAndPlanValue();'>获取电量数据</a>";
				  var content2 = "<a class='btn1' style='margin:0px;color:#06938e;' href='#' onclick='limitLine.exportToPlan();'>交易功率导出至计划</a>";
				  if(<%=AuthoritiesUtil.isAllow("LineL_Btn") %>){
					 // $('#btnLoad').append(content);
					 // $('#btngetPlan').append(content1);
					  $('#btnLoad').append(content1);
					 // $('#btngetPlan').append(content2); 
				  }
				  
				  
			}
			var pathContent="<div class='fl mne'><a href='${pageContext.request.contextPath }/settleMent/init' name='出清数据' >清算数据</a></div>";
			if(<%=AuthoritiesUtil.isAllow("Li_Menu_Tdpz") %>){
				  //$('#limitconrightt').append(pathContent);
				  //alert("222222222222222");
			  }
			$('.menu1').find('a[name=计量清算]').attr('class', 'menufocus');
			limitLine.getLimitLine();
			$("#limitLineMenu li").live('click', function(){limitLine.getAllLimitLineData($(this), '日内计划值');});
			//$("#limitLineMenu li").live('dblclick', function(){limitLine.changeLimitLineName();});
			$("#limitLineMenu li").live('blur', function(){limitLine.finishChangeLimitLineName();});
		//	$("#limitLineDataDiv input").live("keydown", function(e){limitLine.copyTableValue($(this), e);});
			$("#limitLineDataDiv input").live("click", function(e){limitLine.changeData();});
			
		})
	</script>
	
</state:override>
<state:override name="content">
<!-- 锁屏 -->
   <div class="popup" id="popupDiv">  
        <iframe frameborder="1" scrolling="no" height="100%" width="100%" class="fff"></iframe>  
        </div> 
	<div>
<!-- 	----- -->
	<div style="text-align: center">
	    <div id="limitconrightt" class="limitconrightt" style="text-align: center;width:1200px;margin: auto;">
	    		<c:if test='<%=AuthoritiesUtil.isAllow("Li_Menu_Tdxe") %>'>
						<div class="fl mne"><a href="${pageContext.request.contextPath }/metre/init" name="计量数据"  style="color:#D1B664;fontWeight:bold">计量数据</a></div>
				</c:if>
		<div id="btngetPlan" class="rlplan">
		</div>
	</div>
	<!--  <div id="loading" style="display:none">
				      <font color="red"> ...............系统正在努力计算中,请稍后 ...................</font>
     </div> -->
		<div class="mid">
			<div>
				<div class="lmenu">
					<ul id="limitLineMenu">
						
					</ul>
				</div>
			    <div id="limitLineDataDiv" class="fl bd1" style="display:none;">
					<div class="conrightt1">
						<div class="fl mne"><a href="#" name="日内计划值" onclick="limitLine.getLimitLineDataByLimitLineType('日内计划值');">计量</a></div>
					   	 <!--  
					   	 <div class="fl mne"><a href="#" name="日前计划值" onclick="limitLine.getLimitLineDataByLimitLineType('日前计划值');">计划</a></div>
-->
						<div id="btnLoad" class="rl">
						<c:if test='<%=AuthoritiesUtil.isAllow("Li_Btn_Export") %>'>
						 <!--   <a class='btn1' style='margin:0px;color:#06938e;' href='#' onclick='limitLine.exportLimitData();'>导出</a>-->
						</c:if>
						</div>
					</div>
					<div class="cl"></div>
					<div class="fl conrightt2"><span>总电量:</span><span name="sumValue" class="avenum">0</span><span class="avenum">&nbsp;MWh</span>|<span class="pdl30">平均电力:</span><span name="avgValue" class="avenum">0</span><span class="avenum">&nbsp;MW</span></div>
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
</state:override>
<script type="text/javascript">
    // Firefox, Google Chrome, Opera, Safari, Internet Explorer from version 9
        function OnInput (event) {
            var num=event.target.value;
            var re = /^-?[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^-?[1-9]+[0-9]*(\.\d+)?$/;
    		if (!re.test(num)) {
    			alert("请输入数字！");
    			event.target.value="";
    		} else {
    			return true;
    		}
            //document.getElementById("avep").innerHTML=Math.round(avgnum);  
        };
   
    </script>
<%@ include file="/common/block/block.jsp" %>

