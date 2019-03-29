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
	 <script src="${pageContext.request.contextPath }/js/state/limitLine.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	
	<script type="text/javascript">
		var limitLine = new LimitLine();
		var _limitType = ${limitType};
		$(function(){
			var area = $('#area').val();
			if(<%=!AuthoritiesUtil.isAllow_LineL_Input() %>){
				//$("input[class='datainput']").attr('readonly','disabled');
			}else{
				
				 var content= "<a class='btn1' id='btn_save' style='margin:0px;color:#06938e;' href='#' onclick='limitLine.updateLimitLine();'><span style='color: #06938e;'>保存</span></a>";
				  var content1 = "<a class='btn1' style='margin:10px;color:#06938e;' href='#' onclick='limitLine.getLimitAndPlanValue();'>获取计划与限额</a>";
				  var content2 = "<a id='kyrlfb' class='btn1' style='margin:0px;color:#06938e;' href='#' onclick='limitLine.exportToPlan();'>可用容量发布</a>";
				  var content3 = "<a class='btn1' style='margin-left:10px;margin-right:0px;color:#06938e;' href='#' onclick='limitLine.localExport();'>本地导出</a>";
				  if(<%=AuthoritiesUtil.isAllow("li_btn_save") %>){
					  $('#btnLoad').append(content);
				  }
				  if(<%=AuthoritiesUtil.isAllow("li__btn_getJhxe") %>){
					  $('#btngetPlan').append(content1); 
				  }
				  if(<%=AuthoritiesUtil.isAllow("li_btn_rlfb") %>){
					  $('#btngetPlan').append(content2); 
				  }
				  if(<%=AuthoritiesUtil.isAllow("li_btn_ExportLocal") %>){
					  $('#btngetPlan').append(content3);
				  }
			}
			<%-- var pathContent="";
			if(<%=SessionUtil.isState() %>){
				  $('#limitconrightt').append(pathContent);
			  } --%>
			$('.menu1').find('a[name=基础数据]').attr('class', 'menufocus');
			limitLine.getLimitLine();
			$("#limitLineMenu li").live('click', function(){limitLine.getAllLimitLineData($(this), '差值空间');});
			//$("#limitLineMenu li").live('dblclick', function(){limitLine.changeLimitLineName();});
			$("#limitLineMenu li").live('blur', function(){limitLine.finishChangeLimitLineName();});
		//	$("#limitLineDataDiv input").live("keydown", function(e){limitLine.copyTableValue($(this), e);});
			$("#limitLineDataDiv input").live("click", function(e){limitLine.changeData();});
			//getBtnStatus();
			 $(".datainput").keyup(function(){    
	                $(this).val($(this).val().replace(/[^-0-9.]/g,''));    
	            }).bind("paste",function(){  //CTR+V事件处理    
	                $(this).val($(this).val().replace(/[^0-9.]/g,''));     
	            }).css("ime-mode", "disabled"); //CSS设置输入法不可用
		});
		function lineLimitPageBtnStatus(flag){
			if(flag==1){
				$('#btngetPlan a').each(function(i){
					if($(this).attr('id')=='kyrlfb'){
						$(this).text('已发布');
					}
				});
				var span = '<span id="ztfb" style="color: #c19d30; font-size: 16px;padding-right: 50px;">发布状态：已发布</span>';
				if(!_isState){
					$('#btngetPlan span').each(function(i){
						if($(this).attr('id')=='ztfb'){
							$(this).remove();
						}
					});
					$('#btngetPlan').prepend(span);
				}
			}else{
				$('#btngetPlan a').each(function(i){
					if($(this).attr('id')=='kyrlfb'){
						$(this).text('信息发布');
					}
				});
				//$('#btngetPlan a').text('信息发布');
				var span = '<span id="ztfb" style="color: #c19d30; font-size: 16px;padding-right: 50px;">发布状态：未发布</span>';
				if(!_isState){
					$('#btngetPlan span').each(function(i){
						if($(this).attr('id')=='ztfb'){
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
						var node1,node2,node3,node4,node5,kyrlfbBtnNode;
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
							}else if(key=='kyrlfbBtn'){
								kyrlfbBtnNode = result[key];
							}
						}
						if(kyrlfbBtnNode==1){
							$('#btngetPlan a').each(function(i){
								if($(this).attr('id')=='kyrlfb'){
									$(this).text('已发布');
								}
							});
							var span = '<span id="ztfb" style="color: #c19d30; font-size: 16px;padding-right: 50px;">发布状态：已发布</span>';
							if(!_isState){
								$('#btngetPlan span').each(function(i){
									if($(this).attr('id')=='ztfb'){
										$(this).remove();
									}
								});
								$('#btngetPlan').prepend(span);
							}
						}else{
							$('#btngetPlan a').each(function(i){
								if($(this).attr('id')=='kyrlfb'){
									$(this).text('信息发布');
								}
							});
							//$('#btngetPlan a').text('信息发布');
							var span = '<span id="ztfb" style="color: #c19d30; font-size: 16px;padding-right: 50px;">发布状态：未发布</span>';
							if(!_isState){
								$('#btngetPlan span').each(function(i){
									if($(this).attr('id')=='ztfb'){
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
		$(function(){
	        var startPoint = null;
			$('#table_edit').find('input').focus(function(event){
				limitLine.inputVal = this.value;
			});
			$('#table_edit').find('input').keyup(function(event){
				limitLine.inputVal = this.value;
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
							$('#table_edit input[name=' + key + ']').val(limitLine.inputVal);
					    }
		    		}
				    
				  startPoint = null;
			    });//这个是鼠标键，是你鼠标左击放开后的效果
			
			
		});
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
	    		<c:if test='<%=AuthoritiesUtil.isAllow("li_menu_kqtd") %>'>
						<div class="fl mne"><a href="${pageContext.request.contextPath }/lineLimit/init" name="限额管理"  style="color:#D1B664;fontWeight:bold">跨区通道信息</a></div>
				</c:if>
				<c:if test='<%=SessionUtil.isState()%>'>
						<div class='fl mne'><a href='${pageContext.request.contextPath }/path/init' name='通道联络线管理' >交易通道配置</a></div>
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
						<div class="fl mne"><a href="#" name="可用容量" onclick="limitLine.getLimitLineDataByLimitLineType('可用容量');">可用容量</a></div>
						<c:if test='<%=SessionUtil.isState() %>'>
					    <div class="fl mne"><a href="#" name="日前计划值" onclick="limitLine.getLimitLineDataByLimitLineType('日前计划值');">日前预计划</a></div>
						<!--
						<div class="fl mne"><a href="#" name="日内计划值" onclick="limitLine.getLimitLineDataByLimitLineType('日内计划值');">日内计划</a></div>
						  -->
						<c:if test="${limitType=='0'}">
						  <div class="fl mne"><a href="#" name="动态正向限额" onclick="limitLine.getLimitLineDataByLimitLineType('动态正向限额');">通道限额</a></div>
						</c:if>
						<c:if test="${limitType=='1'}">
						  <div class="fl mne"><a href="#" name="静态正向限额" onclick="limitLine.getLimitLineDataByLimitLineType('静态正向限额');">通道限额</a></div>
						</c:if>
						</c:if>
						<!--  
						<div class="fl mne"><a href="#" name="交易功率" onclick="limitLine.getLimitLineDataByLimitLineType('交易功率');">交易功率</a></div>
						-->
						<div id="btnLoad" class="rl">
						<!--
						<c:if test='<%=AuthoritiesUtil.isAllow("Li_Btn_Export") %>'>
						  <a class='btn1' style='margin:0px;color:#06938e;' href='#' onclick='limitLine.exportLimitData();'>导出</a>
						</c:if>
						  -->
						</div>
					</div>
					<div class="cl"></div>
					<div class="fl conrightt2"><span>总电量:</span><span name="sumValue" class="avenum">0</span><span class="avenum">&nbsp;MWh</span>|<span class="pdl30">平均电力:</span><span name="avgValue" class="avenum">0</span><span class="avenum">&nbsp;MW</span></div>
					<div class="cl"></div>
					<div class="fl pdl10">
						<table id="table_edit" width="968" height="302" cellpadding="0" cellspacing="0">
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
								<td width="7%"><input name="h01" class="datainput" ></td>
								<td width="7%"><input name="h05" class="datainput" ></td>
								<td width="7%"><input name="h09" class="datainput" ></td>
								<td width="7%"><input name="h13" class="datainput" ></td>
								<td width="7%"><input name="h17" class="datainput" ></td>
								<td width="7%"><input name="h21" class="datainput" ></td>
								<td width="7%"><input name="h25" class="datainput" ></td>
								<td width="7%"><input name="h29" class="datainput" ></td>
								<td width="7%"><input name="h33" class="datainput" ></td>
								<td width="7%"><input name="h37" class="datainput" ></td>
								<td width="7%"><input name="h41" class="datainput" ></td>
								<td width="7%"><input name="h45" class="datainput" ></td>
							</tr>
							<tr class="bgh">
								<td width="16%" class="cgr">30分</td>
								<td width="7%"><input name="h02" class="datainput" ></td>
								<td width="7%"><input name="h06" class="datainput" ></td>
								<td width="7%"><input name="h10" class="datainput" ></td>
								<td width="7%"><input name="h14" class="datainput" ></td>
								<td width="7%"><input name="h18" class="datainput" ></td>
								<td width="7%"><input name="h22" class="datainput" ></td>
								<td width="7%"><input name="h26" class="datainput" ></td>
								<td width="7%"><input name="h30" class="datainput" ></td>
								<td width="7%"><input name="h34" class="datainput" ></td>
								<td width="7%"><input name="h38" class="datainput" ></td>
								<td width="7%"><input name="h42" class="datainput" ></td>
								<td width="7%"><input name="h46" class="datainput" ></td>
							</tr>
							<tr>
								<td width="16%" class="cgr">45分</td>
								<td width="7%"><input name="h03" class="datainput" ></td>
								<td width="7%"><input name="h07" class="datainput" ></td>
								<td width="7%"><input name="h11" class="datainput" ></td>
								<td width="7%"><input name="h15" class="datainput" ></td>
								<td width="7%"><input name="h19" class="datainput" ></td>
								<td width="7%"><input name="h23" class="datainput" ></td>
								<td width="7%"><input name="h27" class="datainput" ></td>
								<td width="7%"><input name="h31" class="datainput" ></td>
								<td width="7%"><input name="h35" class="datainput" ></td>
								<td width="7%"><input name="h39" class="datainput" ></td>
								<td width="7%"><input name="h43" class="datainput" ></td>
								<td width="7%"><input name="h47" class="datainput" ></td>
							</tr>
							<tr class="bgh">
								<td width="16%" class="cgr">00分</td>
								<td width="7%"><input name="h04" class="datainput" ></td>
								<td width="7%"><input name="h08" class="datainput" ></td>
								<td width="7%"><input name="h12" class="datainput" ></td>
								<td width="7%"><input name="h16" class="datainput" ></td>
								<td width="7%"><input name="h20" class="datainput" ></td>
								<td width="7%"><input name="h24" class="datainput" ></td>
								<td width="7%"><input name="h28" class="datainput" ></td>
								<td width="7%"><input name="h32" class="datainput" ></td>
								<td width="7%"><input name="h36" class="datainput" ></td>
								<td width="7%"><input name="h40" class="datainput" ></td>
								<td width="7%"><input name="h44" class="datainput" ></td>
								<td width="7%"><input name="h48" class="datainput" ></td>
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
								<td width="7%"><input name="h49" class="datainput" ></td>
								<td width="7%"><input name="h53" class="datainput" ></td>
								<td width="7%"><input name="h57" class="datainput" ></td>
								<td width="7%"><input name="h61" class="datainput" ></td>
								<td width="7%"><input name="h65" class="datainput" ></td>
								<td width="7%"><input name="h69" class="datainput" ></td>
								<td width="7%"><input name="h73" class="datainput" ></td>
								<td width="7%"><input name="h77" class="datainput" ></td>
								<td width="7%"><input name="h81" class="datainput" ></td>
								<td width="7%"><input name="h85" class="datainput" ></td>
								<td width="7%"><input name="h89" class="datainput" ></td>
								<td width="7%"><input name="h93" class="datainput" ></td>
							</tr>
							<tr>
								<td width="16%" class="cgr">30分</td>
								<td width="7%"><input name="h50" class="datainput" ></td>
								<td width="7%"><input name="h54" class="datainput" ></td>
								<td width="7%"><input name="h58" class="datainput" ></td>
								<td width="7%"><input name="h62" class="datainput" ></td>
								<td width="7%"><input name="h66" class="datainput" ></td>
								<td width="7%"><input name="h70" class="datainput" ></td>
								<td width="7%"><input name="h74" class="datainput" ></td>
								<td width="7%"><input name="h78" class="datainput" ></td>
								<td width="7%"><input name="h82" class="datainput" ></td>
								<td width="7%"><input name="h86" class="datainput" ></td>
								<td width="7%"><input name="h90" class="datainput" ></td>
								<td width="7%"><input name="h94" class="datainput" ></td>
							</tr>
							<tr class="bgh">
								<td width="16%" class="cgr">45分</td>
								<td width="7%"><input name="h51" class="datainput" ></td>
								<td width="7%"><input name="h55" class="datainput" ></td>
								<td width="7%"><input name="h59" class="datainput" ></td>
								<td width="7%"><input name="h63" class="datainput" ></td>
								<td width="7%"><input name="h67" class="datainput" ></td>
								<td width="7%"><input name="h71" class="datainput" ></td>
								<td width="7%"><input name="h75" class="datainput" ></td>
								<td width="7%"><input name="h79" class="datainput" ></td>
								<td width="7%"><input name="h83" class="datainput" ></td>
								<td width="7%"><input name="h87" class="datainput" ></td>
								<td width="7%"><input name="h91" class="datainput" ></td>
								<td width="7%"><input name="h95" class="datainput" ></td>
							</tr>
							<tr>
								<td width="16%" class="cgr">00分</td>
								<td width="7%"><input name="h52" class="datainput" ></td>
								<td width="7%"><input name="h56" class="datainput" ></td>
								<td width="7%"><input name="h60" class="datainput" ></td>
								<td width="7%"><input name="h64" class="datainput" ></td>
								<td width="7%"><input name="h68" class="datainput" ></td>
								<td width="7%"><input name="h72" class="datainput" ></td>
								<td width="7%"><input name="h76" class="datainput" ></td>
								<td width="7%"><input name="h80" class="datainput" ></td>
								<td width="7%"><input name="h84" class="datainput" ></td>
								<td width="7%"><input name="h88" class="datainput" ></td>
								<td width="7%"><input name="h92" class="datainput" ></td>
								<td width="7%"><input name="h96" class="datainput" ></td>
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
<%@ include file="/common/block/block.jsp" %>

