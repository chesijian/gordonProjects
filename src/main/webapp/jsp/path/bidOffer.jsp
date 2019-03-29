<%@ page language="java" import="java.util.*,com.state.util.AuthoritiesUtil,com.state.util.VersionCtrl" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.state.com/state" prefix="state"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	boolean isAllowEdit = AuthoritiesUtil.isAllow("li_tdpz_btn_add");
%>
<state:override name="head">
<head>
	<title></title>
	<style>
	body {
	    font-family: "微软雅黑,宋体;
	    font-size: 15px;
	    line-height: 22px;
	    color: #ffffff;
	    -webkit-box-sizing: content-box;
		box-sizing: content-box;
	}
	.ureg {
       color: #ffffff;
       padding-top: 0px;
    }
    #bidOfferTable td {
	    border-left: 1px solid #dcdcdb;
	    border-top: 1px solid #dcdcdb;
	}
	.tableCon {
	    position: fixed;
	    top: 30%;
	    left: 50%;
	    margin: -150px 0 0 -225px;
	    width: 388px;
	    background-color: #FFFFFF;
	    z-index: 1000;
	    font-size: 14px;
	    padding: 10px 10px 30px 30px;
	    border-radius: 5px;
	}
	.pdt20 div {
		padding-left:33px;
		padding-top: 15px;
	}
	.mne a:focus,  .mne a:hover {
	    color: #dcdcdb;
	    text-decoration: none;
	}
	.mne a {
		font-size: 15px;
		font-weight: bold;
	}
	#limitconrightt {
	    border: solid 1px #dcdcdb;
	    height: 40px;
	    padding: 10px;
	    border-top: 0px;
	    border-left: 0px;
	    border-right: 0px;
	}
	</style>
	<link href="${pageContext.request.contextPath }/bootstrap/css/bootstrap.min.css?v=<%=VersionCtrl.getVesrsion()%>" rel="stylesheet">
    <Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/head.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css"> 
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/declare.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/line.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	   
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/common/common-public-1.0.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
    <script src="${pageContext.request.contextPath }/js/jquery/jquery-1.8.3.min.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
    <script src="${pageContext.request.contextPath }/js/jquery/jquery.json.min.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.twbsPagination.min.js"></script>
	<script type="text/javascript">
		var pageIndex = 1;
		var currentPage = 1;
		var totalPage = 1;
		$(function(){
			document.getElementById("tk").style.display = 'none';
			refreshData();
		});
		function refreshData(){
			loadBidOfferData();
			sessionSelect(baseUrl+'/intra/declare/getSessionList','bid_offer_session');
		}
		function addBidOffer(id) {
			document.getElementById(id).style.display = 'block';
		}
		function displayAddPanel(id) {
			document.getElementById(id).style.display = 'none';
		}
		function loadBidOfferData(){
			$('#pagination').empty();  
		    $('#pagination').removeData("twbs-pagination");  
		    $('#pagination').unbind("page");
			var time = parent.getTime();
			var area = parent.getArea();
			$.ajax({
				url : baseUrl+'/intra/declare/getBidfferData',
				type : 'POST',
				dataType : 'json',
				data : {
					time : time,
					area : area,
					pageIndex:currentPage,
					limit:5
				},
				success : function(datas){
					if(datas){
						if(datas['totalPage'] && datas['totalPage'] != 0){
							totalPage = datas['totalPage'];
						}
						var result = datas['data'];
						$('#bidOfferTable tbody').remove();
						$('#lineCount').html(result.length+'条');
						var table = $('#bidOfferTable');
						if(result.length==0){
							$('#lineCount').html('0条');
						}else{
							for(var i=0;i<result.length;i++){
								var tr = '<tr>';
								if(i%2 == 1){
									tr = '<tr class="bgh">';
								}
								tr += '<td align="center">'+result[i].mdate+'</td><td align="center">'+result[i].area+'</td><td align="center">'+result[i].priceMax+'</td><td align="center">'+result[i].priceMin+'</td><td align="center">'+result[i].session+'</td>'
								tr += '</tr>';
								table.append($(tr));
							}
						}
						$("#pagination").twbsPagination({
							totalPages:totalPage,
							visiblePages:"5",
							startPage:currentPage,
							first:"首页",
							prev:"上一页",
							next:"下一页",
							last:"最后一页",
							onPageClick:function(e,page){	
								 // 将当前页数重置为page 
								currentPage = page;  
								loadBidOfferData();
							}
						});
					}
				}
			});
		}
		//得到交易时间段下拉框
		function sessionSelect(url,id){
			$.ajax({
				url : url,
				type : 'POST',
				dataType:'json',
				success : function(result) {
					if (result) {
						var selectNode = $('#'+id);
						var option = '';
						for (var j = 0; j < result.length; j++) {
							selectNode.html("");
							var name = result[j].session;
							option += '<option value="'+name+'">'+name+'</option>';
						}
						selectNode.append(option);
					} 
				},
				error : function(xhr, status) {
					alert("系统错误!");
				}
			});
		}
		function saveBidOffer(){
			var priceMax = new Number($('#price_max').val());
			var priceMaxVal = priceMax.toFixed(3);
			var priceMin = new Number($('#price_min').val());
			var priceMinVal = priceMin.toFixed(3);
			var session = $('#bid_offer_session option:selected').val();
			var area = parent.getArea();
			var time = parent.getTime();
			$.ajax({
				url : baseUrl+'/intra/declare/saveBidOfferInfo',
				type : 'POST',
				dataType : 'json',
				data : {
					priceMaxVal : priceMaxVal,
					priceMinVal : priceMinVal,
					session : session,
					area : area,
					time : time
				},
				success : function(result) {
					//alert(result);
					var rs = result;
					if (rs.status) {
						alert("提交成功!");
						window.location.reload();
					} else {
						alert(rs.msg);
					}
				},
				error : function(xhr, status) {
					alert("系统错误!");
				}
			});
		}
	</script>
