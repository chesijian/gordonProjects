<%@ page language="java"
	import="java.util.*,com.state.util.AuthoritiesUtil,com.state.util.VersionCtrl"
	pageEncoding="UTF-8" contentType="text/html; charset=utf-8"%>

<title></title>
<head>
<!-- 引入 Bootstrap -->
<script src="${pageContext.request.contextPath }/js/jquery/jquery-1.10.2.js?v=<%=VersionCtrl.getVesrsion()%>"
	type="text/javascript"></script>
<script src="${pageContext.request.contextPath }/js/state/jquery.ztree.core.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
    <Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/zTreeStyle.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
    <Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/issue.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css"> 
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/declare.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/common/common-public-1.0.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
<link
	href="${pageContext.request.contextPath }/bootstrap/css/bootstrap.min.css?v=<%=VersionCtrl.getVesrsion()%>"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath }/bootstrap/plugin/js/table/bootstrap-table.min.css?v=<%=VersionCtrl.getVesrsion()%>"
	rel="stylesheet" />
	<style>
		.lmenu {
	padding-bottom:50px;
	overflow:auto;
	float: left;
	width: 220px;
	border: solid 1px #dcdcdb;
	font-size: 14px;
	border-top: 0px;
	height:482px;
}

	</style>
	<script src="${pageContext.request.contextPath }/js/charts/highcharts.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/state/jqueryFormat.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<!-- 
	<script src="${pageContext.request.contextPath }/js/charts/modules/exporting.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	 -->
	<script src="${pageContext.request.contextPath }/js/state/issue.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
<script
	src="${pageContext.request.contextPath }/js/state/issue/result.js?v=<%=VersionCtrl.getVesrsion()%>"></script>

<script type="text/javascript">
var issue = new Issue();

