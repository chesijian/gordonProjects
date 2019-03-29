<%@ page language="java" import="java.util.*,com.state.util.VersionCtrl" pageEncoding="UTF-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.state.com/state" prefix="state" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html >
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理员界面</title>
<%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/ui/themes/default/easyui.css"> --%>
<Link Rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/common.css?v=<%=VersionCtrl.getVesrsion()%>">
<%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/ui/themes/icon.css"> --%>
<script src="${pageContext.request.contextPath }/ui/jquery-1.4.4.min.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/ui/jquery.easyui.min.js"></script>

<script type="text/javascript">  
	var dataTree = [ {
		"id" : "1",
		"text" : "设备维护"
	}, {
		"id" : "2",
		"text" : "监测类型维护"
	}, {
		"id" : "3",
		"text" : "监测内容维护"
	},{
		"id" : "4",
		"text" : "维护人信息"
	}
	 ];
	var userTree = [ 
	{
		"id" : "1",
		"text" : "用户维护"
	}
	
	];
	var warnTree = [ {
		"id" : "1",
		"text" : "报警日志"
	} ];
	var equTree = [ {
		"id" : "1",
		"text" : "设备列表"
	} ];
	
	

	
	$(function() {
		
		
		//$("#toolsTree1").tree("loadData", dataTree);
		
		$.post("declare/getTreeList",null,function(data){//分类树型数据加载
			$("#toolsTree1").tree("loadData",data);		
		});
			   
		
		$("#toolsTree1").tree({//分类树型模型加载 
			 onClick:function(node){
			 	typeId = node.id;
				typename=node.text;
				//alert(typeId+"/"+typename);
				if(typeId!=0){
					
				}
			},
			onContextMenu: function(e, node){
				e.preventDefault();
				$('#mm').menu('show', {
					left: e.pageX,
					top: e.pageY
				});
			}
		});
		


		


	   
	    
	});
	
</script>
</head>
<body class="easyui-layout" border="false">
<div region="north" id="north"
			style="height: 50px; background: #E0ECFF; overflow: hidden"
			border="false">
  <div style="float: left; padding-left: 10px; padding-top: 10px"> <img src="image/logo.png" /> </div>
  <div style="float: left; padding-top: 10px"> <span
					style="font-family: 黑体; font-size: 20px; font-weight: bold; font-style: normal; text-decoration: none; color: #15428B; padding-left: 100px;">设备运行综合监测管理系统</span> <span
					style="font-family: 黑体; font-size: 20px; font-weight: bold; font-style: normal; text-decoration: none; color: #15428B;">欢迎:</span> <span
					style="font-family: 黑体; font-size: 20px; font-weight: bold; font-style: normal; text-decoration: none; color: #15428B;"><%=session.getAttribute("username") %></span> <span
					style="font-family: 黑体; font-size: 20px; font-weight: bold; font-style: normal; text-decoration: none; color: #15428B;">当前时间:</span> <span
					style="font-family: 黑体; font-size: 20px; font-weight: bold; font-style: normal; text-decoration: none; color: #15428B;"
					id="Clock">时间：</span> </div>
  <div style="float: right; padding-right: 20px; padding-top: 10px">
  	<a id="homepage" href="admin.jsp" plain="true" class="easyui-linkbutton" iconCls="icon-homepage">首页</a>
    <a id="updatepass" href="#" plain="true" class="easyui-linkbutton"iconCls="icon-updatepass">修改密码</a>
    <a id="out" href="#" plain="true" class="easyui-linkbutton"iconCls="icon-out">退出</a> </div>
</div>
<div region="south"
			style="height: 50px; padding: 10px; background: #9DBCDB;"
			border="false">

</div>
<div region="west"
			style="width: 200px; padding1: 1px; overflow: hidden;" border="false">
  <!-- 左 -->
  <div class="easyui-accordion" fit="true">
    <div title="<center>设备列表</center>" selected="true" style="overflow: auto;">
      <ul id="toolsTree4">
      </ul>
    </div>
    <div title="<center>基础维护</center>" style="overflow: auto;" id="data">
      <ul id="toolsTree1">
      </ul>
    </div>
    <div title="<center>用户维护</center>" style="padding: 10px;" id="user">
      <ul id="toolsTree2">
      </ul>
    </div>
    <div title="<center>报警日志</center></div>" id="warn">
      <ul id="toolsTree3">
      </ul>
    </div>
  </div>
</div>
<div region="center" id="centerTitle" style="overflow: hidden;"
			title="<center>主界面</center>" border="false">
  <!--<div id="centerPanel" fit="true" border="false"></div>-->
  
</div>
<div id="codeWindow" class="easyui-window"
			title="<center>修改密码</center>"
			style="width: 400px; height: 200px; padding: 5px;" closed="true"; collapsible="false"; minimizable="false";collapsible="false">
<div class="easyui-layout" fit="true">
  <div region="center" border="false"
					style="padding: 10px; background: #fff; border: 1px solid #ccc;">
    <form id="form1">
      <input type="hidden" id="userName" value="<%=session.getAttribute("username")%>"> 
      <table align="center">
        <tr>
          <td><label> 旧密码 </label></td>
          <td><input class="easyui-validatebox" type="password" id="oldPass"
										name="oldcode" style="width: 125px" missingMessage="旧密码不能为空"
										required="true" /></td>
        </tr>
        <tr>
          <td><label> 新密码 </label></td>
          <td><input class="easyui-validatebox" type="password" id="newPass"
										name="newcode" style="width: 125px" missingMessage="新密码不能为空"
										required="true" /></td>
        </tr>
        <tr>
          <td><label> 确认密码 </label></td>
          <td><input class="easyui-validatebox" type="password" id="surePass"
										name="confimcode" style="width: 125px"
										missingMessage="确认密码不能为空" required="true" /></td>
        </tr>
        <tr>
          <td colspan="5"><a id="sure" href="#" class="easyui-linkbutton">确定</a> <a id="reset" href="#" class="easyui-linkbutton">重置</a></td>
        </tr>
      </table>
    </form>
  </div>
</div>
</div>
</body>
</html>