</state:override>
<state:override name="content">
	<div>   
         <div id="limitconrightt" class="limitconrightt" style="text-align: center;width:1200px;margin: auto;"> 
					<!-- 	<div class="fl mne"><a href="#" name="正向限额" onclick="limitLine.getLimitLineDataByLimitLineType('正向限额');">上限</a></div> -->
						<c:if test='<%=AuthoritiesUtil.isAllow("Market_Kq_Msg") %>'>
						<div class="fl mne"><a href="${pageContext.request.contextPath }/lineLimit/init" name="限额管理" >跨区通道信息</a></div>
						</c:if>
						<c:if test='<%=AuthoritiesUtil.isAllow("Market_Jytd") %>'>
						<div class="fl mne"><a href="${pageContext.request.contextPath }/path/init" name="通道联络线管理">交易通道配置</a></div>
						</c:if>
						<div class="fl mne"><a href="${pageContext.request.contextPath }/path/bidOffer" name="报价申报" style="color:#D8AD27;font-weight:bold;font-size:16px;">报价申报</a></div>
						<c:if test='<%=AuthoritiesUtil.isAllow("Market_Pd") %>'>
						<%-- <div class="fl mne"><a href="${pageContext.request.contextPath }/intra/market/init" name="可能性判断" >可能性判断</a></div> --%>
						</c:if>
		    </div>	
			<div class="mid">
				<div class="contop">
					<div class="fl"><span class="xmenu">交易通道配置</span><span id="lineCount" class="count"></span></div>
					<div class="rl" style="margin-top: 15px;">
					<c:if test='<%=AuthoritiesUtil.isAllow("Market_Jytd_Add_Btn") %>'>
						<span><a class="btn1" href="javascript:void(0);" onclick="addBidOffer('tk')">+添加</a></span>
					</c:if>
					</div>
					<div class="cl"></div>
				</div>
				<div>
					<div class="fl bd1 ustb">
						<div class="fl">
							<table id="bidOfferTable" width="1170" cellpadding="0" cellspacing="0">
								<thead>
								<th width="7%">时间</th>
								<th width="7%">地区</th>
								<th width="7%">价格上限</th>
								<th width="7%">价格下限</th>
								<th width="7%">交易时间段</th>
								</thead>
							</table>
							<div style="width: 1200px;margin-top: 37px;" >
								<ul id="pagination" class="pagination" style="margin:0px;"></ul>
							</div>
						</div>
						<div class="cl"></div>
					</div>
				</div>
				
		</div>
		<div id="tk" style="display: none;">
			<div class="tableCon">
				<div class="rl"><a class="btn3" href="javascript:void(0);" onclick="displayAddPanel('tk')">x</a></div>
				<div class="cl"></div>
				<input type="hidden" id="bid_offer_id"/>
				<div class="pdt20" >
					<div>
					   价格上限：&nbsp;&nbsp;&nbsp;<input id="price_max"/>
					</div>
					<div>
					   价格下限：&nbsp;&nbsp;&nbsp;<input id="price_min" />
					</div>
					<div>
					   交易时间段：&nbsp;&nbsp;&nbsp;&nbsp;<select id="bid_offer_session" style="width: 80px;"></select>
					</div>
					<div class="cl"></div>
					<div class="pdt20 rl pdr20"><a class="btn1"  href="javascript:void(0);" onclick="saveBidOffer()">提交</a></div>
				</div>
			</div>
			<div class="blbg">&nbsp;</div>
		</div>
	</div>
</state:override>

<%@ include file="/common/block/block.jsp" %>

