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
		a:hover {
		    color: #ffa110 !important;
		}
		.bghh {
			background-color: #e5e5e5;
		}
	</style>
	<script src="${pageContext.request.contextPath }/js/charts/highcharts.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/state/jqueryFormat.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<!-- 
	<script src="${pageContext.request.contextPath }/js/charts/modules/exporting.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	-->
	<script src="${pageContext.request.contextPath }/js/state/issueResult.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script type="text/javascript">
		var executeResult = new ExecuteResult();

		$(function(){			
			executeResult.loadTree();
			
			
			var startPoint = null;
			$('#table_edit').find('input').focus(function(event){
				issueResult.inputVal = this.value;
			});
			$('#table_edit').find('input').keyup(function(event){
				issueResult.inputVal = this.value;
			});
			$("#table_edit").mousedown(function(event){
			    startPoint = {
			    		x:event.pageX,
			    		y:event.pageY
			    };
			    });//这个是鼠标键，是你鼠标左击按下的的效果
			  $("#table_edit").mouseup(function(event){
				  
				  var endPoint = {
				    		x:event.pageX,
			    			y:event.pageY
				    };
				    if(startPoint == null){
				   		return ;
				   	}
				    if((startPoint.x - endPoint.x )== 0 && (startPoint.y - endPoint.y) == 0){
				    	startPoint = null;
				    	return ;
				    }
				    
				    var inputs = $('#table_edit').find('input');
				    var startIndex = null;
				    var endIndex = null;
				    if(inputs != null && inputs != undefined){
				    	for(var i = 0;i<inputs.length;i++){
				    		if(i == 10){
				    			//break;
				    		}
				    		var input = inputs[i];
				    		var offset = $(input.parentNode).offset();
				    		//获取每个输入框的起始点坐标
				    		var inputSP = {x:offset.left,y:offset.top};
				    		var inputEP = {x:offset.left+input.clientWidth,y:offset.top+input.clientHeight};
				    		//判断开始点是否在输入框矩形区域内
				    		if(startPoint.x<inputEP.x && startPoint.x>inputSP.x && startPoint.y<inputEP.y && startPoint.y>inputSP.y){
				    			startIndex = parseInt(input['name'].substring(1));				    			
				    		}
				    		//判断结束点是否在输入框矩形区域内
				    		if(endPoint.x<inputEP.x && endPoint.x>inputSP.x && endPoint.y<inputEP.y && endPoint.y>inputSP.y){
				    			endIndex = parseInt(input['name'].substring(1));				    			
				    		}
				    		if(startIndex != null && endIndex != null){
				    			break;
				    		}
				    	}
				    }
				    if(startIndex != null && endIndex != null){
				    	for (var i = startIndex; i <= endIndex; i++) {
							keyStr = (100 + i) + "";
							keyStr = keyStr.substring(1, keyStr.length);
							key = "h" + keyStr;
							$('#table_edit input[name=' + key + ']').val(issueResult.inputVal);
					    }
		    		}
				    startPoint = null;
			    });//这个是鼠标键，是你鼠标左击放开后的效果
			
			
		});
		/**
		判断两个矩形是否相交
		*/
		function isIntersect(x01,y01,x02,y02,x11,y11,x12,y12){
			var zx = ab(x01+x02-x11-x12); //两个矩形重心在x轴上的距离的两倍
	        var x = ab(x01-x02)+ab(x11-x12); //两矩形在x方向的边长的和
	        var zy = ab(y01+y02-y11-y12); //重心在y轴上距离的两倍
	        var y = ab(y01-y02)+ab(y11-y12); //y方向边长的和
	        if(zx <= x && zy <= y)
	            return true;
	        else
	            return false;
		}
		function ab(n)
		{
		    if(n >= 0)
		        return n;
		    else
		        return -n;
		}
		
	</script>
