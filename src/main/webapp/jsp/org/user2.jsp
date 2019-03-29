<%@ page language="java" import="java.util.*,com.state.util.VersionCtrl"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html>
<head>
<title>用户信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!-- 引入 Bootstrap 
<link
	href="${pageContext.request.contextPath }/bootstrap/css/bootstrap-theme.min.css?v=<%=VersionCtrl.getVesrsion()%>"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath }/bootstrap/css/bootstrap.min.css?v=<%=VersionCtrl.getVesrsion()%>"
	rel="stylesheet">
	<link
	href="${pageContext.request.contextPath }/bootstrap/css/bootstrap-responsive.css?v=<%=VersionCtrl.getVesrsion()%>"
	rel="stylesheet">
	-->
<link
	href="${pageContext.request.contextPath }/bootstrap/plugin/css/bootstrap.css"
	rel="stylesheet" />
<link
	href="${pageContext.request.contextPath }/bootstrap/plugin/css/bootstrap-responsive.css"
	rel="stylesheet" />
	<!--
<link
	href="${pageContext.request.contextPath }/bootstrap/plugin/css/stilearn.css"
	rel="stylesheet" />
<link
	href="${pageContext.request.contextPath }/bootstrap/plugin/css/stilearn-responsive.css"
	rel="stylesheet" />
<link
	href="${pageContext.request.contextPath }/bootstrap/plugin/css/stilearn-helper.css"
	rel="stylesheet" />
<link
	href="${pageContext.request.contextPath }/bootstrap/plugin/css/stilearn-icon.css"
	rel="stylesheet" />
 
        <link href="${pageContext.request.contextPath }/bootstrap/plugin/css/font-awesome.css" rel="stylesheet" />
        
        <link href="${pageContext.request.contextPath }/bootstrap/plugin/css/animate.css" rel="stylesheet" />
		<!-- 
		<link href="${pageContext.request.contextPath }/bootstrap/plugin/css/datepicker.css" rel="stylesheet" />
        <link href="${pageContext.request.contextPath }/bootstrap/plugin/css/colorpicker.css" rel="stylesheet" />
         -->
<link
	href="${pageContext.request.contextPath }/bootstrap/plugin/css/select2.css"
	rel="stylesheet" />
	<!-- 
<link
	href="${pageContext.request.contextPath }/bootstrap/plugin/css/bootstrap-wysihtml5.css"
	rel="stylesheet" />
	
<link
	href="${pageContext.request.contextPath }/bootstrap/plugin/css/uniform.default.css?v=<%=VersionCtrl.getVesrsion()%>"
	rel="stylesheet">
	 -->
<!-- 
<script
	src="${pageContext.request.contextPath }/bootstrap/js/jquery-1.10.2.js?v=<%=VersionCtrl.getVesrsion()%>"
	type="text/javascript"></script>
	-->
<!-- 包括所有已编译的插件 
<script
	src="${pageContext.request.contextPath }/bootstrap/js/bootstrap.min.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
-->

</head>
<body>
	<!--controls-->
	<div id="controls" class="row-fluid">
		<!--span-->
		<div class="span12">
			<!--box-->
			<div class="box corner-all">
				<!--box header-->
				<div class="box-header grd-white color-silver-dark corner-top" align="center">

					<span >用户信息</span>
				</div>
				<!--/box header-->
				<!--box body-->
				<div class="box-body">
					<!--grid sizing-->
					<form class="form-horizontal" id="form-validate">
						<fieldset>

							<div class="control-group">
								<label class="control-label" for="userId">用户编号</label>
								<div class="controls">
									<input type="text" class="grd-white" id="userId"
										data-validate="{required: true, messages:{required:'用户编号不能为空'}}" />
									<span class="help-inline" style="display: none">不能为空</span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="userName">用户名称</label>
								<div class="controls">
									<input type="text" class="grd-white" id="userName"
										data-validate="{required: true, messages:{required:'请输入用户名'}}" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="sf">用户身份</label>
								<div class="controls">
									<select id="sf" style="width: 220px;" data-form="select2">
										<option value="bug">买方</option>
										<option value="sale">卖方</option>
										<option value="all">所有</option>
									</select>
								</div>

							</div>
							<div class="control-group">
								<label class="control-label" for="role">所属角色</label>
								<div class="controls">
									<select id="role" style="width: 220px;" data-form="select2">
										<option value="bug">买方</option>

									</select>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="selectArea">所属区域</label>
								<div class="controls">
									<select id="selectArea" style="width: 220px;"
										data-form="select2">
										<option value="hb">国调</option>

										<option value="hb">江苏</option>

										<option value="hb">山东</option>

										<option value="hb">上海</option>

										<option value="hb">四川</option>

										<option value="hb">浙江</option>

									</select>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="password">密码</label>
								<div class="controls">
									<input type="password" class="grd-white"
										data-validate="{required: true, messages:{required:'请输入密码'}}"
										name="password" id="password" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="cpassword">确认密码</label>
								<div class="controls">
									<input type="password" class="grd-white"
										data-validate="{required: true, equalTo: '#password', messages:{required:'请输入确认密码', equalTo: '两次输入的密码不一致'}}"
										name="cpassword" id="cpassword" />
								</div>
							</div>
							<div class="form-actions">
								<button type="submit" class="btn btn-primary">保存</button>
								<button type="button" class="btn">取消</button>
							</div>

						</fieldset>
					</form>
					<!--/grid sizing-->
				</div>
				<!--/box body-->
			</div>
			<!--/box-->
		</div>
		<!--/span-->
	</div>
	<!--/controls-->


	<!-- javascript
        ================================================== -->
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/jquery.js"></script>
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/jquery-ui.min.js"></script>
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/bootstrap.js"></script>
	<!--
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/uniform/jquery.uniform.js"></script>
	  
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/datepicker/bootstrap-datepicker.js"></script>
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/colorpicker/bootstrap-colorpicker.js"></script>
-->
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/select2/select2.js"></script>
	<!--
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/wysihtml5/wysihtml5-0.3.0.js"></script>
	 
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/wysihtml5/bootstrap-wysihtml5.js"></script>
-->
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/validate/jquery.validate.js"></script>
	<!-- -->
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/validate/jquery.metadata.js"></script>

	<!--
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/wizard/jquery.ui.widget.js"></script>
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/wizard/jquery.wizard.js"></script>
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/responsive-tables/responsive-tables.js"></script>
 	-->
	<!-- required stilearn template js, for full feature-->
	<!-- 
        <script src="${pageContext.request.contextPath }/bootstrap/plugin/js/holder.js"></script>
        <script src="${pageContext.request.contextPath }/bootstrap/plugin/js/stilearn-base.js"></script>
	-->
	<script src="${pageContext.request.contextPath }/js/org/user.js"></script>

	<script type="text/javascript">
		
	</script>
</body>
</html>