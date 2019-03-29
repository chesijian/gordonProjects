<%@ page language="java" import="java.util.*,com.state.util.AuthoritiesUtil,com.state.util.VersionCtrl" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>用户管理</title>
<link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/style.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	<Link Rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/common.css?v=<%=VersionCtrl.getVesrsion()%>">
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/ui-dialog.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
     <link href="${pageContext.request.contextPath }/bootstrap/css/bootstrap.min.css?v=<%=VersionCtrl.getVesrsion()%>" rel="stylesheet">
   
     <script src="${pageContext.request.contextPath }/bootstrap/js/jquery-1.10.2.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
     <script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.twbsPagination.min.js"></script>
     <script type="text/javascript" src="${pageContext.request.contextPath }/js/common/common-public-1.0.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
     <script src="${pageContext.request.contextPath }/bootstrap/js/bootstrap.min.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
     <script src="${pageContext.request.contextPath }/js/state/dialog/dialog-min.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/state/dialog/dialog-plus.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/state/dialog/sea.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
<script src="${pageContext.request.contextPath }/js/state/usermanager.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
<style>
.rbtn {
    border-radius: 4px;
    background-color: #ffffff;
    padding: 2px 10px 2px 10px;
    font-size: 14px;
    color: #06938e;
    border-top-style: outset;
    border-top-color: #06938e;
    border-left-style: outset;
    border-left-color: #06938e;
    border-bottom-style: inset;
    border-bottom-color: #06938e;
    border-right-style: inset;
    border-right-color: #06938e;
    border-top-width: thin;
    border-left-width: thin;
    border-bottom-width: thin;
    border-right-width: thin;
    /* font-family: "Microsoft YaHei" !important; */
    font-weight: normal;
    }
    
   </style>
	<script type="text/javascript">
	//设置默认当前页  
    var currentPage = 1;  
       //设置默认总页数  
    var totalPage = 1;
	$(function(){
		changePage(currentPage);
	});
	function changePage(currentPage){
		$('#pagination').empty();  
	    $('#pagination').removeData("twbs-pagination");  
	    $('#pagination').unbind("page");  
	    //将页面的数据容器制空  
	    $("#usersTable").empty();  
		    
		$.ajax({
			url : "changePage",
			type : 'POST',
			dataType : 'json',
			data : {
				currentPage:currentPage,
				limit:8
			},
			success : function(result) {
				var areaContent='<thead>'
					+'<th style="text-align:center">用户名</th>'
					+'<th style="text-align:center">地区</th>'
					+'<th style="text-align:center">权限</th>'
					+'<th style="text-align:center">状态</th>'
					+'<th style="text-align:center">操作</th></thead>';
				totalPage = result.totalPage;//将后台数据复制给总页数  
				currentPage = result.currentPage; 
				for(var i=0;i<result.list.length;i++){
					if(i%2!=0){
						areaContent+='<tr class="bgh">';
					}else{
						areaContent+='<tr>';
					}
					areaContent+=
					'<td>'+result.list[i].mname+'</td>'+
					'<td>'+result.list[i].area+'</td>'+
					'<td>'+result.list[i].roleName+'</td>';
					
					if(result.list[i].islogin=='1'){
						areaContent+='<td><a class="btn5">正常</a></td><td align="center" >';
					}
					if(result.list[i].islogin=='0'){
						areaContent+='<td><a class="btn5">停用</a></td><td align="center">';
					}
					
					if(<%=AuthoritiesUtil.isAllow("Manager_Btn_edit")%>){
						areaContent+='<a class="btn1" href="#" onclick="editUser(\''+result.list[i].id+'\');">修改</a>&nbsp;';
					}
					if(<%=AuthoritiesUtil.isAllow("Manager_Btn_enabled")%>){
						if(result.list[i].islogin=='1'){
							areaContent+='<a class="btn1" href="#" onclick="updateUser(\''+result.list[i].mname+'\',\'0\');">停用</a>';
						}
						if(result.list[i].islogin=='0'){
							areaContent+='<a class="btn1" href="#" onclick="updateUser(\''+result.list[i].mname+'\',\'1\');">启用</a>';
						}
					}
					if(<%=AuthoritiesUtil.isAllow("Manager_Btn_delete")%>){
						areaContent+='&nbsp;<a class="btn1" href="#" onclick="deleteUser(\''+result.list[i].id+'\');">删除</a>';
					}
					areaContent+='&nbsp;<a class="btn1" href="#" onclick="parent.editPwd(\''+result.list[i].id+'\');">修改密码</a>';
					areaContent+="</td> </tr>";
				}
				$("#usersTable").append(areaContent);
				$("#pagination").twbsPagination({
					totalPages: totalPage,
					visiblePages:"5",
					startPage: currentPage,
					first:"首页",
					prev:"上一页",
					next:"下一页",
					last:"最后一页",
					onPageClick:function(e,page){
						changePage(page);
					}
				});
			}
		});
	}
	
	
	$(function(){
		
		
	});
</script>
</head>
<body>
<div class="mid">
				<div class="contop">
					<div class="fl" style="float:left;"><!-- <span class="xmenu">用户管理</span> --><%-- <span class="count">未审批:${fn:length(nopass)},已审批:${fn:length(pass)}</span> --%></div>
					<c:if test='<%=AuthoritiesUtil.isAllow("Manager_Btn_add") %>'>
				    <div class="cl" style="float:right;"><span><a class='rbtn'  href="javascript:;"  onclick='newUser();return false;'>+新用户</a></span></div>
				    </c:if>
				</div>
							<table id="usersTable" class="table table-hover" style="margin-top:10px">
								<thead>
									
								</thead>
								
							</table>
						<div style="text-align: center;">
							<ul id="pagination" class="pagination" style="margin:0px;"></ul>
						</div>
						
			    
			</div>
</body>
</html>