<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*,com.state.util.AuthoritiesUtil,com.state.util.VersionCtrl"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>地图展示通道信息</title>
<script src="${pageContext.request.contextPath }/js/echart-2.2.7/dist/echarts.js"></script>
<script type="text/javascript">
	var data = ${result};
	var areaArry =${areas};
	var isLoad = false;
	function getIsLoad(){
		return isLoad;
	}
</script>

</head>
<body>
<div id="main" style="width:100%;height:480px;"></div>
<script src="${pageContext.request.contextPath }/js/state/issue/map.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
<div style="position: absolute;display:none; border-style: solid; white-space: nowrap; 

transition: left 0.4s ease 0s, top 0.4s ease 0s; background-color: rgba(0, 0, 0, 0.7); border-

width: 0px; border-color: rgb(51, 51, 51); border-radius: 4px; color: rgb(255, 255, 255); padding: 

5px; left: 818px; top: 345px;" class="echarts-tooltip zr-element">--河南</div>
</body>
</html>