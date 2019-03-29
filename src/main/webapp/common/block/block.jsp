<%@page import="com.state.util.SessionUtil"%>
<%@ page language="java" import="java.util.*,com.state.util.AuthoritiesUtil,com.state.util.VersionCtrl" pageEncoding="UTF-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.state.com/state" prefix="state" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
//System.out.println("============"+AuthoritiesUtil.isAllow_Menu_UserManager());
%>
<!DOCTYPE html>
<html >
<head>
	  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/ui/themes/icon.css?v=<%=VersionCtrl.getVesrsion()%>">
    <Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/head.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css"> 
    <Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/ui-dialog.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/common.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/declare.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	<style type="text/css">
	.css_a_flow{
		font-size:15pt;
		font-family:微软雅黑,宋体;
		margin-right:10px;
	}
	.btncolor{
    	background-color: green;
	}

	</style>
	<!--   -->
	<link href="${pageContext.request.contextPath }/bootstrap/css/bootstrap-btn.css?v=<%=VersionCtrl.getVesrsion()%>" rel="stylesheet">
   
    <script src="${pageContext.request.contextPath }/js/jquery/jquery-1.8.3.min.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
    <script src="${pageContext.request.contextPath }/js/jquery/jquery.json.min.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
	<script src="${pageContext.request.contextPath }/js/My97DatePicker/WdatePicker.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/common/common-public-1.0.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
    <script src="${pageContext.request.contextPath }/js/state/dialog/dialog-min.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/state/dialog/dialog-plus.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/state/dialog/sea.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	
	<state:block name="head"></state:block>
