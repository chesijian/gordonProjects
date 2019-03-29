<%@ page language="java" import="java.util.*,com.state.util.VersionCtrl" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.state.com/state" prefix="state"%>
<html>
<head>
	<meta charset="UTF-8">
	<title>test</title>
	<script src="${pageContext.request.contextPath }/js/state/dialog/sea.js?rnd=<%=Math.random()%>"></script>
	<!-- 引入 Bootstrap -->
     <link href="${pageContext.request.contextPath }/bootstrap/css/bootstrap.min.css?rnd=<%=Math.random()%>" rel="stylesheet">
     <script src="${pageContext.request.contextPath }/bootstrap/js/jquery-1.10.2.js?rnd=<%=Math.random()%>" type="text/javascript" ></script>
     <!-- 包括所有已编译的插件 -->
     <script src="${pageContext.request.contextPath }/bootstrap/js/bootstrap.min.js?rnd=<%=Math.random()%>"></script>
</head>
<script>   
$(function(){   
   var params=GetQueryString("paramid");
   var strs = params.split(",");
   var nm=1;
   for(var i=0;i<strs.length;i++){
	   if(strs[i]>0){
		   $("#tabs").append("<li><a href='#identifier' data-toggle='tab' data="+strs[i]+">申报单"+nm+"</a></li>");
		   nm=nm+1;
	   }
   }
   $("#tabs li").bind('click',function(e){
	   e.preventDefault();
	   $(this).tab('show');
	   $("#tableData").empty();
	   var id=$(this).find("a").attr("data");
	   var location=(window.location+'').split("/");
		var basePath=location[0]+'//'+location[2]+'/'+location[3];
		var purl=basePath+'/declare/getExportMsg';
	 	$.ajax({
			url : purl,
			type : 'POST',
			dataType : 'json',
			data : {
				id:id
			},
			success : function(result) {
		       if (result) {
		    	   var type="";
		    	  // alert(result.declareData.dtype);
		    	   if(result.declareData.dtype=='FULL_DAY'){
		    		   type="全天";
		    	   }else if(result.declareData.dtype=='HIGH'){
		    		   type="高峰";
		    	   }else if(result.declareData.dtype=='LOW'){
		    		   type="低谷";
		    	   }
		    	  
			   var cent='<tbody><tr><td colspan="4" align="center" style="font-weight:bold;">申报信息</td></tr>'+
			      '<tr><td>申报单位:</td><td id="content" >'+result.declare.area+'</td><td>申报用户:</td><td id="content">'+result.declare.area+'</td></tr>'+
			      '<tr><td>申报交易名称</td><td id="content">'+result.declare.sheetName+'</td><td>电价(元/Mwh):</td><td id="content">'+result.declareData.sumPrice+'</td></tr>'+
			      '<tr><td>申报电量(Mwh)：</td><td id="content">'+result.declareData.sumQ+'</td><td>平均电力(MW)：</td><td id="content">'+result.declareData.aveP+'</td></tr>'+
			       '<tr><td>类型：</td><td id="content">'+type+'</td><td>交易日期：</td><td id="content">'+result.declare.mdate+'</td></tr>'+
			       '<tr><td>备注：</td><td id="content" colspan="3">'+result.declare.descr+'</td></tr>'+
			      '<tr><td colspan="4" align="center" style="font-weight:bold;">出清信息</td></tr>';
			      if(result.billCountMeg){
			    	  if(result.billCountMeg.clear && result.billCountMeg.fdfy){
			    		  cent+='<tr><td>出清电量(Mwh)：</td><td id="content">'+result.billCountMeg.clear+'</td><td>出清费用(元)：</td><td id="content">'+result.billCountMeg.fdfy+'</td></tr>'+
					        '<tr><td colspan="4" align="center" style="font-weight:bold;">结算信息</td></tr>'+
					       '<tr><td>结算电量(Mwh)：</td> <td id="content">/</td><td>结算费用(元)：</td><td id="content">/</td></tr>';
			    	  }else{
			    		  cent+='<tr><td>出清电量(Mwh)：</td><td id="content">/</td><td>出清费用(元)：</td><td id="content">/</td></tr>'+
					        '<tr><td colspan="4" align="center" style="font-weight:bold;">结算信息</td></tr>'+
					       '<tr><td>结算电量(Mwh)：</td> <td id="content">/</td><td>结算费用(元)：</td><td id="content">/</td></tr>';
			    	  }
			      }
			       cent+='</tbody>';
			       $("#tableData").append(cent);
				} else {
					alert("新增失败!");
				}
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		}); 
   
   }) ;
   $("#tabs").children("li:first").trigger("click");
}); 


//获取参数
function GetQueryString(name) { 
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r!=null) return (r[2]); return null; 
	} 
	 
	var sname = GetQueryString("name"); 
	if(sname!=null) 
	{ 
	var sname_ = decodeURIComponent(sname); 
	//alert(sname_); 
	}
function getDeclareMsg(event){
	alert(event.text());
}
</script> 
<body>
<% String paramid=request.getParameter("paramid");%>

<ul id="tabs" class="nav nav-tabs">
  
</ul>
<table class="table table-bordered" id="tableData" style="margin-bottom:0px">
   
     
 
</table>
</body>
</html>