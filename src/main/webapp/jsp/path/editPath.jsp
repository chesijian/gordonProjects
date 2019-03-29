<%@ page language="java" import="java.util.*,com.state.util.VersionCtrl"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 

<!DOCTYPE html>
<html>
<head>
<title>交易通道信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style type="text/css">

</style>
<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/declare.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/line.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
<script src="${pageContext.request.contextPath }/js/state/path.js?v=<%=VersionCtrl.getVesrsion()%>"></script>

<script type="text/javascript" src="${pageContext.request.contextPath }/js/common/common-public-1.0.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
<script src="${pageContext.request.contextPath }/bootstrap/plugin/js/jquery.js"></script>
<script src="${pageContext.request.contextPath }/bootstrap/plugin/js/jquery-ui.min.js"></script>
<script src="${pageContext.request.contextPath }/bootstrap/plugin/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath }/bootstrap/plugin/js/select2/select2.js"></script>
<script src="${pageContext.request.contextPath }/bootstrap/plugin/js/validate/jquery.validate.js"></script>
<script src="${pageContext.request.contextPath }/bootstrap/plugin/js/validate/jquery.metadata.js"></script>
<script src="${pageContext.request.contextPath }/js/org/user.js"></script>

 
</head>
<body>
	<div id="tk">
			<div class="bcon">
				<div class="rl"><a class="btn3" href="javascript:void(0);" onclick="path.displayAddPanel('tk')">x</a></div>
				<div class="cl"></div>
				<div class="pdt20" >
					<div>通道名称&nbsp;&nbsp;<input id="addPathName" name="mpath"/></div>
					<div class="pdt20 ">
						<span>首端&nbsp;&nbsp;</span><select id="startLine" name="startArea"> </select>
						<span>末端&nbsp;&nbsp;</span><select id="endLine" name="endArea"> </select>
					</div>
					<div id="lineSelectDiv">
						<div class="fl pdt20" id="firstSelectPanel">
							成员1&nbsp;&nbsp;<select id="lineSelect1" name="">
							     
						         </select>
							
							<span>成员方向&nbsp;&nbsp;</span><select id="lineSelectOR1" name="mdirection">
							     <option value ="1">正</option>
	                             <option value ="2">反</option>
						         </select>
							<a class="btn3 addPath" href="javascript:void(0);" onclick="path.addLineSelect()">+</a>
						</div>
						<div class="cl"></div>
					</div>
					
					
					<div class="cl"></div>
					<div class="pdt20 rl pdr20"><a class="btn1"  href="javascript:void(0);" onclick="path.savePath('addPathName', 'startLine', 'endLine','lineSelectDiv')">提交</a></div>
				</div>
			</div>
			<div class="blbg">&nbsp;</div>
		</div>			
<script type="text/javascript">
	document.getElementById("tk").style.display = 'block';
	//var status = "${status}";
	//var userInfo = ${userInfo};
</script>  
</body>
</html>