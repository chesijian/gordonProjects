<%@ page language="java" import="java.util.*,com.state.util.VersionCtrl" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.state.com/state" prefix="state"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<state:override name="head">
	<title></title>
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/declare.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	<script src="${pageContext.request.contextPath }/js/charts/highcharts.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/charts/modules/exporting.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/state/match.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script type="text/javascript">
		var match = new Match();
		$(function(){
			$('.menu1').find('a[name=发布]').attr('class', 'menufocus');
			var area = $('#area').val();
			if(area=='国调'){
				  var content="<div class='fl mne'><a href='${pageContext.request.contextPath }/match/init' name='输电通道'  style='color:#D1B664;fontWeight:bold'>输电通道</a></div>";
				  $('#limitconrightt').append(content);
			}
			match.getMatch();
			$("#matchMenu li").live('click', function(){match.getMatchData($(this), '交易功率');});
		})
	</script>
</state:override>
<state:override name="content">
	
	<div>
	     <div id="limitconrightt" class="limitconrightt">
				<c:if test='<%=AuthoritiesUtil.isAllow("Issue_Menu_fbxx") %>'>
				<div class="fl mne"><a href="${pageContext.request.contextPath }/issue/init" name="发布信息">发布信息</a></div>
				</c:if>
		</div>
		<div class="mid">
			<div>
				<div class="lmenu">
					<ul id="matchMenu">
					</ul>
				</div>
				<div id="matchDataDiv" class="fl bd1" style="display:none;">
					<div class="conrightt1">
						<div class="fl mne"><a href="#" name="交易功率" onclick="match.getMatchDataByMatchType('交易功率');">交易功率</a></div>
						<div class="fl mne"><a href="#" name="正向限额" onclick="match.getMatchDataByMatchType('正向限额');">正向剩余能力</a></div>
						<div class="fl mne"><a href="#" name="反向限额" onclick="match.getMatchDataByMatchType('反向限额');">反向剩余能力</a></div>
					</div>
					<div class="cl"></div>
					<div class="fl pdl10">
						<table width="968" height="302" cellpadding="0" cellspacing="0">
							<thead>
								<th width="16%" class="cgr">时间</th>
								<th width="7%" class="cgr">0</th>
								<th width="7%" class="cgr">1</th>
								<th width="7%" class="cgr">2</th>
								<th width="7%" class="cgr">3</th>
								<th width="7%" class="cgr">4</th>
								<th width="7%" class="cgr">5</th>
								<th width="7%" class="cgr">6</th>
								<th width="7%" class="cgr">7</th>
								<th width="7%" class="cgr">8</th>
								<th width="7%" class="cgr">9</th>
								<th width="7%" class="cgr">10</th>
								<th width="7%" class="cgr">11</th>
							</thead>
							<tr>
								<td width="16%" class="cgr">00:15</td>
								<td width="7%"><input readonly="true" name="h01" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h05" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h09" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h13" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h17" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h21" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h25" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h29" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h33" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h37" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h41" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h45" class="datainput"></td>
							</tr>
							<tr class="bgh">
								<td width="16%" class="cgr">00:30</td>
								<td width="7%"><input readonly="true" name="h02" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h06" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h10" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h14" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h18" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h22" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h26" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h30" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h34" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h38" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h42" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h46" class="datainput"></td>
							</tr>
							<tr>
								<td width="16%" class="cgr">00:45</td>
								<td width="7%"><input readonly="true" name="h03" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h07" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h11" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h15" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h19" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h23" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h27" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h31" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h35" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h39" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h43" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h47" class="datainput"></td>
							</tr>
							<tr class="bgh">
								<td width="16%" class="cgr">00:00</td>
								<td width="7%"><input readonly="true" name="h04" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h08" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h12" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h16" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h20" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h24" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h28" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h32" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h36" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h40" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h44" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h48" class="datainput"></td>
							</tr>
							<tr>
								<td width="16%" class="cgr">时间</td>
								<td width="7%" class="cgr">12</td>
								<td width="7%" class="cgr">13</td>
								<td width="7%" class="cgr">14</td>
								<td width="7%" class="cgr">15</td>
								<td width="7%" class="cgr">16</td>
								<td width="7%" class="cgr">17</td>
								<td width="7%" class="cgr">18</td>
								<td width="7%" class="cgr">19</td>
								<td width="7%" class="cgr">20</td>
								<td width="7%" class="cgr">21</td>
								<td width="7%" class="cgr">22</td>
								<td width="7%" class="cgr">23</td>
							</tr>
							<tr class="bgh">
								<td width="16%" class="cgr">00:15</td>
								<td width="7%"><input readonly="true" name="h49" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h53" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h57" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h61" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h65" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h69" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h73" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h77" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h81" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h85" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h89" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h93" class="datainput"></td>
							</tr>
							<tr>
								<td width="16%" class="cgr">00:30</td>
								<td width="7%"><input readonly="true" name="h50" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h54" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h58" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h62" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h66" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h70" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h74" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h78" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h82" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h86" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h90" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h94" class="datainput"></td>
							</tr>
							<tr class="bgh">
								<td width="16%" class="cgr">00:45</td>
								<td width="7%"><input readonly="true" name="h51" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h55" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h59" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h63" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h67" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h71" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h75" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h79" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h83" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h87" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h91" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h95" class="datainput"></td>
							</tr>
							<tr>
								<td width="16%" class="cgr">00:00</td>
								<td width="7%"><input readonly="true" name="h52" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h56" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h60" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h64" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h68" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h72" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h76" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h80" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h84" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h88" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h92" class="datainput"></td>
								<td width="7%"><input readonly="true" name="h96" class="datainput"></td>
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

<%@ include file="/common/block/block.jsp" %>

