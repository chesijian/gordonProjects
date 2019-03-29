<%@ page language="java" import="java.util.*,com.state.util.AuthoritiesUtil,com.state.util.VersionCtrl" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.state.com/state" prefix="state"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<state:override name="head">
	<title></title>
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/button.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/demo.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css"> 
	<!-- 引入 Bootstrap -->
    <link href="${pageContext.request.contextPath }/bootstrap/css/bootstrap.min.css?v=<%=VersionCtrl.getVesrsion()%>" rel="stylesheet">

	<style type="text/css">  
     .mid {
	    margin: auto;
		width: 1200px;
		text-align: center;
	}
	.mid #frame {
		height: auto;
	}
	.mne a {
		margin: 2px 10px;
		text-decoration: none;
	}
        </style>  


	<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.twbsPagination.min.js"></script>
	<script src="${pageContext.request.contextPath }/js/state/issue/daily.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script type="text/javascript">
	var pageIndex = 1;
	var daily = new daily();
	var currentPage = 1;
    var areaCurrentPage = 1;  
    var areaTotalPage = 1;  
	
	$(function(){
		showTab(pageIndex);
	});
	function showTab(index){
		pageIndex = index;
		$('#tab'+index).show();
		$('#a_'+index).css("color","#D1B664");
		$('#a_'+index).css("fontWeight","bold");
		$('#tab'+index).siblings("div").each(function(){
			var tabId = $(this).attr('id');
			if(tabId){
				$(this).hide();
				$('#a_'+tabId.substring(tabId.length-1,tabId.length)).css("color","#7F7F7F");
				$('#a_'+tabId.substring(tabId.length-1,tabId.length)).css("fontWeight","");
			}
		});
		if(index==3){
			$('#spanExportPlan').show();
		}else{
			$('#spanExportPlan').hide();
		}
		if(index==2){
			changeTab('area','year');
		}
		if(index == 4){
			if(typeof document.getElementsByTagName("iframe")["frame_4"].contentWindow.refreshChart != "undefined"){
				document.getElementsByTagName("iframe")["frame_4"].contentWindow.refreshChart();
			}
		}
	};
	function changeTab(str,type){
		$('#pagination').empty();  
	    $('#pagination').removeData("twbs-pagination");  
	    $('#pagination').unbind("page"); 
	    currentPage =areaCurrentPage;
	    var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();

		$.ajax({
			url : "getData",
			type : 'POST',
			dataType : 'json',
			data : {
				str : str,
				type:type,
				startTime:startTime,
				endTime:endTime,
				pageIndex:currentPage,
				limit:8
			},
			success : function(result) {
				if (result.list) {
					 if(str=='area'){
						 areaTotalPage = result.totalPage;//将后台数据复制给总页数  
						 $("#areaData").empty();
						/*var areaContent='<thead><th width="7%" class="cgr">地区</th>'+
							'<th width="7%" class="cgr">申报电量</th>'+
							'<th width="7%" class="cgr">出清电量</th>'+
							'<th width="7%" class="cgr">结算电量</th>'+
							'<th width="7%" class="cgr">出清费用</th>'+
							'<th width="7%" class="cgr">结算费用</th>'+
							'<th width="7%" class="cgr"><font color="#f44336">'+ts+'</font></th>'+
							'<th width="7%" class="cgr">说明</th></thead>';*/
						 var areaContent='<thead><th width="7%" class="cgr">地区</th>'+
							'<th width="7%" class="cgr">交易电量</th>'+
							'<th width="7%" class="cgr">成交笔数</th>'+
							/*'<th width="7%" class="cgr">平均电价</th>'+
							'<th width="7%" class="cgr"><font color="#f44336">'+ts+'</font></th>'+
							'<th width="7%" class="cgr">说明</th>'+*/
							'</thead>';
						for(var i=0;i<result.list.length;i++){
							 areaContent=areaContent+'<tr><td width="7%">'+result.list[i].area+'</td>'+
							'<td width="7%">'+new Number(result.list[i].sumq).toFixed(2)+'</td>'+
							'<td width="7%">'+result.list[i].cqq+'</td>';
							/*'<td width="7%">'+result.list[i].cqp+'</td>';*/
							
							/*if(type=='year'){
							 areaContent=areaContent+'<td width="7%" align="center"><font color="#f44336">'+result.list[i].year+'</font></td>';
							}else if(type=='month'){
							 areaContent=areaContent+'<td width="7%" align="center"><font color="#f44336">'+result.list[i].year+'-'+result.list[i].month+'</font></td>';
							}*/
							areaContent=areaContent+'</tr>';
						}
						$("#areaData").append(areaContent);
						$("#pagination").twbsPagination({
							totalPages:areaTotalPage,
							visiblePages:"5",
							startPage:areaCurrentPage,
							first:"首页",
							prev:"上一页",
							next:"下一页",
							last:"最后一页",
							onPageClick:function(e,page){	
								 // 将当前页数重置为page 
								var str=$("#str").val();
								var type=$("#type").val();
								//var startTime=$("#startTime").val();
								//var endTime=$("#endTime").val();
								areaCurrentPage = page;  
								changeTab(str,type);
							}
						});
					}
				} 
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
           });
	}
	function changeTime(){
		areaCurrentPage=1;
		lineCurrentPage=1;
		var str=$("#str").val();
		var type=$("#type").val();
		changeTab('area','year');
	}
	function changeType(type){
		$("#type").val(type);
		areaCurrentPage=1;
		lineCurrentPage=1;
		var str=$("#str").val();
		changeTab('area','year');
	}
	/**
	 * 导出数据
	 */
	function exportExcel(){
		
		var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		var str=$("#str").val();
		var type=$("#type").val();
		window.open( "exportExcel?startTime="+startTime+"&endTime="+endTime+"&str="+str+"&type="+type);
	}
	/**
	 * 导出日交易单
	 */
	function exportDailyDoc(){
		mask("正在导出...");
		var time = $('#time').val();
		$.ajax({
			url : baseUrl+'/issue/getExportDailyDoc',
			type : 'POST',
			dataType : 'json',
			async : false,
			data : {
				time : time
			},
			success : function(result) {
				unmask();
				if(result != undefined && result != null){
					if(result['status']){
						var results = result['msg'];
						var url =  baseUrl+"/issue/exportDailyDoc?fileName="+encodeURI(encodeURI(results));;
						window.open(url);
						/* for(var key in result){
							var url =  baseUrl+"/issue/exportDailyDoc?fileName="+encodeURI(encodeURI(result[key]));;
							window.open(url);
						} */
					}else{
						alert(result['msg']);
					}
					//console.info(result);
					
				}else{
					alert("没有交易数据！");
				}
			}
		});
		//var url = "exportDailyDoc?time="+$('#time').val();
		//window.open(url);
	};
	</script>
</state:override>
<state:override name="content">
		<input type="hidden" name="type" id="type" value="${type}"/>
 		<input type="hidden" name="str" id="str" value="${str}"/>
		<div class="mid">
			<div class="contop" style="padding: 10px 0px 13px;">
				
				<div id="limitconrightt" class="limitconrightt" style="text-align: center;width:1200px;margin: auto;border:0px;padding: 5px;padding-left: 0px;">
			 	<div class="fl mne"><a id="a_1" href='javascript:;' onclick='showTab(1);return false;' name="计量数据" style="color:#7F7F7F;fontWeight:bold">计量数据</a></div>
			 	<div class="fl mne"><a id="a_2" href='javascript:;' onclick='showTab(2);return false;' name="地区信息" style="color:#7F7F7F;fontWeight:bold">地区信息</a></div>
			 	<div class="fl mne"><a id="a_3" href='javascript:;' onclick='showTab(3);return false;' name="日报展示" style="color:#7F7F7F;fontWeight:bold">日报展示</a></div>
			 	<div class="fl mne"><a id="a_5" href='javascript:;' onclick='showTab(5);return false;' name="交易单" style="color:#7F7F7F;fontWeight:bold">交易单展示</a></div>
			 	<div class="fl mne"><a id="a_4" href='javascript:;' onclick='showTab(4);return false;' name="周报展示" style="color:#7F7F7F;fontWeight:bold">周报展示</a></div>
			 	
				 <div id="btngetPlan" class="rlplan" style="padding-right: 3px;margin-top: 0px;">
					<c:if test='<%=AuthoritiesUtil.isAllow("Report_Menu_DayDaiy_Export") %>'>
				     <span id="spanExportPlan" style="display: none;padding-right: 0px;"><a class='btn1' style='margin-right: -5px;color:#06938e;' href='#' onclick='daily.exportDailyExcel();'>导出日报</a></span>
				   </c:if>
					<span><a class='btn1' style='margin-right:12px;color:#06938e;' href='#' onclick='exportDailyDoc();'>导出交易单</a></span>
					
				  </div> 
				  </div>
			    </div> 
			
			<div id="tab1" style="display: none;border-top: solid 1px #dcdcdb;padding-top: 0px;margin: 0 auto;height: 700px;">
			<iframe id="frame" src="${pageContext.request.contextPath }/metre/measure" style="width: 100%;height: 100%;border:0"></iframe>
			</div>
			<div id="tab2" style="display: none;border-top: solid 1px #dcdcdb;padding-top: 0px;margin: 0 auto;height: 700px;">
				<div class="input-append" style="float:left;margin:10px 0px 10px 350px;">
				   <span>开始时间:<input type="text" class="Wdate" id="startTime"  style="padding:0px 0px;height:23px;border:1px solid #C3D0DD;" name="startTime" readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" value="" /></span>
				   <span>结束时间:<input type="text" class="Wdate" id="endTime"  style="padding:0px 0px;height:23px;border:1px solid #C3D0DD;" name="endTime" readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" value="" /></span>
	               <button type="button" id="selbtn" class="btn btn-default" style="padding:0px 15px;height:24px;margin-bottom:2px;" onclick="changeTime()">查询</button> 
<!-- 	               <select id="tabType" style="padding:0px 15px;height:23px;" onchange="changeType(this.value)"><option value="year">年度</option>
	              <option value="month">月度</option>
	               </select> -->
	               <span><a class='btn1' href='#' onclick='exportExcel();'>*导出</a></span>
	         </div> 
				<div style="padding-left:0px">
					<table class="table table-hover" id="areaData" style="margin-top:50px">
						
					</table>
					<div style="width: 1200px;" >
						<ul id="pagination" class="pagination" style="margin:0px;"></ul>
					</div>						
				</div>
			</div>
			<div id="tab3" style="display: none;border-top: solid 1px #dcdcdb;padding-top: 0px;margin: 0 auto;height: 700px;">
				<iframe id="frame_3" src="${pageContext.request.contextPath }/issue/dailyMap" style="width: 100%;height: 100%;border:0"></iframe>
			</div>
			
			<div id="tab5" style="display: none;border-top: solid 1px #dcdcdb;padding-top: 0px;margin: 0 auto;height: 700px;">
				<iframe id="frame_5" src="${pageContext.request.contextPath }/report/tradeMap" style="width: 100%;height: 100%;border:0"></iframe>
			</div>
			<div id="tab4" style="display: none;border-top: solid 1px #dcdcdb;padding-top: 0px;margin: 0 auto;height: 700px;">
			<iframe id="frame_4" src="${pageContext.request.contextPath }/issue/exportWeekData" style="width: 100%;border:0;height: 700px;"></iframe>
			</div>
			</div>
		</div>
	</div>
</state:override>


    
<%@ include file="/common/block/block.jsp" %>