</head>
<body>
	
	<state:block name="header">
		<div class="ttbg">
			<div class="mid">
				<div class="fl pdt10"><img src="${pageContext.request.contextPath }/img/logo.png"></div>
				 <!-- --> 
				<div style="color: white; float: left; padding-top: 20px; color: white;font-family: 黑体;font-size: 30pt;font">富余可再生能源跨省区现货市场</div>
				 
				
				<input id="jsppath" type="hidden" value="${jspType}" ></input>
				<div class="rl ureg">
					<span style="font-size:16px">${userInfo.area}</span>
					<span style="font-size:16px;margin-right:10px;"><a href="javascript:;" onclick="editPwd('${userInfo.id}');return false;" style="color:#ffffff;font-size:16px">${userInfo.mname}</a></span>
					<span><input type="text" class="Wdate" id="time" readonly="readonly" style="color:#0C0B0B;width: 110px;height:20px;" onfocus="WdatePicker({isShowWeek:true,onpicked:function(){changeDate();}});" value="${userInfo.matchDate}" /></span>
					<!-- <span><input id="d411" class="Wdate" type="text" onClick="WdatePicker({skin:'whyGreen',minDate:'2006-09-10',maxDate:'2008-12-20'})"/></span> -->
					
					
					<input id="user" name="user" value="${userInfo.mname}" type="hidden"/>
					<input id="area" name="area" value="${userInfo.area}" type="hidden"/>
					<input id="a2" name="a2" value="${a2}" type="hidden"/>
					<input id="a3" name="a3" value="${a3}" type="hidden"/>
					<span><a href="${pageContext.request.contextPath}/login/logout" style="color:#ffffff;font-size:16px">注销</a></span>
					<span><a style="background-image: '../img/icon/cog.png'"></a></span>
				</div>
			</div>
			<div class="cl"></div>
			
		</div>
		  <c:set var="user" value="${sessionScope.userInfo}"></c:set>
		<div style="heigth:50px;background-color: "><div class="mid" style="margin-top: 15px;margin-bottom: 10px;">
			<table cellspacing="0" cellpadding="0" style="border: 0px;margin: auto;" id="process_btn">
			<tr>
			<c:if test='<%=AuthoritiesUtil.isAllow("Li") %>'>
			<td style="border:0px;"><a href="${pageContext.request.contextPath }/lineLimit/init" class="big_btn big_btn-primary css_a_flow btncolor" style="background-color: #06938e;margin-right:80px;"  name="node1">信息公告</a></td>
			</c:if>
			<c:if test='<%=AuthoritiesUtil.isAllow("De") %>'>
			<c:if test="${user.drole=='all'}">
			<td style="border:0px;"><a href="${pageContext.request.contextPath }/declare/init" class="big_btn big_btn-primary css_a_flow btncolor" style="background-color: green;" name="node2"> 交易申报</a></td>
			</c:if>
			<c:if test="${user.drole=='buy'}">
			<td style="border:0px;"><a href="${pageContext.request.contextPath }/declare/init"  class="big_btn big_btn-primary css_a_flow btncolor" style="background-color: green;" name="node2"> 购电报价</a></td>
			</c:if>
			<c:if test="${user.drole=='sale'}">
			<td style="border:0px;"><a href="${pageContext.request.contextPath }/declare/init"  class="big_btn big_btn-primary css_a_flow btncolor" style="background-color: green;" name="node2"> 售电报价</a></td>
			</c:if>
			</c:if>
			<c:if test='<%=AuthoritiesUtil.isAllow("De_Btn_Match") %>'>
			<td style="border:0px;"><img style="padding-top:5px;padding-right: 10px;" height="30px" width="30px" src="../img/icon/pic1.png"></td>
			<td style="border:0px;"><a  href='#' onclick='newMatch.matchData();' class="big_btn big_btn-primary css_a_flow btncolor" style="background-color: green;"  name="node3"> 集中竞价</a></td>
			</c:if>
			<c:if test='<%=AuthoritiesUtil.isAllow("Issue_Menu_fbxx") %>'>
			<td style="border:0px;"><img style="padding-top:5px;padding-right: 10px;" height="30px" width="30px" src="../img/icon/pic1.png"></td>
			<td style="border:0px;"><a href="${pageContext.request.contextPath }/issue/init"  class="big_btn big_btn-primary css_a_flow btncolor" style="background-color: green;" name="node4">交易结果</a></td>
			</c:if>
			<c:if test='<%=AuthoritiesUtil.isAllow("Issue_Menu_result") %>'>
			<td style="border:0px;"><img style="padding-top:5px;padding-right: 10px;" height="30px" width="30px" src="../img/icon/pic1.png"></td>
			<td style="border:0px;"><a href="${pageContext.request.contextPath }/issue/result"  class="big_btn big_btn-primary css_a_flow btncolor" style="background-color: green;" name="node5">执行结果</a></td>
			</c:if>
			<c:if test='<%=AuthoritiesUtil.isAllow("manager")%>'>
			<td style="border:0px;"><a href="${pageContext.request.contextPath }/report/init"  style="margin-left:80px;background-color: #06938e;" class="big_btn big_btn-primary">统计查询</a></td>
			</c:if>
