<%@ page language="java" import="java.util.*,com.state.util.VersionCtrl"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 

<!DOCTYPE html>
<html>
<head>
<title>用户信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style type="text/css">
.icon-copy {background-image: url(../img/icon/copy.gif) !important;}

</style>
<link
	href="${pageContext.request.contextPath }/css/common.css"
	rel="stylesheet" />
	<link
	href="${pageContext.request.contextPath }/js/easyui/themes/bootstrap/easyui.css"
	rel="stylesheet" />

<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/common/common-public-1.0.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
<%-- <script src="${pageContext.request.contextPath }/js/state/issue.js?v=<%=VersionCtrl.getVesrsion()%>"></script> --%>
</head>
<body>

	<!--controls-->
    <input type="hidden" value="${userId}" id="userId">
    <input type="hidden" value="${time}" id="time">
     <input type="hidden" value="${index}" id="index"/>
	<div class="box-body" style="margin-top: 10px; border: none;">
		
		<div style="margin-top: 0px;">
			 <div style="margin-top: 0px;text-align: right;" id="imageDiv">
		       <a href="#">
		        <img alt="签名" src="${pageContext.request.contextPath }/img/sign.png" onclick="getImage();">
		       </a>
			</div>

		</div>
		<div class="form-actions" style="text-align: center;margin-top: 50px;">
			<a id="btn1" href="#" class="easyui-linkbutton" onclick="updateResult();"; >&nbsp;&nbsp;保存&nbsp;&nbsp;</a>
		</div>


		<!--/grid sizing-->
	</div>
	<div style="display: none; width: 300px; height: 150px" id="dialog"
		title="提示">
		<div style="margin-top: 40px; margin-left: 57px;">
			<font size="2"> 点击<img src='${pageContext.request.contextPath }/img/signIcon.png'>签名后再进行发布操作！
			</font>
		</div>
	</div>



	<script type="text/javascript">
	
	var mName =  $("#userId").val();
	var isExist = true;
	var options = {
			url : baseUrl+"/manager/getUserAutoGraphIsExist",
			type : "post",
			data :{id:mName},
			dataType : "json",
			success : function(response) {
				var rs = response;
				if (true == rs.status) {
					return;
					parent.closeWin();
				} else {
					if(rs.msg!=null){
						alert(rs.msg);
						isExist = false;
					}
					
				}
				
			},
			error : function() {
				alert("系统错误");
			}
		};
	var index = $("#index").val();
	function getImage(){
		$.ajax(options);
		if(isExist){
			index++;
			if(parseInt(index)>1){
				alert("不能连续签名！");
				return;
			}
			
			var mName =  $("#userId").val();
			var pathContent = "<img alt='' src='${pageContext.request.contextPath }/manager/getImage?id="+mName+"&sign=true' >";
			$('#imageDiv').html(pathContent+$('#imageDiv').html());
		}
		
		
	} 
	
 function updateResult() {
	  //  alert('++++');
	    var time = $("#time").val();
		var checkFlag=true;
		$('#IssueDataDiv input[class=datainput]').each(function(){
			if(this.value==''){
				checkFlag=false;
				alert("请输入正确的数据!");
			  }
			});
		if(checkFlag){
			$.ajax({
				url : baseUrl+'/issue/updateResult',
				type : 'POST',
				dataType : 'json',
				data : {
					resultStr : parent.issue.makeResultData(),
					time:time
				},
				success : function(result) {
					if (result) {
						//myLimitLine.initChangeData();
						//myLimitLine.refreshLimitLineData();
						if(result['msg']=='先签名后保存！'){
							$("#dialog").dialog();
						}else{
							alert(result['msg']);
							if(result['status']){
								parent.closeWin();
							}
						}
						
					} else {
						alert("保存失败!");
					}
				},
				error : function(xhr, status) {
					alert("系统错误!");
				}
			});
		}
	};
	
	// 出清审核
	function issueData() {
		
		var time = $("#time").val();
		
		if (confirm("是否确定发布出清信息?")) {
			parent.mask("正在发布...");
			$.ajax({
				url : baseUrl+'/issue/updateIssue',
				type : 'POST',
				dataType : 'json',
				data : {
					time : time
				},
				timeout : 15000,
				success : function(result) {
					parent.unmask();
					if(result){
						if(result['msg']=='先签名后保存！'){
							$("#dialog").dialog();
						}else{
							alert(result['msg']);
							if(result['status']){
								parent.closeWin();
							}
						}
						
						
					}else{
						alert("发布失败!");
					}
				},
				error : function(xhr, status) {
					alert("系统错误!");
					parent.unmask();
				}

			});
		}

	};
	
	</script>
</body>
</html>