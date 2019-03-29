<%@ page language="java" import="java.util.*,com.state.util.AuthoritiesUtil,com.state.util.SessionUtil,com.state.util.VersionCtrl" pageEncoding="UTF-8"
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
.big_btn-primary:hover {
    color: #fff;
    background-color: #286090;
    border-color: #204d74;
}
a:hover {
    color: #ffa110 !important;
}
.th{
    height: 40px;
    border-left: 1px solid #dcdcdb;
    background-color: #ebebeb;
    color: #068F81;

}
.tr{
background-color: #f9f9f9;}
.icon-ok {background-image: url(../img/icon/sok.gif) !important;}
	</style>
	<script src="${pageContext.request.contextPath }/js/charts/highcharts.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/state/jqueryFormat.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<!-- 
	<script src="${pageContext.request.contextPath }/js/charts/modules/exporting.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	 -->
	<script src="${pageContext.request.contextPath }/js/common/common-public-1.0.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/state/issue.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script>
	function shutdown(msg){
		//alert(11);
		if(typeof document.getElementsByTagName("iframe")["_win_frame"].contentWindow != "undefined"){
			//alert(22);
			document.getElementsByTagName("iframe")["_win_frame"].contentWindow.shutdown(msg);
			
		}
	}
	function Match(){
	  	// 撮合数据
	  	 	this.subData=function(purl,dtype){
	  	 		createWin(baseUrl+"/match/issueProcess",800,500,"撮合进度");
	  	 		//return ;
	  	 		var time = $("#time").val().replace(/[^0-9]/ig, "");
	  	 		//alert(dtype);
	  	 		var time1 = $("#time").val();
	  	 		$.ajax({
	  	 			url : purl,
	  	 			type : 'POST',
	  	 			dataType : 'json',
	  	 			data : {
	  	 				time:time,
	  	 				time1:time1,
	  	 				dtype:dtype
	  	 			},
	  	 			beforeSend:function(){
	  	 				//$("#loading").show();
	  	 				//$("#popupDiv").show();  
	  	 			},
	  	 			success : function(result) {
	  	 				var msg = '';
	  	 				if(result != undefined && result!= null){
	  	 					issue.loadTree(1,"result");
	  	 					getProcessBtnStatus();
	  	 					//alert(result['msg']);
	  	 					if(result['status']){
	  	 						alert("撮合成功!");
	  	 						closeWin();
	  	 					}else{
	  	 						alert(result['msg']);
	  	 						
	  	 					}
	  	 					//
	  	 					
	  	 					msg = result['log'];
	  	 				}else{
	  	 					alert("计算失败！");
	  	 				}
	  	 				shutdown(msg);
	  	 			    unmask();
	  	 			 
	  	 			},
	  	 			error : function(xhr, status) {
	  	 				alert("系统错误!");
	  	 				unmask();
	  	 				shutdown();
	  	 			}
	  	 		});
	  	 		
	  	 	}
	  	 	
	  	 	//判断申报单类型是否一致
	  	 	this.matchData = function() {
	  	 		var time = $("#time").val().replace(/[^0-9]/ig, "");
	  	 		var time1 = $("#time").val();
	  	 		var curl=baseUrl+'/match/checkDtype';
	  	 		var purl=baseUrl+'/match/matchData';
	  	 		 
	  	 			 //$("#declareDataDiv").hide();
	  	 			 mask("正在计算...");
	  	 			 $.ajax({
	  	 				    url : curl,
	  	 					type : 'POST',
	  	 					dataType : 'json',
	  	 					data : {
	  	 						time:time,
	  	 						time1:time1
	  	 					},
	  	 					success : function(result) {
	  	 						var dy=result;
	  	 						if(dy=='0'){
	  	 							if(confirm("提交的申报单类型不一致,是否继续计算?")){
	  	 								newMatch.subData(purl,dy);
	  	 							}else{
	  	 								unmask();
	  	 							}
	  	 						}else if(dy==''){
	  	 							  alert("当前日期不存在申报单!"); 
	  	 							  unmask();
	  	 						}else{
	  	 							match.subData(purl,dy);
	  	 						}
	  	 						
	  	 					}
	  	 			
	  	 			 });
	  	 			
	  	 		 
	  	 	};
	  	}
	var match = new Match();
	</script>
	<script type="text/javascript">
		var issue = new Issue();
		// var match = new Match();
		$(function(){
			$('.menu1').find('a[name=发布]').attr('class', 'menufocus');
			var area = $('#area').val();
			if(${isMatch}){
				$('#btn_save').hide();
				$('#map').hide();
				match.matchData();
			}else{
				issue.loadTree(0,"survey");
			}
		});
		function issuePageBtnStatus(flag){
			if(flag==1){
				$('#btngetPlan a').each(function(i){
					if($(this).attr('id')=='jgfb'){
						$(this).text('已发布');
					}
				});
				var span = '<span id="isztfb" style="color: #c19d30; font-size: 16px;padding-right: 50px;">发布状态：已发布</span>';
				if(!_isState){
					$('#btngetPlan span').each(function(i){
						if($(this).attr('id')=='isztfb'){
							$(this).remove();
						}
					});
					$('#btngetPlan').prepend(span);
				}
			}else{
				$('#btngetPlan a').each(function(i){
					if($(this).attr('id')=='jgfb'){
						$(this).text('结果发布');
					}
				});
				//$('#btngetPlan a').text('信息发布');
				var span = '<span id="isztfb" style="color: #c19d30; font-size: 16px;padding-right: 50px;">发布状态：未发布</span>';
				if(!_isState){
					$('#btngetPlan span').each(function(i){
						if($(this).attr('id')=='isztfb'){
							$(this).remove();
						}
					});
					$('#btngetPlan').prepend(span);
				}
			}
		}
		function getBtnStatus(){
			$.ajax({
			    url : baseUrl+"/system/getProcessBtnStatus",
				type : 'POST',
				dataType : 'json',
				data:{
					time:$('#time').val()
				},
				success : function(result) {
					if(result != null){
						var node1,node2,node3,node4,node5;
						//console.info(result);
						for(var key in result){
							if(key=='node1'){
								node1 = result[key];
							}else if(key=='node2'){
								node2 = result[key];
							}else if(key=='node3'){
								node3 = result[key];
							}else if(key=='node4'){
								node4 = result[key];
							}else if(key=='node5'){
								node5 = result[key];
							}
						}
						if(node4==3){
							$('#btngetPlan a').each(function(i){
								if($(this).attr('id')=='jgfb'){
									$(this).text('已发布');
								}
							});
							var span = '<span id="isztfb" style="color: #c19d30; font-size: 16px;padding-right: 50px;">发布状态：已发布</span>';
							if(!_isState){
								$('#btngetPlan span').each(function(i){
									if($(this).attr('id')=='isztfb'){
										$(this).remove();
									}
								});
								$('#btngetPlan').prepend(span);
							}
						}else{
							$('#btngetPlan a').each(function(i){
								if($(this).attr('id')=='jgfb'){
									$(this).text('结果发布');
								}
							});
							//$('#btngetPlan a').text('信息发布');
							var span = '<span id="isztfb" style="color: #c19d30; font-size: 16px;padding-right: 50px;">发布状态：未发布</span>';
							if(!_isState){
								$('#btngetPlan span').each(function(i){
									if($(this).attr('id')=='isztfb'){
										$(this).remove();
									}
								});
								$('#btngetPlan').prepend(span);
							}
						}
					}
				}
			});
		}
	</script>
</state:override>
<state:override name="content">			
	<div>
	    <div id="limitconrightt" class="limitconrightt" style="text-align: center;width:1200px;margin: auto;">
			  <c:if test='<%=AuthoritiesUtil.isAllow("Issue_Menu_fbxx") %>'>
				<div class="fl mne"><a id="a_0" href='javascript:;' onclick='issue.loadTree(0,"survey");return false;' name="当日概况" style="color:#D1B664;fontWeight:bold">当日概况</a></div>
			  </c:if>
			  <c:if test='<%=AuthoritiesUtil.isAllow("Issue_Menu_fbxx") %>'>
				<div class="fl mne"><a id="a_1" href='javascript:;' onclick='issue.loadTree(1,"result");return false;' name="发布信息" >分省交易结果</a></div>
			  </c:if>
			  <c:if test='<%=AuthoritiesUtil.isAllow("Issue_Menu_tdjyjg") %>'>
				<div class='fl mne'><a id='a_5' href='javascript:;' onclick='issue.loadTree(5,"lineLimit");return false;' name='交易功率'>通道交易结果</a></div>
			  </c:if>
			  <c:if test='<%=AuthoritiesUtil.isAllow("Issue_Menu_Clear") %>'>
				<div class='fl mne'><a id='a_3' href='javascript:;' onclick='issue.loadTree(3);return false;' name='出清信息'>出清明细</a></div>
			  </c:if>
			  <!-- 
			  <c:if test='<%=AuthoritiesUtil.isAllow("Issue_Menu_Cost") %>'>
				<div class='fl mne'><a id='a_2' href='javascript:;' onclick='issue.loadTree(2,"pathResult");return false;' name='输电通道'>费用信息</a></div>
			  </c:if>
			   -->
			  <c:if test='<%=AuthoritiesUtil.isAllow("Issue_Menu_Map") %>'>
				<div class='fl mne'><a id='a_4' href='javascript:;' onclick='issue.loadTree(4);return false;' name='地图展示'>可视化展示</a></div>
			  </c:if>
			  <div class='fl mne'><a id='a_7' href='javascript:;' onclick='issue.loadTree(7);return false;' name='交易单展示'>交易单展示</a></div>
			  <!--  
			  <div class='fl mne'><a id='a_6' href='javascript:;' onclick='issue.loadTree(6,"dealResult");return false;' name='日报编辑'>执行结果</a></div>
			  -->
			  
			  <div id="btngetPlan" class="rlplan" style="padding-right: 3px;margin-top: 0px;">
			 <!-- 
			  <c:if test='<%=AuthoritiesUtil.isAllow("De_Btn_Match") %>'>
					<a class='button button-glow button-rounded button-caution' style='font-size:15px;line-height:25px;height:25px;padding:0 20px;color:white;' href='#' onclick='match.matchData();'>优化计算</a>
					</c:if>
					-->
			  <c:if test='<%=AuthoritiesUtil.isAllow("Issue_Btn_cqsh") %>'>
					<a id='jgfb' class='btn1' style='margin:0px;color:#06938e;' href='#' onclick='issue.showDialog();'>结果发布</a>
				
				</c:if>
				 <!--  
				<c:if test='<%=AuthoritiesUtil.isAllow("Issue_Btn_Export") %>'>
					<a class='btn1' style='margin-right:0px;color:#06938e;' href='#' onclick='issue.exportData();'>导出</a>
				</c:if>
				-->
				<c:if test='<%=AuthoritiesUtil.isAllow("Issue_Btn_ExportLocal") %>'>
					<a class='btn1' style='margin-right:0px;color:#06938e;' href='#' onclick='issue.localExportData();'>本地导出</a>
				</c:if>
				<a class='btn1' style='margin-right:0px;color:#06938e;' href='#' onclick='issue.exportDailyDoc();'>导出交易单</a>
				<!--  
				<c:if test='<%=AuthoritiesUtil.isAllow("Issue_Btn_ExportDay") %>'>
					<a class='btn1' style='margin-right:0px;color:#06938e;' href='#' onclick='issue.exportExcel();'>导出日报</a>
				</c:if>
				<c:if test='<%=AuthoritiesUtil.isAllow("Issue_Btn_ExportDaily") %>'>
					<a class='btn1' style='margin-right:0px;color:#06938e;' href='#' onclick='issue.exportDailyDoc();'>导出交易单</a>
				</c:if>
				-->
				</div>
		</div>
		<div class="mid" id="result" style="display:none;">
			<div class="contop" style="padding:0px 10px;">
				<!-- <div class="fl"><span class="xmenu">发布</span></div>-->
				<div id="lbtn" class="rl"><!-- <span><a class="btn1" href="#" onclick="issue.issueData();">出清审核</a></span><span><a class="btn1" href="#" onclick="issue.issueData();">结算审核</a></span>--> </div>
				<div class="cl"></div>
			</div>
			<div>
			    
			    <div class="lmenu zTreeDemoBackground left">
					<ul id="treeDemo"  class="ztree">
						
					</ul>
				</div>
				
				<div id="IssueDataDiv" class="fl bd1" style="padding-top:10px;;border:solid 0px;width:975px;">
					<div class="rl" style="height: 30px">
						<a class='btn1' id='btn_save' href='#' style="color:#06938e;" onclick='issue.showDeclDialog();'><span style="color: #06938e;">保存</span></a>
						<!-- <a class='btn1' id='btn_save' href='#' style="color:#06938e;" onclick='issue.updateResult();'>保存</a> -->
					</div>
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
								<td width="10%" class="cgr">60分</td>
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
								<td width="10%" class="cgr">60分</td>
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
		
		<div class="mid" id="survey" style="height:850px;width: 1220px;display:none;;border: solid 1px #dcdcdb;border-top: 0px;">
			<div style="width:100%;margin:auto;margin-left:0px;">
			<div class="conrightt2" style="margin: auto;font-size: 11pt;">
				<span>当日买方申报电量:</span><span id="buySumElectricity" class="avenum">0</span><span class="avenum">&nbsp;MWh</span>&nbsp;&nbsp;&nbsp;
				<span>当日卖方申报电量:</span><span id="saleSumElectricity" class="avenum">0</span><span class="avenum">&nbsp;MWh</span>&nbsp;&nbsp;&nbsp;
				<span >当日买方成交电量:</span><span id="buyClearSumElectricity" class="avenum">0</span><span class="avenum">&nbsp;MWh</span>&nbsp;&nbsp;&nbsp;
				<span >当日卖方成交电量:</span><span id="saleClearSumElectricity" class="avenum">0</span><span class="avenum">&nbsp;MWh</span>&nbsp;&nbsp;&nbsp;
				<span >成交笔数:</span><span id="clearNum" class="avenum">0</span><span >笔</span><span class="avenum">&nbsp;</span>
			</div>
			<div class="conrightt2" style="margin: auto;font-size: 18pt;">
				<span>交易通道最大成交电量：<span id="pathMaxClearName"></span>&nbsp;&nbsp;</span><span id="pathMaxClearElectricity" class="avenum">0</span><span class="avenum">&nbsp;MWh</span>&nbsp;&nbsp;&nbsp;
				<span class="pdl30"> 最大成交电力:</span><span id="pathMaxInterval" class="avenum"></span> &nbsp;&nbsp;<span id="pathMaxIntervalPower"  class="avenum">  0</span><span class="avenum">&nbsp;MW</span></div>
			
			</div>
			<div style="width:1197px;height:300px;margin:auto;margin-left:10px;border: solid 1px #dcdcdb;border-bottom:0px;">
			<div id="survey_chart1" style="float: left;width:295px;height:300px"></div>
			<div id="survey_chart2" style="float: left;width:295px;height:300px"></div>
			<div id="survey_chart3" style="float: left;width:295px;height:300px"></div>
			<div id="survey_chart4" style="float: left;width:295px;height:300px"></div>
			</div>
			<div id="div_surveytable" style="width:1197px;height:200px;margin:auto;margin-top:0px;margin-left:10px;border: solid 1px #dcdcdb;border-top:0px;">
			<div style="float: left;width:300px;">
			<table id="survey_table1"  class="table table-hover"  style="border-left: 0px;">
				<tr class="th" style="border-left: 0px;" align="center"><td  colspan="2"   style="border-left: 0px;">购电省申报电量</td></tr>
				<!-- 
				<tr class="tr" style="border-left: 0px;" ><td width="50">浙江</td><td width="50">1000</td></tr>
				<tr class="" style="border-left: 0px;" ><td width="50">上海</td><td width="50">1000</td></tr>
				<tr class="tr" style="border-left: 0px;" ><td width="50">江苏</td><td width="50">1000</td></tr>
				 -->
			</table>
			</div>
			<div style="float: left;width:299px;">
			<table id="survey_table2" class="table  table-hover" style="border-left: 0px;border-right: 0px;">
				<tr class="th" style="border-left: 0px;" align="center"><td  colspan="2"   ">购电省交易电量</td></tr>
			</table></div>
			<div style="float: left;width:299px;min-width:">
			<table id="survey_table3" class="table " style="border-right: 0px;">
				<tr class="th" style="border-left: 0px;" align="center"><td  colspan="2"   style="border-left: 0px;">售电省申报电量</td></tr>
				
			</table></div>
			<div style="float: left;width:299px;">
			<table id="survey_table4" class="table " style="border-right: 0px;">
				<tr class="th" style="border-left: 0px;" align="center"><td  colspan="2"   ">售电省交易电量</td></tr>
			
			</table></div>
			
			</div>
			<div id="survey-column" style="float: left;width:1197;height:300px;margin-top:10px;margin-left:10px;border: solid 1px #dcdcdb;">
			
			</div>
		</div>
		<div class="mid" id="clear" style="height:80%;width: 1220px;display:none">
		<iframe id="frame" src="${pageContext.request.contextPath }/issue/initClear" style="width: 100%;height: 100%;border:0"></iframe>
		</div>
		<div class="mid" id="map" style="height:80%;width: 1220px;display:none">
		<iframe id="frame_map" src="${pageContext.request.contextPath }/issue/initMap" style="width: 100%;height: 100%;border:0"></iframe>
		</div>
		<div class="mid" id="trade" style="height:80%;width: 1220px;display:none">
			<iframe id="frame_trade" src="${pageContext.request.contextPath }/report/tradeMap" style="width: 100%;height: 100%;border:0"></iframe>
		</div>
		<%-- <div class="mid" id="resultEdit" style="height:80%;width: 1220px;">
		<iframe id="frame_result" src="${pageContext.request.contextPath }/issue/initResult" style="width: 100%;height: 100%;border:0"></iframe>
		</div> --%>
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
		//if(issue.issueType == "lineLimit"){
			//issue.getLineLimitData(treeNode.id);
		//}else{
			//console.info(treeNode);
			issue.getResultData(treeNode.id,treeNode);
		//}
		
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