<!-- 			<c:if test='<%=AuthoritiesUtil.isAllow("system")%>'>
			<td style="border:0px;"><a href="${pageContext.request.contextPath }/system/init"  style="margin-left:10px;background-color: #06938e;" class="big_btn big_btn-primary css_a_flow">系统管理</a></td>
			</c:if> -->

			</tr></table>
		</div></div>
		
				
	</state:block>

	<state:block name="content" >
	
	
	</state:block>

	<state:block name="footer"></state:block>
	
	<script type="text/javascript">
	var _isState = <%=SessionUtil.isState() %>;

	//可用容量发布 按钮状态
	var kyrlfbBtn = false;
	//结果发布 按钮状态
	var jgfbBtn = false;
	$(function(){
		//var time = document.getElementById("time");
		//time.addEventListener("input",changeDate,false); 
		var path = $("#jsppath").val();
		if(path=="system"){
			$("#process_btn").hide();
		}
	});
	
	 getPath = function() {
		 
         var path=$("#jsppath").val();
         //var strs = path.split("\/");//linux 
       //  alert(path);
         //var strs = path.split("\\");//windows
         //var jspNmae=strs[strs.length-1];
         //alert(path);
         if(path=="declare"){
         	loadTreeData();
         	// declare.getDeclare();
         }else if(path=="limitLine"){
         	limitLine.refreshLimitLineData();
         }else if(path=="match"){
         	match.getMatch();
         }else if(path=="settleMent"){
        	 window.location.reload();
         	//match.getMatch();
         }else if(path=="issue"){
         	//issue.showData();
         	issue.loadTree();
         }else if(path=="metre"){
        	 limitLine.refreshLimitLineData();
         }else if(path=="result"){
        	 //issueResult.refreshLimitLineData();
        	 executeResult.loadTree();
         }else if(path=="system"){
        	 
         }else if(path=="report"){
        	 location.reload();
        	 //reflsh();
         }else{
        	 location.reload();
         }
         getProcessBtnStatus();
        unmask();
     }
     
   //更改日期
		function changeDate() {
		//alert("更改时间");
		mask();
		var time = $("#time").val().replace(/[^0-9]/ig, ""); 
		var purl="${pageContext.request.contextPath}/declare/changeDate";
			$.ajax({
				url : purl,
				type : 'POST',
				dataType : 'json',
				data:{
					time:time
				},
				success : function(result) {
					getPath();
				}
			});
	     }
	  //修改密码
     function editPwd(id){
    	 var url = "${pageContext.request.contextPath}/manager/editPwd?id="+id;
    	 createWin(url,500,205,"修改密码");
     }
	  
	  
  	function NewMatch(){
  	// 撮合数据
  	 	
  	 	//判断申报单类型是否一致
  	 	this.matchData = function() {
  	 		var time = $("#time").val().replace(/[^0-9]/ig, "");
  	 		var time1 = $("#time").val();
  	 		var curl=baseUrl+'/match/jump';
  	 		 if(confirm("是否确定计算撮合数据?")){
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
  	 						if(result != null){
  	 							if(result['status']){
  	 								window.location.href=baseUrl+"/issue/init";
  	 							}else{
  	 								alert(result['msg']);
  	 							}
  	 						}
  	 						
  	 					}
  	 			
  	 			 });
  	 			
  	 		 }
  	 	};
  	}
  	var newMatch = new NewMatch();
  	//定时获取按钮状态
  	var colorArr = ['#626b62','#337ab7','green'];
  	function getProcessBtnStatus(){
  		$.ajax({
		    url : baseUrl+"/system/getProcessBtnStatus",
			type : 'POST',
			dataType : 'json',
			data:{
				time:$('#time').val()
			},
			success : function(datas) {
				var kyrlfbBtnNode,jgfbBtnNode;
				var result = datas['status'];
				var data = datas['data'];
				if(result != null){
					//console.info(result);
					for(var key in result){
						$('#process_btn').find('a[name='+key+']').css('background-color',colorArr[result[key]-1]);
						if(key=='jgfbBtn'){
							var jgfbBtnNode = result[key];
							if($("#jsppath").val()=='issue'){
								issuePageBtnStatus(jgfbBtnNode);
							}
							if(jgfbBtnNode==1){
								jgfbBtn = true;
							}else{
								jgfbBtn = false;
							}
						}else if(key=='kyrlfbBtn'){
							var kyrlfbBtnNode = result[key];
							if($("#jsppath").val()=='limitLine'){
								lineLimitPageBtnStatus(kyrlfbBtnNode);
							}
							if(kyrlfbBtnNode==1){
								kyrlfbBtn = true;
							}else{
								kyrlfbBtn = false;
							}
						}
					}
				}
			}
	 	});
  	}
  	getProcessBtnStatus();
window.setInterval(function(){ 
	getProcessBtnStatus();
  	}, 60000); 
    </script>
</body>

</html>
