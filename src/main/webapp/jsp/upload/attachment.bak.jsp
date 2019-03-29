<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>附件上载</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	
	-->
<link rel="stylesheet" type="text/css" href="css/attachment.css">
<script type="text/javascript" src="jslib/upload/jquery.js"></script>
	<script type="text/javascript" src="jslib/upload/ajaxfileupload.js"></script>
<script type="text/javascript">
	var fileNum = 0;
	function GetUrlParam(name) {
		var reg = new RegExp("(^|\\?|&)" + name + "=([^&]*)(\\s|&|$)", "i");
		if (reg.test(location.href))
			return unescape(RegExp.$2.replace(/\+/g, " "));
		return "";
	};
	function selFile(){
		//alert(fileNum);
		document.getElementById("file_1").click();
		
	}
	function upfileCallBack(obj) {
		upLoad(obj);
	}
	function upLoad(obj){
		$.ajaxFileUpload
		(
			{
				url:'AttachmentServlet',
				secureuri:false,
				fileElementId:'file_1',
				dataType: 'json',
				success: function (data, status)
				{
					if(data.status == -4){
						//alert(data.fileId);
						var xtype = GetUrlParam("xtype");
						var div_list = parent.document.getElementById("_attachment_filelist_"+xtype);
						var num = div_list.getElementsByTagName("span").length;
						var newFile = "<span id='_span_"+xtype+"_"+data.id+"' fileId='"+data.id+"' size='"+data.size+"' fileName='"+data.fileName+"'  type='"+data.type+"'>&nbsp;&nbsp;"+(num+1)+"."+obj.value+"("+data.size+") 待上传...<img src='../images/icons/fam/tab-close-on.gif' id='"+data.id+"' onclick='_deleteFile(this,\""+xtype+"\");'/></span>";
						div_list.innerHTML  += "<br>"+newFile;
					}
				},
				error: function (data, status, e)
				{
					alert(e);
				}
			}
		);
	}
</script>
</head>

<body style="background-color: #f7f7f7" topmargin="0">
	<a onclick="selFile();"><img src="images/icons/fam/attachment.gif">选择文件</a>
		<div id="attachment_upload_list" style="display: none">
			<input type="file" name="attachment" id="file_1" onchange="upfileCallBack(this)" />
		</div>
	
</body>
</html>
