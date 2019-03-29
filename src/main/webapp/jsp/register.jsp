<%@ page language="java" import="java.util.*,com.state.util.VersionCtrl" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.state.com/state" prefix="state"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="js/jquery/jquery-1.8.3.min.js?v=<%=VersionCtrl.getVesrsion()%>"></script>

<state:override name="head">
	<title>注册</title>
	<link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/style.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">

<script type="text/javascript">
	//注册按钮事件
		function register(){
			var user = $("#user").val();
			var password = $("#password").val();
			var passwords = $("#passwords").val();
			var area = $("#selectArea  option:selected").text();
			var sf=$('input[name="sf"]:checked').val();
			if(user.length==0){
				alert("用户名不能为空");
				return false;
			}
			if(password.length==0){
				alert("密码不能为空");
				return false;
			}
			if(password!=passwords){
				alert("两次密码输入不同，请核对后提交");
				return false;
			}
			var pd=confirm("您确定要对(用户："+user+",地区："+area+")注册吗？");
			if(pd==true){
				//检查用户是否存在
				var isNo=false;
				var option = {
						url : "${pageContext.request.contextPath }/login/containUser",
						type : "post",
						data : {
							user : user
						},
						dataType : "json",
						success : function(response) {
							if ("fail" == response) {
								alert("此帐户已存在");
								return false;
							}else if("noapprove" == response){
								alert("此帐户未审核");
							}else{
								var options = {
										url : "${pageContext.request.contextPath }/login/registerUser",
										type : "post",
										data : {
											user : user,
											password : password,
											area : area,
											sf : sf
										},
										dataType : "json",
										success : function(response) {
											if("success" == response) {
												alert("注册成功，请联系管理员进行审批");
												window.location.href = "${pageContext.request.contextPath }/login";
											}else {
												alert("注册失败");
											}
										},
										error : function() {
											alert("系统错误");
										}
									};
									$.ajax(options);
							}
						},
						error : function() {
							alert("检查用户是否存在系统出错");
						}
					};
					$.ajax(option);
			}
		}
	
	function goBack(){
		window.location.href="${pageContext.request.contextPath }/login";
	}
</script>


</state:override>
<state:override name="header"><div></div></state:override>
<state:override name="content">
	<div class="ttbg">
			<div class="mid">
				<div class="fl pdt10"><img src="${pageContext.request.contextPath }/img/logo.png"></div>
			</div>
			<div class="cl"></div>
		</div>
		<div class="rest">新用户注册</div>
		<div class="res">
			
			<div class="fl pdt20">
				<p><span class="wid40">用户名：&nbsp;&nbsp;&nbsp;&nbsp;</span>
					<input type="text" id="user" name="user" autofocus="autofocus" required="required" autocomplete="off" placeholder="请输入用户名"/></p>
				<p><span class="wid60">密　码：&nbsp;&nbsp;&nbsp;</span>
					<input type="password" id="password" name="password" class="loginInput" required="required" placeholder="请输入密码"/>
				</p>
				<p><span class="wid60">确认密码：</span>
					<input type="password" id="passwords" name="passwords" class="loginInput" required="required" placeholder="请再次输入密码"/>
				</p>
				<p><span class="wid60">区　域：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<select name="selectArea" id="selectArea" style="width:140px">
					<c:forEach var="n" items="${allArea}">
						<option value="hb">${n.area}</option>
					</c:forEach>
				</select></span></p>
				<p><span class="wid60">身　份：&nbsp;&nbsp;&nbsp;&nbsp;</span>
					<span><input name="sf" type="radio" checked="checked" value="buy"/>买方</span>
					<span> <input name="sf" type="radio" value="sale"/>卖方</span>
				</p>
				<div class="pdt100" style="border-color: red">
				   <span class="pdt100">
				    <a href="#"  class="btn1" onclick="goBack()">返回</a>
				   </span>
				   <a href="#" class="btn1" onclick="register()">注册</a>
				</div>
				

			</div>
		</div>
</state:override>

<%@ include file="/common/block/block_none.jsp" %>
