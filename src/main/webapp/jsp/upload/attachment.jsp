<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	//System.out.println(request.getHeader("USER-AGENT").toLowerCase());
	String[] ie8m = new String[]{"msie 8.0;","msie 7.0;","msie 6.0;"};
	String[] ie9p = new String[]{"msie 10.0;","msie 9.0;"};
	//String ie = "msie 6.0;msie 7.0;msie 8.0;";
	boolean isIE8m = false;
	boolean isIE9p = false;
	for(int i = 0;i<ie8m.length;i++){
		if(request.getHeader("USER-AGENT").toLowerCase().indexOf(ie8m[i]) > 0){
			isIE8m = true;
		}
	}
	for(int i = 0;i<ie9p.length;i++){
		if(request.getHeader("USER-AGENT").toLowerCase().indexOf(ie9p[i]) > 0){
			isIE9p = true;
		}
	}
	
	//System.out.println(request.getHeader("USER-AGENT")+"======="+isIE);
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
<style type="text/css">
a.addfile {
	background-image: url(${pageContext.request.contextPath }/jsp/upload/upload.gif);
	background-repeat: no-repeat;
	display: block;
	float: left;
	height: 20px;
	margin-top: 1px;
	position: relative;
	text-decoration: none;
	top: 0pt;
	width: 80px;
}

input.addfile {
	cursor: pointer !important;
	height: 18px;
	left: -13px;
	filter: alpha(opacity = 0);
	position: absolute;
	top: 5px;
	width: 1px;
	z-index: -1;
}
</style>
<!--  
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/jsp/upload/attachment.css">
-->
<!-- 
<script type="text/javascript" src="jslib/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="admin/upload/jquery.js"></script>
 -->
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/upload/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/upload/ajaxfileupload.js"></script>
<script type="text/javascript">
	var fileNum = 0;
	function GetUrlParam(name) {
		var reg = new RegExp("(^|\\?|&)" + name + "=([^&]*)(\\s|&|$)", "i");
		if (reg.test(location.href))
			return unescape(RegExp.$2.replace(/\+/g, " "));
		return "";
	};
	
	function formOnLoad(){
			//var formId = parent.document.getElementById("_normal_form_formId").value;
			//document.getElementById("extraParams").value = formId; 
		
	}
	function selFile() {
		//alert(fileNum);
		//document.getElementById("file_1").click();
		var ie = navigator.appName == "Microsoft Internet Explorer" ? true : false;
		if (ie) {
			//document.getElementById("file_1").click(); 
			var a = document.createEvent("MouseEvents");//FF的处理 
			a.initEvent("click", true, true);
			document.getElementById("file_1").dispatchEvent(a);
			//document.getElementById("filename").value=document.getElementById("file").value;
		} else {
			var a = document.createEvent("MouseEvents");//FF的处理 
			a.initEvent("click", true, true);
			document.getElementById("file_1").dispatchEvent(a);
		}

	}
	function upfileCallBack(obj) {
		upLoad(obj);
	}
	/**/
	function updateFileStatus(xtype,flag){
		var _attachment_loading = parent.document.getElementById("_attachment_loading_"+xtype);
		var file = {};
		
		
		if(_attachment_loading != undefined){
			if(flag == true){
				file.fileFlag = true;
				_attachment_loading.style.display="none";
			}else{
				_attachment_loading.style.display="";
				file.fileFlag = false;
			}
		}	
		//alert(_attachment_loading.style.display);
	}
	function upLoad(obj) {
		var xtype = "${xtype}";
		updateFileStatus(xtype,false);
		//return;
		/*
		$("#loading").ajaxStart(function() {
			$(this).show();
		}).ajaxComplete(function() {
			$(this).hide();
		});
		*/
		//alert(parent.document.getElementById("time").value);
		//return;
		var mdate = parent.document.getElementById("time").value;
		//alert(obj.value);
		$.ajaxFileUpload({
			url : 'attachment/upload?mdate='+mdate,
			secureuri : false,
			fileElementId : 'file_1',
			dataType : 'json',
			data : {
				
				//formId : document.getElementById("extraParams").value
			},
			success : function(data, status) {
				
				updateFileStatus(xtype,true);
				//alert("进来了");
				//console.info(data)
				//alert(status +"=="+ (typeof data.status)+"=="+data.status+"=="+(data.status == true));
				if (data.status == true) {
					//alert(data.msg);
					var div_list = parent.document.getElementById("_attachment_filelist_" + xtype);
					var num = div_list.getElementsByTagName("span").length;
					
					var newFile ;
						newFile = "<span id='_span_"+xtype+"_"+data.id+"' fileId='"+data.id+"' size='"+data.size+"' fileName='"+data.fileName+"'  type='"+data.type+"'>&nbsp;&nbsp;" + (num + 1) + "." + data.fileName + "(" + data.size + ") </span>";
					
					div_list.innerHTML += "<br>" + newFile;
					
					//alert(div_list.innerHTML);
					//parent.uploadFileCallBack(data, xtype);
					parent.alert("上传成功!");
					if(data['lastId'] != null){
						parent.loadTreeData(data['lastId']);
					}else{
						parent.loadTreeData()
					}
					parent.closeWin();
					
				}else {
					
					parent.alert(data.msg);
				}
			},
			error : function(data, status, e) {
				//alert("cesada");
				//alert(status+"==error=="+e);
				updateFileStatus(xtype,true);
				parent.alert("导入失败！error");
			}
		});
	}
	/*
	$(function(){  
		if($.browser.msie&&($.browser.version == "10.0")){ 
			alert("ie10"); 
			}  
	});  
	*/
</script>
</head>

<body style="background-color:transparent;" topmargin="0" onload="formOnLoad();">
	<!--  
	
-->
	<%
		if (isIE8m) {
	%>
	<input type="hidden" name="__Click" value="0">
	<input type="hidden" name="extraParams" id="extraParams"/>
	 <!-- 
	<a href="javascript:void(1==1);" class="addfile" style="cursor: default;" hidefocus="true"> <input class="addfile" onchange="upfileCallBack(this)" type="file" id="file_1" name="attachment"> </a>
	-->
	<a href="javascript:;" class="addfile" style="cursor: default;" hidefocus="true"> <input class="addfile" onchange="upfileCallBack(this)" type="file" id="file_1" name="attachment"> </a>
	<br>
	<input type="file" id="file_1" name="attachment" >
	<%
		} else if(isIE9p){
			%>
			<input type="hidden" name="__Click" value="0">
			<input type="hidden" name="extraParams" id="extraParams"/>
		
			 <input size="28" style="width: 66px;" onchange="upfileCallBack(this)" type="file" id="file_1" name="attachment"> 
			<br>
			
			<%	
		}else {
	%>
	<a id="selFile" onclick="selFile();"><img src="${pageContext.request.contextPath }/jsp/upload/attachment.gif">上传文件</a>
	<input type="hidden" name="extraParams"  id="extraParams"/>
	<div id="attachment_upload_list" style="display: none">
		<input type="file" name="attachment" id="file_1" onchange="upfileCallBack(this)" />
	</div>
	<%
		}
	%>



</body>
</html>
