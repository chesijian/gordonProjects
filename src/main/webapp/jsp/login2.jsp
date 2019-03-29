<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.state.com/state" prefix="state"%>
<script type="text/javascript" src="js/jquery/jquery-1.8.3.min.js"></script>

<state:override name="head">
	<title>登录</title>
	<link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/style.css" Type="Text/Css">


<script type="text/javascript">
	//注册按钮事件
	function register(){
		window.location.href = "${pageContext.request.contextPath }/login/register";
	}
	//登录按钮事件
	function loginBtn(){
		var user = $("#user").val();
		var password = $("#password").val();
		if(user.length==0){
			alert("用户名不能为空");
			return false;
		}
		if(password.length==0){
			alert("密码不能为空");
			return false;
		}
		validLogin(user, password);
	}
	//如果在方法中使用ajax的返回值作为方法的返回值一定要把ajax变成同步的	
	function validLogin(user, password) {
	    alert("${pageContext.request.contextPath }");
		var option = {
			url : "${pageContext.request.contextPath }/validLogin",
			type : "post",
			data : {
				user : user,
				password : password
			},
			dataType : "json",
			success : function(response) {
				if ("success" == response) {
					window.location.href = "${pageContext.request.contextPath }/declare/init";
				}else if("sucnoauthor" == response) {
					alert("此用户无权限，请联系管理员！");
				}else {
					alert("用户名或密码错误");
				}
			},
			error : function() {
				alert("系统错误");
			}
		};
		$.ajax(option);
	}
</script>


</state:override>
<state:override name="content">
	<div class="ttbg">
			<div class="mid">
				<div class="fl pdt10"><img src="${pageContext.request.contextPath }/img/logo.png"></div>
			</div>
			<div class="cl"></div>
		</div>
		<div>
			<div class="regi">
				<div>
					<div class="fl wid600"><img src="${pageContext.request.contextPath }/img/l1.png" /></div>
					<div class="fl pd16"><img src="${pageContext.request.contextPath }/img/l2.jpg" /></div>
					<div class="fl pd16"><img src="${pageContext.request.contextPath }/img/l3.jpg" /></div>
					<div class="cl"></div>
				</div>
				<div>
					<div class="fl pd16"><img src="${pageContext.request.contextPath }/img/l1.jpg" /></div>
					<div class="fl pd16"><img src="${pageContext.request.contextPath }/img/l4.jpg" /></div>
					<div class="fl pd16"><img src="${pageContext.request.contextPath }/img/l5.jpg" /></div>
					<div class="fl pd16"><img src="${pageContext.request.contextPath }/img/l6.jpg" /></div>
					<div class="fl pd16"><img src="${pageContext.request.contextPath }/img/l7.jpg" /></div>
					<div class="fl pd16"><img src="${pageContext.request.contextPath }/img/l8.jpg" /></div>
					<div class="cl"></div>
				</div>
				<div class="login">
					<div class="fl pdds"><img src="${pageContext.request.contextPath }/img/l9.jpg" /></div>
					<div class="fl pdl15"><img src="${pageContext.request.contextPath }/img/l10.jpg" /></div>
					<div class="fl pdt20">
						<p>
							<span class="wid40">用户名：</span><input type="text" id="user" autofocus="autofocus" required="required" autocomplete="off" placeholder="请输入用户名" value=""/>
						</p>
						<p>
							<span class="wid60">密    码：</span>    <input type="password" id="password" name="password" required="required" placeholder="请输入密码" value="" onkeydown="if(event.keyCode==13){$('#login').click();return false;}"/>
						</p>
						<p>
							<span class="wid100"><a href="#" id="login" class="btn1" onclick="loginBtn()">登录</a></span><a href="#" class="btn1" onclick="register()">注册</a></p>
					</div>
				</div>
			</div>
		</div>

</state:override>

<%@ include file="/common/block/block_none.jsp" %>