</state:override>
<state:override name="content">			
	<div>
	    <div id="limitconrightt" class="limitconrightt" style="text-align: center;width:1200px;margin: auto;">
			  
			  <div class='fl mne'><a id='a_6' href='javascript:;' onclick='issueResult.getLimitLine();return false;' name='日报编辑'>执行结果</a></div>
			  <div id="btngetPlan" class="rlplan" style="padding-right: 3px;margin-top: 0px;">
			 	<a class='btn1' id='btn_get' href='#' style="color:#06938e;" onclick='executeResult.getDealResultFromInterface();'>获取执行结果</a>
			 
			 </div>
		</div>
		<div class="mid" id="result">
			<div class="contop" style="padding:0px 10px;">
				<div id="lbtn" class="rl"> </div>
				<div class="cl"></div>
			</div>
			<div>
			    
			    <div class="lmenu zTreeDemoBackground left">
					<ul id="treeDemo"  class="ztree">
						
					</ul>
				</div>
				
				<div id="IssueDataDiv" class="fl bd1" style="padding-top:10px;;border:solid 0px;width:975px;">
					
					<div class="fl conrightt2" style="width:928px;text-align: left;padding-top: 0px;display:none;"><span>总电量:</span><span name="sumValue" class="avenum">0</span><span class="avenum">&nbsp;MWh</span>|<span class="pdl30">平均电力:</span><span name="avgValue" class="avenum">0</span><span class="avenum">&nbsp;MW</span></div>
					
					<div class="fl pdl10">
						<table id="table_edit" width="968" height="302" cellpadding="0" cellspacing="0">
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
								<td width="7.5%"><input readonly="true" name="h01" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h05" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h09" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h13" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h17" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h21" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h25" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h29" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h33" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h37" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h41" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h45" class="datainput" oninput="OnInput (event)"></td>
							</tr>
							<tr class="bgh">
								<td width="10%" class="cgr">30分</td>
								<td width="7.5%"><input readonly="true" name="h02" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h06" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h10" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h14" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h18" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h22" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h26" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h30" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h34" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h38" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h42" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h46" class="datainput" oninput="OnInput (event)"></td>
							</tr>
							<tr>
								<td width="10%" class="cgr">45分</td>
								<td width="7.5%"><input readonly="true" name="h03" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h07" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h11" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h15" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h19" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h23" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h27" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h31" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h35" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h39" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h43" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h47" class="datainput" oninput="OnInput (event)"></td>
							</tr>
							<tr class="bgh">
								<td width="10%" class="cgr">60分</td>
								<td width="7.5%"><input readonly="true" name="h04" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h08" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h12" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h16" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h20" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h24" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h28" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h32" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h36" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h40" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h44" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h48" class="datainput" oninput="OnInput (event)"></td>
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
								<td width="7.5%"><input readonly="true" name="h49" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h53" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h57" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h61" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h65" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h69" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h73" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h77" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h81" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h85" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h89" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h93" class="datainput" oninput="OnInput (event)"></td>
							</tr>
							<tr>
								<td width="10%" class="cgr">30分</td>
								<td width="7.5%"><input readonly="true" name="h50" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h54" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h58" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h62" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h66" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h70" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h74" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h78" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h82" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h86" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h90" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h94" class="datainput" oninput="OnInput (event)"></td>
							</tr>
							<tr class="bgh">
								<td width="10%" class="cgr">45分</td>
								<td width="7.5%"><input readonly="true" name="h51" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h55" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h59" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h63" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h67" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h71" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h75" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h79" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h83" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h87" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h91" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h95" class="datainput" oninput="OnInput (event)"></td>
							</tr>
							<tr>
								<td width="10%" class="cgr">60分</td>
								<td width="7.5%"><input readonly="true" name="h52" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h56" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h60" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h64" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h68" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h72" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h76" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h80" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h84" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h88" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h92" class="datainput" oninput="OnInput (event)"></td>
								<td width="7.5%"><input readonly="true" name="h96" class="datainput" oninput="OnInput (event)"></td>
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

function OnInput (event) {
    var num=event.target.value;
    num = (num+"").replace(",","");
    var re = /^-?[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^-?[1-9]+[0-9]*(\.\d+)?$/;
	if (!re.test(num)) {
		alert("请输入数字！");
		event.target.value="";
	} else {
		return true;
	}
};

function zTreeOnClick(event, treeId, treeNode) {
	//var treeName=treeNode.name;
	
	//如果叶子则展示数据
	if(treeNode.id.charAt(0) != '-'){
		executeResult.getResultData(treeNode.id,treeNode);
		
	}else{
		//如果点击的是文件夹，则展开所有子节点
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		treeObj.expandNode(treeNode, true, true, true);
	}
	
};
</script>
</HEAD>
<%@ include file="/common/block/block.jsp" %>

