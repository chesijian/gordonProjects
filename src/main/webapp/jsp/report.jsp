<%@ page language="java" import="java.util.*,com.state.util.VersionCtrl" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.state.com/state" prefix="state"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<state:override name="head">
	<title></title>
	<script src="${pageContext.request.contextPath }/js/jquery/jquery-1.10.2.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
	<!-- 引入 Bootstrap -->
     <link href="${pageContext.request.contextPath }/bootstrap/css/bootstrap.min.css?v=<%=VersionCtrl.getVesrsion()%>" rel="stylesheet">
     <!-- 包括所有已编译的插件 -->
     <script src="${pageContext.request.contextPath }/bootstrap/js/bootstrap.min.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<link href="${pageContext.request.contextPath }/bootstrap/plugin/js/table/bootstrap-table.min.css?v=<%=VersionCtrl.getVesrsion()%>" rel="stylesheet" />
	<script src="${pageContext.request.contextPath }/bootstrap/plugin/js/table/bootstrap-table.min.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/bootstrap/plugin/js/table/bootstrap-table-zh-CN.min.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/report.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.twbsPagination.min.js"></script>
	<style>
	.dcss{
    color:#D1B664;
    margin: 10px 10px 10px 20px;
	fontWeight:bold;
	}
	.ncss{
    color:#7f7f7f;
    margin: 10px 10px 10px 20px;
	fontWeight:bold;
	}
	a:hover {
    color: #ffa110 !important;
    text-decoration: none;
}
	</style>
	<script type="text/javascript">
	function post(url, params) {
	    var temp = document.createElement("form");
	    temp.action = url;
	    temp.method = "post";
	    temp.style.display = "none";
	    for (var x in params) {
	        var opt = document.createElement("input");
	        opt.name = x;
	        opt.value = params[x];
	        temp.appendChild(opt);
	    }
	    document.body.appendChild(temp);
	    temp.submit();
	    return temp;
	} 
	
	</script>
</state:override>
<state:override name="content">			
<body>
 <input type="hidden" name="type" id="type" value="${type}"/>
 <input type="hidden" name="str" id="str" value="${str}"/>
<ul id="myTab" class="nav nav-tabs" style="margin-left:100px">
	<li id="a" onclick="changeTab('measureData')"><a href="#measureData" data-toggle="tab" style="font-size: 10pt;">计量数据</a></li>
   <c:if test='<%=AuthoritiesUtil.isAllow("Report_Menu_dqxx") %>'>
     <li onclick="changeTab('area','year')"><a href="#home" data-toggle="tab" >地区信息</a></li>
   </c:if>
   <li onclick="changeTab('area','year')"><a href="#home" data-toggle="tab" >地区信息</a></li>
   <li id="b" onclick="changeLiId('b')"><a href="#resultData" data-toggle="tab" >日报展示</a></li>
   <li onclick="changeLiId('c')"><a href="#weekData" data-toggle="tab" >周报展示</a></li>
   <c:if test='<%=AuthoritiesUtil.isAllow("Report_Menu_llxx") %>'>
     <!-- <li onclick="changeTab('line','year')"><a href="#ios" data-toggle="tab">联络线信息</a></li> -->
   </c:if>
</ul>
		<div class="input-append" style="float:left;margin:10px 0px 10px 350px;">
			   <span>开始时间:<input type="text" class="Wdate" id="startTime"  style="padding:0px 0px;height:23px;border:1px solid #C3D0DD;" name="startTime" readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" value="" /></span>
			   <span>结束时间:<input type="text" class="Wdate" id="endrime"  style="padding:0px 0px;height:23px;border:1px solid #C3D0DD;" name="endTime" readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" value="" /></span>
               <button type="button" id="selbtn" class="btn btn-default" style="padding:0px 15px;height:24px;margin-bottom:2px;" onclick="changeTime()">查询</button> 
               <select id="tabType" style="padding:0px 15px;height:23px;" onchange="changeType(this.value)"><option value="year">年度</option>
              <option value="month">月度</option>
               </select>
               <span><a class='btn1' href='#' onclick='exportExcel();'>*导出</a></span>
         </div> 
         <!-- 
       <div class="dropdown" style="float: right">
     <button type="button" class="btn dropdown-toggle" id="dropdownMenu1" 
      data-toggle="dropdown">
                 类型 
      <span class="caret"></span>
     </button>
     <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
      <li role="presentation">
         <a role="menuitem" tabindex="-1"  onclick="changeType('year')">年度</a>
      </li>
      <li role="presentation">
         <a role="menuitem" tabindex="-1" onclick="changeType('month')" >月度</a>
      </li>
     </ul>
    </div> -->
<div id="myTabContent" class="tab-content" style="margin-left: auto;">
<div class="tab-pane fade " id="measureData">
		<div id="users" style="margin:0px 0px 0px 60px;width: 95%;text-align: center;" >
		<iframe id="frame" src="${pageContext.request.contextPath }/metre/measure" style="width: 100%;height: 100%;border:0"></iframe>
		</div>
   </div>
   <div class="tab-pane fade " id="resultData">
		<div id="result" style="margin:0px 0px 0px 60px;width: 95%;text-align: center;" >
		<iframe id="frame" src="${pageContext.request.contextPath }/issue/dailyMap" style="width: 100%;height: 100%;border:0"></iframe>
		</div>
   </div>
   <div class="tab-pane fade " id="weekData">
		<div id="week" style="margin:0px 0px 0px 60px;width: 95%;text-align: center;" >
		<iframe id="frame" src="${pageContext.request.contextPath }/issue/exportWeekData" style="width: 100%;height: 100%;border:0"></iframe>
		</div>
   </div>
   <div class="tab-pane fade in active" id="home">
      <div>
		<div style="margin:10px 0px 0px 100px;width: 1200px;text-align: center;" >
			<div>
				<div>
					<div style="padding-left:0px;">
						<table class="table table-hover" id="areaData" style="margin-top: 50px;">
							
						</table>
						<div style="text-align: center;">
							<ul id="pagination" class="pagination" style="margin:0px;"></ul>
						</div>						
					</div>
					
				</div>
			</div>
		</div>
	</div>
   </div>
   <div class="tab-pane fade" id="ios">
      <div>
        
		<div style="margin:10px 0px 0px 100px;width: 1200px;text-align: center;" >
			<div>
				<div>
					<div style="padding-left:0px" id="yearLineDiv">
						<table class="table table-hover" id="lineData">
						
						</table>
						<div style="text-align: center;">
							<ul id="pagination1" class="pagination" style="margin:0px;"></ul>
						</div>						
					</div>
				</div>
					
				</div>
			</div>
		</div>
	</div>
   </div>
  
</div>


</body>
</state:override>
<script type="text/javascript">
function reflsh(){
	alert("jjjjj");
	$("#"+liId).addClass("active");
}
/**
 * 导出数据
 */
function exportExcel(){
	
	var startTime=$("#startTime").val();
	var endTime=$("#endTime").val();
	var str=$("#str").val();
	var type=$("#type").val();
	window.open("exportExcel?startTime="+startTime+"&endTime="+endTime+"&str="+str+"&type="+type);
}

</script>
<%@ include file="/common/block/block.jsp" %>

