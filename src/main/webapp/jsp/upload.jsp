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

<script type="text/javascript" src="${pageContext.request.contextPath }/js/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript">
	$(function(){
		//处理上传
		$("#uploadBtn").uploadify({
			height:10,//按钮大小
			width:50,//按钮宽度
			buttonText:"导入",//按钮文字
			fileObjName:"file",//上传文件的名字(对应的MultipartFile的名字)
			multi:false,//是否允许多选
			debug:true,
			fileTypeExts:'*.dat; *.jpg; *.png',//控制上传文件的后缀
			swf:"${pageContext.request.contextPath}/js/uploadify/uploadify.swf",//falsh文件的地址
			uploader:"http://localhost:8080/market/declare/fileUpload",//后台处理上传文件的地址;
			overrideEvents:['onUploadSuccess','onUploadProgress','onSelect','onUploadError'],
			onUploadSuccess:function(file,data){
				alert("88888");
				$("#uploadImg1").attr("src",data);
				$("#uploadImage1").val(data);
			},
			onUploadError:function(file, errorCode, errorMsg, errorString){
				console.debug(errorCode);
				console.debug(errorMsg);
				console.debug(errorString);
			}
		});
	});
	</script>
</head>
<body>

	<div class="box-body" style="margin-top: 10px;border: none;" >
		<form class="form-horizontal" style="border: none;" id="form-validate">
			<fieldset style="border: none;">
				<table style="width:100%" border="0">
				<tr>
				<td width="50%">
				<div>
			        <label for="area">导入:</label>
			        <a  href="javascript:;" id="uploadBtn1">上传文件</a>
			        
			    </div>    
				</td>
				<td width="50%">
					<input type="file" id="uploadBtn"/>
				</td>
				</tr>
				</table>
			</fieldset>
		</form>
	</div>

</body>
</html>