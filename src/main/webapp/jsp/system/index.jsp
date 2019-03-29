<%@ page language="java" import="java.util.*,com.state.util.AuthoritiesUtil,com.state.util.VersionCtrl" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.state.com/state" prefix="state"%>

<state:override name="head">
	<title></title>
	 <!-- 引入 Bootstrap -->
	 <script src="${pageContext.request.contextPath }/js/jquery/jquery-1.10.2.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
   
     <link href="${pageContext.request.contextPath }/bootstrap/css/bootstrap.min.css?v=<%=VersionCtrl.getVesrsion()%>" rel="stylesheet">
     <link href="${pageContext.request.contextPath }/bootstrap/plugin/js/table/bootstrap-table.min.css?v=<%=VersionCtrl.getVesrsion()%>" rel="stylesheet" />
     <link
	href="${pageContext.request.contextPath }/js/easyui/themes/bootstrap/easyui.css"
	rel="stylesheet" />
	<!--  -->
   <script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/jquery.min.js"></script>
   
   <script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/easyui-lang-zh_CN.js"></script>
     <script src="${pageContext.request.contextPath }/bootstrap/js/bootstrap.min.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/bootstrap/plugin/js/table/bootstrap-table.min.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/bootstrap/plugin/js/table/bootstrap-table-zh-CN.min.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<!--  
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/ui-dialog.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	<script src="${pageContext.request.contextPath }/js/state/dialog/dialog-min.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/state/dialog/dialog-plus.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	-->
	<script src="${pageContext.request.contextPath }/js/My97DatePicker/WdatePicker.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/system/index.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	
	<script type="text/javascript">
	//日前的角色 type_为 0，日内的角色type_为1
	var type_ = 0;
	//日前、日内右侧树的数据
	var rqData,rnData;
	var selectTabId = "accreditData";
	$(function(){
		$('.menu1').find('a[name=系统管理]').attr('class', 'menufocus');
		
      });	
	<!--后来添加 -->
	var timeData = ${dataInfo};
	
	</script>
	<style>
	

	.newlmenu {
	float: left;
	width: 200px;
	
	font-size: 14px;
	
	/*
	border-right: 0px;
	border: solid 1px #dcdcdb;padding-bottom:50px;*/
	overflow:auto;
	/*! height:545px; */
}
	.panel-body-noheader{
		border-top-width:0px;
		border:0px;
	}
	table {
    margin: 0;
    padding: 0;
    border: 0px solid #dcdcdb;
    /*
    border-right: 1px solid #dcdcdb;
    border-bottom: 1px solid #dcdcdb;
    */
    border-left: 0px;
}
.panel-header, .panel-body {
    border-width: 0px;
    border-style: solid;
}
a:hover {
    color: #ffa110 !important;
    text-decoration: none;
}
	</style>
</state:override>
<state:override name="content">			
	<body>

<ul id="myTab" class="nav nav-tabs" style="margin-left:100px;margin-right: 100px;">
<li onclick="changeTab('usersData')"><a href="#usermanager" data-toggle="tab" style="font-size: 10pt;">用户管理</a></li>
   <li onclick="changeTab('accreditData')"><a href="#accredit" data-toggle="tab" style="font-size: 10pt;">权限管理</a></li>
   <li onclick="changeTab('loginData')"><a href="#home" data-toggle="tab" style="font-size: 10pt;">在线用户</a></li>
   <li onclick="changeTab('operateData')"><a href="#ios" data-toggle="tab" style="font-size: 10pt;">操作日志</a></li>
</ul>
<div class="input-append" style="float:left;margin:10px 0px 10px 350px;">
	   <span>开始时间:<input type="text" class="Wdate" style="padding:0px 0px;height:23px;border:1px solid #C3D0DD;" id="startTime" name="startTime" readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" value="" /></span>
	   <span>结束时间:<input type="text" class="Wdate" style="padding:0px 0px;height:23px;border:1px solid #C3D0DD;" id="endTime" name="endTime" readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" value="" /></span>
       <button type="button" id="selbtn" class="btn btn-default" style="padding:0px 15px;height:24px;margin-bottom:2px;" onclick="changeTime()">查询</button> 
</div> 

<!-- ------------------111111111111111---------------------------- -->       
<div id="myTabContent" class="tab-content" style="padding-right: 100px;">
<div class="tab-pane fade " id="usermanager">
		<div id="users" style="margin:10px 0px 0px 100px;width: 92%;text-align: center;" >
		<iframe id="frame" src="${pageContext.request.contextPath }/manager/usermanager" style="width: 100%;height: 100%;border:0"></iframe>
		</div>
   </div>
   <div class="tab-pane fade in active" id="accredit" style="height:100%;">   
       <div class="row" style="height:100%;"> 
          
        <table id="table_accredit" style="width:90%;height:100%;margin:10px 0px 0px 115px;">
        	<tr height="100%;" style="border-top: solid 1px #dcdcdb;border-bottom: solid 1px #dcdcdb;border-right: solid 1px #dcdcdb;">
        		<td width="15%" height="100%" class="newlmenu" title="功能列表">
        			<li class="bghh" style="cursor:pointer;">
							角色权限授权
							<div class="cl"></div>
							</li>
					<input id="roleUid" type="hidden">
        		</td>  
        		<td width="25%" height="100%" style="vertical-align: top;">
        			<table style="border: none;" width="100%" id="leftTable" >
        			
        			</table>   
        		</td>
        		<td width="60%" style="margin: 0px;border-left: 0px;vertical-align: top;border-left: 1px solid #dcdcdb;" height="" >
        			<div class="conrightt1" style="border: 0;height: 35px">		

				    
						    <label>权限菜单</label>
							<div class="rl" >
								<a class='btn1' style='margin:0px;color:#06938e;' href="#" onclick='saveData();'>保存</a>
							</div>					

		
						</div>
        			<div  class="newlmenu" style="width: 100%;border-top:1px solid #dcdcdb;border-left: 0px;heigth:">										

				
						<div style="width: 100%;cursor:pointer;padding-left: 7px;overflow-y: auto;height: 460px;" >
							<ul id="right_menu">
								
							</ul>
						</div>	
					</div>
        		</td>
        	</tr>
        </table>
      
		
		
     </div>                       

   </div>

   <!-- ------------------111111111111111---------------------------- -->  
   <div class="tab-pane fade " id="home">
		<div style="margin:10px 0px 0px 100px;width: 92%;text-align: center;" >
			
					
					<table  class="table table-hover"  id="loginData" >
                                                
                    </table>
					
		</div>
   </div>
   <div class="tab-pane fade" id="ios">
      
		<div style="margin:10px 0px 0px 100px;width: 92%;text-align: center;" >
			
						<table class="table table-hover" id="operateData">
						
						
						</table>
	</div>
   </div>
  
</div>


</body>
	
</state:override>

</HEAD>
<%@ include file="/common/block/block.jsp" %>



