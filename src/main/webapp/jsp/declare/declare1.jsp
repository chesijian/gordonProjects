<%@ page language="java" import="java.util.*,com.state.util.VersionCtrl"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 

<!DOCTYPE html>
<html>
<head>
<title>申报单信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style type="text/css">
.icon-copy {background-image: url(../img/icon/copy.gif) !important;}
.look{color: #000000;
 	font-size:20px;
}
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

	<script src="${pageContext.request.contextPath }/js/state/declare/declare.js"></script> 
</head>
<body>

	<!--controls-->
	
				<div class="box-body" style="margin-top: 10px;border: none;" >
					<!--grid sizing-->
					<form class="form-horizontal" style="border: none;" id="form-validate">
					<input type="hidden" value="${id}" id="userId"/>
					<input type="hidden" value="${index}" id="index"/>
						<fieldset style="border: none;">
							<table style="width:100%" border="0">
							<tr>
							<td width="50%">
							<div>
        <label for="area">申报单位:</label>
        <input class="look easyui-textbox" type="text" id="area" name="area" data-options="required:true,disabled:true" />
    </div>
    
							</td>
							<td width="50%">
							<div>
        <label for="user">申报用户:</label>
        <input class="easyui-textbox"  type="text" id="user" name="user" data-options="disabled:true" />
    </div>
							</td>
							</tr>
							<tr>
							<td width="50%">
							<c:if test="${status=='new'}">
							<div>
        <label for="startDate">开始日期:</label>
        <input class="easyui-datebox" type="text" id="startDate" name="startDate" data-options="disabled:true"  required="required" />
    </div>
    </c:if>
    <c:if test="${status=='edit'}">
							<div>
        <label for="startDate">交易单号:</label>
        <input class="easyui-textbox" type="text" id="sheetName" name="sheetName" data-options="disabled:true"  required="required" />
    </div>
    </c:if>
							</td>
							<td width="50%">
							<!-- 
							<c:if test="${status=='new'}"></c:if>
							<div>
        						<label for="endDate">结束日期:</label>
       			 				<input class="easyui-datebox" type="text" id="endDate" name="endDate" required="required" />
  
    						</div>
    						
     						-->
    <c:if test="${status=='edit'}">
   
							<div>
        <label for="startDate">交易日期:</label>
        <input class="easyui-datebox" type="text" id="time" name="time" data-options="disabled:true"  required="required" />
    </div>
    </c:if>
							</td>
							</tr>
							<tr>
							<td width="50%"><table id="timeTable" ></table></td><td width="50%"><table id="powerTable" ></table></td>
							</tr>
							</table>
							<c:if test="${readOnly == true}">
							<div style="margin-top: 0px;">
							 
							  <div style="margin-top: 0px;text-align: right;" id="imageDiv">
							       <img id="img_old" alt="" src="${pageContext.request.contextPath }/manager/getImage?id=${userIdList}&sign=false" >
							  </div>
							</div>
							</c:if>
							<c:if test="${readOnly == false}">
							<div style="margin-top: 0px;">
							 
							  <div style="margin-top: 0px;text-align: right;" id="imageDiv">
							       <img id="img_old" alt="" src="${pageContext.request.contextPath }/manager/getImage?id=${userIdList}&sign=false" >
							       <a href="#">
							        <img alt="签名" src="${pageContext.request.contextPath }/img/sign.png" onclick="getImage();">
							       </a>
							  </div>
							</div>
							<div class="form-actions" style="text-align:center">
							<a id="btn1" href="#" class="easyui-linkbutton" onclick="saveData();"; >保 存</a>
								<c:if test="${isShowDel == true}">
								<a id="btn2" href="#" class="easyui-linkbutton" onclick="parent.declare.deleteDeclare();parent._win.close();">删除</a>
								</c:if>
							</div>
							</c:if>
						</fieldset>
					</form>
					<!--/grid sizing-->
				</div>
				<div style="display: none; width: 300px;height: 150px" id="dialog" title="提示" >
				   <div style="margin-top: 40px;margin-left: 57px;">
				           <font size="2"> 点击<img src='${pageContext.request.contextPath }/img/signIcon.png'>签名后再进行保存操作！</font>
				   </div>
				</div>
				
				<div class="bcon" style="display: none; width: 280px;height: 150px" id="copyDialog" title="请选择日期" >
				   <div style="margin-top: 20px;margin-left: 65px;">
				           <div><input style="width: 120px" class="easyui-datebox" type="text" id="copyTime" required="required" /></div>
				           <div style="margin-top: 20px;margin-left: 15px;">
				           <a href="#" class="easyui-linkbutton" onclick="copyData()">确定</a>
				           <a style="margin-left: 20px" href="#" class="easyui-linkbutton" onclick="closeDialog()">取消</a>
				           </div>
				   </div>
				</div>
	<!--/controls-->


	<!-- javascript
        ================================================== -->
	

	<script type="text/javascript">
	var readOnly ="${readOnly}";// $("#readOnly").val();
	var status = "${status}";
	var dataInfo = ${dataInfo};
	var priceStatus = ${priceStatus};
	var timeData = dataInfo['data'];
	var mName =  $("#userId").val();
	var isExist = true;
	var options = {
			url : baseUrl+"/manager/getUserAutoGraphIsExist",
			type : "post",
			async:false,
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
			var pathContent = "<img alt='' src='${pageContext.request.contextPath }/manager/getImage?id="+mName+"&sign=true'>";
			$('#img_old').remove();
			$('#imageDiv').html(pathContent+$('#imageDiv').html());
		}
		
	} 
	//alert("readOnly=="+readOnly);
	</script>
</body>
</html>