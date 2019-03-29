<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*,com.state.util.VersionCtrl"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>计算进度</title>
<script src="${pageContext.request.contextPath }/js/jquery/jquery-1.8.3.min.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
    
    
 <script type="text/javascript">
 
 var flag = true;
 var startIndex = 0;
 var pageIndex = 0;
 var pageSize = 1;
 function getIssueContent(){
	 if(flag == false){
		 return;
	 }
	 pageIndex++;
		$.ajax({
		    url : "../match/getIssueData",
			type : 'POST',
			dataType : 'json',
			data:{
				time:parent.$("#time").val(),
				pageIndex:pageIndex,
				pageSize:pageSize
			},
			success : function(result) {
				if(result != null && result != ""){
					var content = "";
					var f = document.getElementById("msg_end");
					for(var i = startIndex;i<result.length;i++){
						//content+="</br>"+result[i];
						//$('#content').append("<span>"+result[i]+"</span></br>");
						$('#content').html($('#content').html());
						f.scrollIntoView(); 
					}
					//$('#content').html($('#content').html()+content);
					startIndex = result.length;
					//var e = document.getElementById("content");
					//e.scrollTop = e.scrollHeight;
				}
				
			}
	
	 });
 }
 function shutdown(msg){
	// alert(00);
	var f = document.getElementById("msg_end");
	if(msg){
		
		//alert(msg);
		$('#content').html(msg+"<h3>-----------------计算结束-------------------</h3>");
	}else{
		$('#content').html($('#content').html()+"<h3>-----------------计算结束-------------------</h3>");
	}
	
	f.scrollIntoView(); 	 
	 flag = false;
	 startIndex = 0;
 }
 getIssueContent();
 window.setInterval(function(){ 
	 
	 getIssueContent();
	 
   	}, 3000); 
 </script>
</head>
<body>
<div style="float:inherit;">
<div id="content" style="margin:auto ;overflow:auto;width: 690px;heigth:490px;">
<h3>-----------------开始计算-------------------</h3>

</div>
<div id="msg_end" style="height:0px; overflow:hidden"></div>
</div>
</body>
</html>