function zTreeOnClick(event, treeId, treeNode) {
	var treeName=treeNode.name;
	/******2016.08.17修改新界面******start******/
	//alert(treeNode.id);
	//如果叶子则展示数据
	if(treeNode.id.charAt(0) != '-'){
		//alert(treeNode.id);
		//if(issue.issueType == "lineLimit"){
			//issue.getLineLimitData(treeNode.id);
		//}else{
			//console.info(treeNode);
			issue.getResultData(treeNode.id,treeNode);
		//}
		
	}else{
		//如果点击的是文件夹，则展开所有子节点
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		treeObj.expandNode(treeNode, true, true, true);
	}
	
	 /******2016.08.17修改新界面******end******/
	
};
$(function(){
	issue.loadTree(1,"result");
	/* $("#userInfoForm").ajaxForm({
		beforeSubmit:function(){
			$("#submitBtn").button('loading');
		},
		success:function(data){
			if(data.success){
				$.messager.confirm("提示","修改成功",function(){window.location.reload();});
			}else{
				$.messager.confirm("提示",data.msg);
				$("#submitBtn").button("reset");
			}
		}
	}); */
});
</script>
</head>
<body>
<div class="mid" id="result">
			<div class="contop" style="padding:0px 10px;">
				<div id="lbtn" class="rl"></div>
				<div class="cl"></div>
			</div>
	<div>
			    
	    <div class="lmenu zTreeDemoBackground left">
			<ul id="treeDemo"  class="ztree">
				
			</ul>
		</div>
		<form id="resultForm" class="form-horizontal" action="/saveResult" method="post" style="width: 700px;">
		<div id="resultDataDiv" class="fl bd1" style="padding-top:10px;;border:solid 0px;width:968px;">
			<div class="form-group">
				<button id="submitBtn" type="submit" class="btn btn-primary col-sm-offset-5" data-loading-text="数据保存中" autocomplate="off">
					保存数据
				</button>
			</div>
			<div class="fl pdl10">
				<table width="968" height="302" cellpadding="0" cellspacing="0">
					<thead>
						<th width="10%" class="cgr">时刻</th>
						<th width="7%" class="cgr">0时</th>
						<th width="7%" class="cgr">1时</th>
						<th width="7%" class="cgr">2时</th>
						<th width="7%" class="cgr">3时</th>
						<th width="7%" class="cgr">4时</th>
						<th width="7%" class="cgr">5时</th>
						<th width="7%" class="cgr">6时</th>
						<th width="7%" class="cgr">7时</th>
						<th width="7%" class="cgr">8时</th>
						<th width="7%" class="cgr">9时</th>
						<th width="7%" class="cgr">10时</th>
						<th width="7%" class="cgr">11时</th>
					</thead>
					<tr>
						<td width="10%" class="cgr">15分</td>
						<td width="7.5%"><input  name="h01" class="datainput"></td>
						<td width="7.5%"><input  name="h05" class="datainput"></td>
						<td width="7.5%"><input  name="h09" class="datainput"></td>
						<td width="7.5%"><input  name="h13" class="datainput"></td>
						<td width="7.5%"><input  name="h17" class="datainput"></td>
						<td width="7.5%"><input  name="h21" class="datainput"></td>
						<td width="7.5%"><input  name="h25" class="datainput"></td>
						<td width="7.5%"><input  name="h29" class="datainput"></td>
						<td width="7.5%"><input  name="h33" class="datainput"></td>
						<td width="7.5%"><input  name="h37" class="datainput"></td>
						<td width="7.5%"><input  name="h41" class="datainput"></td>
						<td width="7.5%"><input  name="h45" class="datainput"></td>
					</tr>
					<tr class="bgh">
						<td width="10%" class="cgr">30分</td>
						<td width="7.5%"><input  name="h02" class="datainput"></td>
						<td width="7.5%"><input  name="h06" class="datainput"></td>
						<td width="7.5%"><input  name="h10" class="datainput"></td>
						<td width="7.5%"><input  name="h14" class="datainput"></td>
						<td width="7.5%"><input  name="h18" class="datainput"></td>
						<td width="7.5%"><input  name="h22" class="datainput"></td>
						<td width="7.5%"><input  name="h26" class="datainput"></td>
						<td width="7.5%"><input  name="h30" class="datainput"></td>
						<td width="7.5%"><input  name="h34" class="datainput"></td>
						<td width="7.5%"><input  name="h38" class="datainput"></td>
						<td width="7.5%"><input  name="h42" class="datainput"></td>
						<td width="7.5%"><input  name="h46" class="datainput"></td>
					</tr>
					<tr>
						<td width="10%" class="cgr">45分</td>
						<td width="7.5%"><input  name="h03" class="datainput"></td>
						<td width="7.5%"><input  name="h07" class="datainput"></td>
						<td width="7.5%"><input  name="h11" class="datainput"></td>
						<td width="7.5%"><input  name="h15" class="datainput"></td>
						<td width="7.5%"><input  name="h19" class="datainput"></td>
						<td width="7.5%"><input  name="h23" class="datainput"></td>
						<td width="7.5%"><input  name="h27" class="datainput"></td>
						<td width="7.5%"><input  name="h31" class="datainput"></td>
						<td width="7.5%"><input  name="h35" class="datainput"></td>
						<td width="7.5%"><input  name="h39" class="datainput"></td>
						<td width="7.5%"><input  name="h43" class="datainput"></td>
						<td width="7.5%"><input  name="h47" class="datainput"></td>
					</tr>
					<tr class="bgh">
						<td width="10%" class="cgr">00分</td>
						<td width="7.5%"><input  name="h04" class="datainput"></td>
						<td width="7.5%"><input  name="h08" class="datainput"></td>
						<td width="7.5%"><input  name="h12" class="datainput"></td>
						<td width="7.5%"><input  name="h16" class="datainput"></td>
						<td width="7.5%"><input  name="h20" class="datainput"></td>
						<td width="7.5%"><input  name="h24" class="datainput"></td>
						<td width="7.5%"><input  name="h28" class="datainput"></td>
						<td width="7.5%"><input  name="h32" class="datainput"></td>
						<td width="7.5%"><input  name="h36" class="datainput"></td>
						<td width="7.5%"><input  name="h40" class="datainput"></td>
						<td width="7.5%"><input  name="h44" class="datainput"></td>
						<td width="7.5%"><input  name="h48" class="datainput"></td>
					</tr>
					<tr>
						<td width="10%" class="cgr">时刻</td>
						<td width="7%" class="cgr">12时</td>
						<td width="7%" class="cgr">13时</td>
						<td width="7%" class="cgr">14时</td>
						<td width="7%" class="cgr">15时</td>
						<td width="7%" class="cgr">16时</td>
						<td width="7%" class="cgr">17时</td>
						<td width="7%" class="cgr">18时</td>
						<td width="7%" class="cgr">19时</td>
						<td width="7%" class="cgr">20时</td>
						<td width="7%" class="cgr">21时</td>
						<td width="7%" class="cgr">22时</td>
						<td width="7%" class="cgr">23时</td>
					</tr>
					<tr class="bgh">
						<td width="10%" class="cgr">15分</td>
						<td width="7.5%"><input  name="h49" class="datainput"></td>
						<td width="7.5%"><input  name="h53" class="datainput"></td>
						<td width="7.5%"><input  name="h57" class="datainput"></td>
						<td width="7.5%"><input  name="h61" class="datainput"></td>
						<td width="7.5%"><input  name="h65" class="datainput"></td>
						<td width="7.5%"><input  name="h69" class="datainput"></td>
						<td width="7.5%"><input  name="h73" class="datainput"></td>
						<td width="7.5%"><input  name="h77" class="datainput"></td>
						<td width="7.5%"><input  name="h81" class="datainput"></td>
						<td width="7.5%"><input  name="h85" class="datainput"></td>
						<td width="7.5%"><input  name="h89" class="datainput"></td>
						<td width="7.5%"><input  name="h93" class="datainput"></td>
					</tr>
					<tr>
						<td width="10%" class="cgr">30分</td>
						<td width="7.5%"><input  name="h50" class="datainput"></td>
						<td width="7.5%"><input  name="h54" class="datainput"></td>
						<td width="7.5%"><input  name="h58" class="datainput"></td>
						<td width="7.5%"><input  name="h62" class="datainput"></td>
						<td width="7.5%"><input  name="h66" class="datainput"></td>
						<td width="7.5%"><input  name="h70" class="datainput"></td>
						<td width="7.5%"><input  name="h74" class="datainput"></td>
						<td width="7.5%"><input  name="h78" class="datainput"></td>
						<td width="7.5%"><input  name="h82" class="datainput"></td>
						<td width="7.5%"><input  name="h86" class="datainput"></td>
						<td width="7.5%"><input  name="h90" class="datainput"></td>
						<td width="7.5%"><input  name="h94" class="datainput"></td>
					</tr>
					<tr class="bgh">
						<td width="10%" class="cgr">45分</td>
						<td width="7.5%"><input  name="h51" class="datainput"></td>
						<td width="7.5%"><input  name="h55" class="datainput"></td>
						<td width="7.5%"><input  name="h59" class="datainput"></td>
						<td width="7.5%"><input  name="h63" class="datainput"></td>
						<td width="7.5%"><input  name="h67" class="datainput"></td>
						<td width="7.5%"><input  name="h71" class="datainput"></td>
						<td width="7.5%"><input  name="h75" class="datainput"></td>
						<td width="7.5%"><input  name="h79" class="datainput"></td>
						<td width="7.5%"><input  name="h83" class="datainput"></td>
						<td width="7.5%"><input  name="h87" class="datainput"></td>
						<td width="7.5%"><input  name="h91" class="datainput"></td>
						<td width="7.5%"><input  name="h95" class="datainput"></td>
					</tr>
					<tr>
						<td width="10%" class="cgr">00分</td>
						<td width="7.5%"><input  name="h52" class="datainput"></td>
						<td width="7.5%"><input  name="h56" class="datainput"></td>
						<td width="7.5%"><input  name="h60" class="datainput"></td>
						<td width="7.5%"><input  name="h64" class="datainput"></td>
						<td width="7.5%"><input  name="h68" class="datainput"></td>
						<td width="7.5%"><input  name="h72" class="datainput"></td>
						<td width="7.5%"><input  name="h76" class="datainput"></td>
						<td width="7.5%"><input  name="h80" class="datainput"></td>
						<td width="7.5%"><input  name="h84" class="datainput"></td>
						<td width="7.5%"><input  name="h88" class="datainput"></td>
						<td width="7.5%"><input  name="h92" class="datainput"></td>
						<td width="7.5%"><input  name="h96" class="datainput"></td>
					</tr>
				</table>
			</div>
			<div class="cl"></div>
		</div>
		</form>
	</div>
</div>
</body>

</html>

