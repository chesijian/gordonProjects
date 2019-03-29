<%@ page language="java" import="com.state.util.VersionCtrl,java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-store">
<script src="${pageContext.request.contextPath }/js/newlogin/js/jquery/jquery-1.8.3.min.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
	<script src="${pageContext.request.contextPath }/js/newlogin/js/jquery/jquery.cookie.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
	<script src="${pageContext.request.contextPath }/js/newlogin/js/jquery/jquery.json.min.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
	<script src="${pageContext.request.contextPath }/js/security/security.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
	
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/js/newlogin/css/head.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">

	<title>登录</title>
	<link Rel="StyleSheet" Href="${pageContext.request.contextPath }/js/newlogin/css/style.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	<style type="text/css">
	.css_label{
		font-size: 10pt;
	}
	</style>


<script type="text/javascript">
	
	$(document).ready(function(){
		userField =  $("#loginuser");
		pwdField =  $("#loginpwd");
		getLoginInfo();
	});
	//注册按钮事件
	function register(){
		window.location.href = "${pageContext.request.contextPath }/login/register";
	}
	function reSet(){
		userField.val('');
		pwdField.val('');
	}
	//登录按钮事件
	function loginBtn(){
		//console.info(userField);
		//alert(userField+userField.val())
		var user = userField.val();
		var password = pwdField.val();
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
		rmbLoginInfo();
		var option = {
			url : "${pageContext.request.contextPath }/login/validLogin",
			type : "post",
			data : {
				user : _encode(user),
				password : _encode(password)
			},
			dataType : "json",
			success : function(response) {
				var rs = response;
				if (rs.status) {
					//window.location.href = "${pageContext.request.contextPath }/declare/init";
					window.location.href = "${pageContext.request.contextPath }/login/navigation";
				}else {
					alert(rs.msg);
				}
			},
			error : function() {
				alert("系统错误");
			}
		};
		$.ajax(option);
	}
	
	//记住账号密码
	function rmbLoginInfo(){
		var obj = document.getElementsByName("loginRecord");
		var isRecord = "";
		for(var i=0; i<obj.length; i++){
			 if(obj[i].checked) isRecord+=obj[i].value+',';
		}
		if(isRecord != null && isRecord != ""  ){
			isRecord = isRecord.substring(0,isRecord.length-1);
		}
		 $.cookie("isRecord", isRecord, { expires: 7 }); //存储一个带7天期限的cookie
         $.cookie("username", userField.val(), { expires: 365 });
         $.cookie("password", pwdField.val(), { expires: 365 });
		
		if(isRecord.indexOf("1") == -1 ){//账号置空
			//Cookies.clear("userId");	
			$.cookie("username","", { expires: 0 });
		}else{
			$.cookie("username", userField.val(), { expires: 365 });
		}
		if(isRecord.indexOf("2") == -1 ){//密码置空
			//Cookies.clear("pwd");	
			$.cookie("password","", { expires: 0 });
		}else{
			$.cookie("password", pwdField.val(), { expires: 365 });
		}
	}

	//从cookie获取账号密码
	function getLoginInfo(){
		if($.cookie("username") != null && $.cookie("password") != ""){
			userField.val($.cookie("username")) ;
			pwdField.val($.cookie("password"));
		}
		
		var isRecord = $.cookie("isRecord") ;
		if(isRecord != null ){
			if(isRecord.indexOf("1") != -1){
			document.getElementsByName("loginRecord")[0].checked = true;
			}
			if(isRecord.indexOf("2") != -1){
				document.getElementsByName("loginRecord")[1].checked = true;
			}
		}
		
	}
</script>

</head>
<body style="position:absolute;left:50%;margin-left:-790.5px;overflow:hidden;">
<div class="ttbg">
			<div class="mid">
				<div class="fl pdt10"><img src="${pageContext.request.contextPath }/js/newlogin/img/logo.png"></div>
			</div>
		</div>
<div style="width:1581px; height:717px; background:url('${pageContext.request.contextPath }/js/newlogin/img/login_body2.jpg'); position:absolute; top:35px; z-index:-1;">
		<div style="margin: auto; width: 548px; height: 230px;  position:relative; margin:auto; position:absolute;left:547px;top:235px;">
			<div class="regi">
				<div class="reg">
					富余可再生能源跨省区现货市场
					<p>
					&nbsp;
					<!--  
					Cross Region Power Market
					-->
					</p>
				</div>
					<div class="login">
						<ul>
						<li><input id="loginuser" autocomplete="off"  type="text" class="loginuser"  /></li>
						<li><input id="loginpwd" autocomplete="off"  type="password" class="loginpwd"  onkeydown="if(event.keyCode==13){$('#login').click();return false;}"/></li>
						<li><input autocomplete="off"   style="margin-left: 45px;" id="login" type="button" class="loginbtn" value="登录"  onclick="loginBtn()"  />
						<input  type="button" class="loginbtn" value="重置"  onclick="reSet()"  />
						<label class="css_label"><input autocomplete="off"  name="loginRecord" type="checkbox" value="1"  />记住账号</label><label class="css_label"><a href="#">忘记密码?&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></label></li>
						</ul>
					</div>
			</div>
		</div>
</div>
</body>
</html>



