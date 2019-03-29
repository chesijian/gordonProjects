<%@ page language="java" import="com.state.util.VersionCtrl,java.util.*,com.state.util.AuthoritiesUtil" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-store">
	<title>导航</title>
	<style type="text/css">
		body {
			margin: 0px auto;
			padding: 0px auto;
			width: 100%;
			height: 100%;
		}
		a {
			text-decoration: none;
		}
		.main_body {
			margin: 0px auto;
			padding: 0px auto;
			overflow: hidden;
			display: table;
			width: 100%;
			height: 100%;
		}
		.first_div {
			margin-right: 50px;
			float: left;
			overflow: hidden;
		}
		.second_div {
			margin-right: 50px;
			float: left;
			overflow: hidden;
		}
		.third_div {
			float: left;
			overflow: hidden;
		}
		img {
			position: absolute;
			width: 100%;
			z-index: -1;
		}
		#content {
			width: 75%;
			height: 660px;
			display: table-cell;
			vertical-align: middle;
		}
		#content .icon {
			width: 185px;
			height: 200px;
			overflow: hidden;
			background: #ff5253;
			text-align: center;
			cursor: pointer;
		}
		#content .icon em {
			width: 96px;
			height: 96px;
			overflow: hidden;
			background: url('${pageContext.request.contextPath }/js/navigation/img/back.png') no-repeat -98px top;
			display: block;
			margin: 27px auto;
			border-radius: 50%;
		}
		#content .bg01 {
			background: #F7B449;
		}
		#content .bg02 {
			background: #75C273;
		}
		#content .bg03 {
			background: #FF5253;
		}
		#content .bg02 em {
			background: url('${pageContext.request.contextPath }/js/navigation/img/back.png') no-repeat -197px top;
		}
		#content .bg03 em {
			background: url('${pageContext.request.contextPath }/js/navigation/img/back.png') no-repeat 0px top;
		}
		#content .icon span {
			font-size: 24px;
			font-weight: bold;
			color: #FFFFFF;
			letter-spacing: 5px;
		}
		#content .icon:hover em {
			-webkit-animation:tada 1s .2s ease both;
			-moz-animation:tada 1s .2s ease both;
			-o-animation:tada 1s .2s ease both;
			animation:tada 1s .2s ease both;
		}
		@-webkit-keyframes tada {
			0% {
				-webkit-transform:scale(1)
			}
			10%, 20% {
				-webkit-transform:scale(0.7) rotate(-3deg)
			}
			30%, 50%, 70%, 90% {
				-webkit-transform:scale(1.1) rotate(3deg)
			}
			40%, 60%, 80% {
				-webkit-transform:scale(1.1) rotate(-3deg)
			}
			100% {
				-webkit-transform:scale(1) rotate(0)
			}
		}
		@-moz-keyframes tada {
			0% {
				-moz-transform:scale(1)
			}
			10%, 20% {
				-moz-transform:scale(0.8) rotate(-5deg)
			}
			30%, 50%, 70%, 90% {
				-moz-transform:scale(1.1) rotate(5deg)
			}
			40%, 60%, 80% {
				-moz-transform:scale(1.1) rotate(-5deg)
			}
			100% {
				-moz-transform:scale(1) rotate(0)
			}
		}
		@-o-keyframes tada {
			0% {
				-o-transform:scale(1)
			}
			10%, 20% {
				-o-transform:scale(0.8) rotate(-5deg)
			}
			30%, 50%, 70%, 90% {
				-o-transform:scale(1.2) rotate(5deg)
			}
			40%, 60%, 80% {
				-o-transform:scale(1.2) rotate(-5deg)
			}
			100% {
				-o-transform:scale(1) rotate(0)
			}
		}
		@keyframes tada {
			0% {
				transform:scale(1)
			}
			10%, 20% {
				transform:scale(0.8) rotate(-5deg)
			}
			30%, 50%, 70%, 90% {
				transform:scale(1.2) rotate(5deg)
			}
			40%, 60%, 80% {
				transform:scale(1.2) rotate(-5deg)
			}
			100% {
				transform:scale(1) rotate(0)
			}
		}
	</style>

<script src="${pageContext.request.contextPath }/js/jquery/jquery-1.8.3.min.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
<script type="text/javascript">
	window.onload = function(){
		var sysContent = '<div class="third_div">' +
					'<a href="${pageContext.request.contextPath }/system/init">' +
				'<div class="icon bg03">' +
					'<em></em>' +
					'<span>系统管理</span>' +
				'</div>' +
			'</a>' +
		'</div>';
		//根据屏幕高度设置对应图片的高度
		var height = $(window).height();
		$('img').height(height);
		$('#content').height(height);
		if(<%=AuthoritiesUtil.isAllow("system") %>){
			document.getElementById("content").style.width = "75%";
			$(sysContent).insertAfter(".second_div");
		}else{
			document.getElementById("content").style.width = "65%";
		}
	}
</script>

</head>
<body>
	<div class="main_body" id="main_body">
		<img src="${pageContext.request.contextPath }/js/navigation/img/body_logo.jpg" />
		<div id="content">
			<div class="first_div">
				<a href="${pageContext.request.contextPath }/lineLimit/init">
					<div class="icon bg01">
						<em></em>
						<span>日前计划</span>
					</div>
				</a>
			</div>
			<div class="second_div">
				<a href="#">
					<div class="icon bg02">
						<em></em>
						<span>日内计划</span>
					</div>
				</a>
			</div>
<!-- 			<div class="third_div">
				<a href="${pageContext.request.contextPath }/system/init">
					<div class="icon bg03">
						<em></em>
						<span>系统管理</span>
					</div>
				</a>
			</div> -->
		</div>
	</div>
</body>
</html>